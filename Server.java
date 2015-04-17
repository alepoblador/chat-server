
import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ArrayList<Account> accountList;
	private ArrayList<BlockedIP> blockList;
	private long BLOCK_TIME;
	private long LAST_HOUR = 10000;
	
	public static void main ( String[] args ) throws IOException {
		Server s = new Server();
		int port = Integer.parseInt( args[0] );
		ServerSocket sock = new ServerSocket( port );
		
		while ( true ) {
			try {
				Socket c = sock.accept();
				System.out.println("Client connected...\n");
				new serverThread( c, s ).start();
			} catch ( IOException i ) {
				i.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	/** Constructor
	 * 
	 */
	public Server() throws FileNotFoundException {
		blockList = new ArrayList<BlockedIP>();
		
		Scanner in = null;
		try { 
			in = new Scanner( new FileReader("user_pass.txt") );
			accountList = new ArrayList<Account>();
			while ( in.hasNext() ) {
				Account a = new Account( in.next(), in.next() );
				accountList.add( a );
			}
		} finally {
			in.close();
		}
	}
	
	/** authenticate - checks if username and password match, then returns
	 * that account
	 * 
	 * @param user - username
	 * @param pass - password
	 * @return account if match, null if no match
	 */
	public Account authenticate( String user, String pass ) {
		for ( Account a : accountList ) {
			if ( a.login( user, pass ) ) {
				return a;
			}
		}
		return null;
	}
	
	/** findUser - find user in the account list
	 * 
	 * @param user - user
	 * @return account if found, null if not found
	 */
	public Account findUser( String user ) {
		for ( Account a : accountList ) {
			if ( a.getUser().equals( user ) )
				return a;
		}
		return null;
	}
	
	public boolean isValidUser( String user ) {
		for ( Account a : accountList ) {
			if ( a.getUser().equals( user ) )
				return true;
		}
		return false;
	}
	
	/** getActiveUsers - return list of active users
	 * 
	 * @return list of active users
	 */
	public ArrayList<Account> getActiveUsers() {
		ArrayList<Account> activeUsers = new ArrayList<Account>();
		for ( Account a : accountList  ) {
			if ( a.getActive() ) {
				activeUsers.add( a );
			}
		}
		return activeUsers;
	}
	
	/** getActiveUsersLastHr() - return string of active users in the last hour
	 * 
	 */
	public String getActiveUsersLastHr() {
		String activeUsersLastHr = null;
		long lasthr;
		
		for ( Account a : accountList ) {
			if ( a.getActive() ) {
				if ( activeUsersLastHr == null ) {
					activeUsersLastHr = a.getUser();
				} else {
					activeUsersLastHr = activeUsersLastHr + " " + a.getUser();
				}
			}
		}
		for ( Account a : accountList ) {
			lasthr = a.getLogOffTime();
			if ( System.currentTimeMillis() - lasthr < LAST_HOUR ) {
				if ( activeUsersLastHr == null ) {
					activeUsersLastHr = a.getUser();
				} else {
					activeUsersLastHr = activeUsersLastHr + " " + a.getUser();
				}
			}
		}
		return activeUsersLastHr;
	}
	
	/** addBlockIP - adds account to the blocked IP list
	 * 
	 * @param ip
	 * @param user
	 */
	public void addBlockIP( InetAddress ip, String user ) {
		BlockedIP b = new BlockedIP( ip, user );
		blockList.add( b );
	}
	
	/** isBlockedIP - checks if the ip/username pair is blocked
	 * 
	 * @param ip
	 * @param user
	 */
	public boolean isBlockedIP( InetAddress ip, String user ) {
		for ( BlockedIP b : blockList ) {
			if ( b.getUsername().equals( user ) && b.getIPaddress().equals( ip ) ) {
				if ( b.isBlocked( BLOCK_TIME ) ) {
					return true;
				} else
					continue;
			}
		}
		return false;
	}
	
	/** remove blockIP - removes account/ip address from the blocked list
	 * 
	 * @param ip
	 * @param user
	 * @return
	 */
	public String removeBlockIP ( InetAddress ip, String user ) {
		for ( BlockedIP b : blockList ) {
			if( (b.getUsername().equals( user )) && (b.getIPaddress().equals( ip )) ){
				blockList.remove(b);
				return ("You've unblocked " + user+".");
			}
		}
		return (user + "is not blocked.");
	}
	
} //end class



