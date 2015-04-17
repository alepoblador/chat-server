
import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.net.Socket;

public class serverThread extends Thread {
	private Socket sock;
	private Server serv;
	private int attempt;
	
	/** Constructor
	 * 
	 */
	public serverThread( Socket s, Server srv ) {
		sock = s;
		serv = srv;
		attempt = 0;
	}
	
	@Override
	public void run() {
		Account acc = null;
		try {
			BufferedWriter out = new BufferedWriter (new OutputStreamWriter (sock.getOutputStream()));
			BufferedReader in = new BufferedReader (new InputStreamReader (sock.getInputStream()));
			String user;
			
			// authenticate login
			while ( true ) {
				out.write( ">Username: " );
				out.newLine();
				out.flush();
				user = in.readLine();
				
				if ( serv.isBlockedIP( sock.getInetAddress() , user ) ) {
					sendmessage( ">This IP address and username are blocked.", out );
					continue;
				}
				
				if ( !serv.isValidUser( user ) ) {
					sendmessage(">Invalid username, please try again!", out);
				} else {
					acc = serv.findUser( user );
					if ( acc.getActive() ) {
						sendmessage(">You are already logged on!", out);
						continue;
					} else {
						break;
					}
				}
			} //endwhile
			
			// block IP and user after 3 attempts
			while ( attempt <= 3 ) {
				out.write( ">Password: " );
				out.newLine();
				out.flush();
				String pass = in.readLine();
				acc = serv.authenticate ( user, pass );
				if ( acc != null ) {
					break;
				} else {
					sendmessage(">Wrong password! Try again.", out); 
					attempt++;
				}
				
				if ( attempt==3 ) {
					sendmessage( ">After 3 attempts username "+user+" is now blocked.", out );
					serv.addBlockIP ( sock.getInetAddress(), user );
					sock.close(); //close socket
					return;
				}
			} //end while
				
			sendmessage( ">Welcome!\n", out );
			acc.setSocket( sock );
			acc.setBufferedWriter( out );
			acc.onActive();
			
			// now wait for client
			while ( true ) {
				sendmessage(">Enter a command: ", out);
				String cmd = in.readLine();
				
				StringTokenizer st = new StringTokenizer( cmd );
				String word = st.nextToken();
				
				//whoelse 
				if ( word.equals("whoelse") ) {
					ArrayList<Account> activeusers = serv.getActiveUsers();
					String names = null;
					for ( Account r : activeusers ) {
						if ( !r.getUser().equals( user ) ) {
							if ( names == null )
								names = r.getUser();
							else
								names = names+", "+ r.getUser();
						}
					}
					if ( activeusers == null ) {
						sendmessage( ">No other accounts are online.\n", out );
					} else {
						sendmessage( ">Who else is online?... "+names, out );
					}
				} // end whoelse
				
				// wholasthr 
				if ( word.equals("wholasthr") ) {
					String users = serv.getActiveUsersLastHr();
					sendmessage( users, out );
				} //end wholasthr
				
				// message 
				if ( word.equals("message") ) {
					word = st.nextToken();
					if ( serv.isValidUser( word ) ) {
						Account rec = serv.findUser( word );
						if ( rec.getUser().equals(user) ) {
							sendmessage( ">You can't message yourself!", out );
						} else {
							// add all words to one string to send
							word = st.nextToken();
							while ( st.hasMoreElements() ) {
								word = word + " " + st.nextToken(); 
							}
							BufferedWriter msgout = rec.getBufferedWriter();
							String message = user+": "+word;
							
							sendmessage(  message, msgout );

						}	
					} else {
						sendmessage( ">"+user+" does not exist.", out );
					}
				} // end message command
				
				// broadcast 
				if ( word.equals("broadcast") ) {
					word = st.nextToken();
					while ( st.hasMoreElements() ) {
						word = word + " "+ st.nextToken(); 
					}
					ArrayList<Account> activeusers = serv.getActiveUsers();
					for ( Account r : activeusers ) {
						BufferedWriter broadcastout = r.getBufferedWriter();
						sendmessage( ">"+user+" broadcast: "+ word, broadcastout );
					}
				} // end broadcast
			
				// logout 
				if ( word.equals("logout") ) {
					if ( acc!=null ) {
						acc.setSocket( null );
						acc.setBufferedWriter(  null);
						acc.offActive();
						acc.setLogOffTime();
					}
					sock.close();
					return;
				} // end logout
			} // end wait
					
			
		} catch ( IOException i ) {
			if ( acc!=null ) {
				acc.setSocket( null );
				acc.setBufferedWriter(  null);
				acc.offActive();
				acc.setLogOffTime();
			}
			i.printStackTrace();
		}
		System.out.println("close");
					
	} // end run

	/** sendmessage - sends a string through a bufferedfwriter
	 * 
	 * @param message - string to send
	 * @param out - bufferedwriter
	 */
	public void sendmessage( String s, BufferedWriter b ) {
		try {
			b.write (s);
			b.newLine ();
			b.flush ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

}
