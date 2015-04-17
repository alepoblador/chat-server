
import java.io.*;
import java.util.*;
import java.net.Socket;

public class Account {
	private String username;
	private String password;
	private boolean isLoggedIn;
	private boolean isActive;
	private Socket s;
	private BufferedWriter bw;
	private long logOffTime;
	private ArrayList<String> blocklist;
	
	/** Constructor
	 * 
	 */
	public Account( String user, String pass ) {
		s = null;
		bw = null;
		username = user;
		password = pass;
		isLoggedIn = false;
		isActive = false;
		logOffTime = 0;
	}
	
	/** login - set account to logged in if inputted user and pass match accountList
	 * of Server
	 * 
	 * @param user - username
	 * @param pass - password
	 * @return
	 */
	public boolean login ( String user, String pass ) {
		if ( username.equals( user ) && password.equals( pass ) ) {
			isLoggedIn = true;
			return isLoggedIn;
		} else {
			isLoggedIn = false;
			return isLoggedIn;
		}
	}
	
	public void logout() {
		isLoggedIn = false;
	}
	
	public boolean getStatus() {
		return isLoggedIn;
	}
	
	/** block - blocks an account
	 * 
	 * @param acc - username of account to be blocked
	 * @return
	 */
	public String block( String acc ) {
		if ( blocklist.contains( acc ) ) {
			return ( "This account is already blocked!" );
		} else if ( username.equals( acc ) ) {
			return ( "You cannot block yourself!" );
		} else {
			blocklist.add( acc );
			return ( acc+" is now blocked from messaging you.");
		}
	}
	
	/** unblock - unblocks an account
	 * 
	 * @param acc - username of account to be blocked
	 * @return
	 */
	public String unblock( String acc ) {
		if ( blocklist.contains ( acc ) ) {
			blocklist.remove( acc );
			return ( "You have unblocked "+acc+"." );
		} else {
			return ( "This account is not blocked!" );
		}
	}
	
	/** isBlocked - return whether this account has blocked the param account
	 * 
	 * @param acc
	 * @return
	 */
	public boolean isBlocked( String acc ) { return blocklist.contains( acc ); }
	public ArrayList<String> getBlocklist() { return blocklist; } 
		
	public String getUser() { return username; }
	
	public long getLogOffTime() { return logOffTime; }
	public void setLogOffTime() { logOffTime = System.currentTimeMillis(); }
	
	public boolean getActive() { return isActive; }
	public void onActive() { isActive = true; }
	public void offActive() { isActive = false; }
	public Socket getSocket() { return s; }
	public void setSocket ( Socket sock ) { s = sock; }
	public BufferedWriter getBufferedWriter() { return bw; }
	public void setBufferedWriter ( BufferedWriter buff ) { bw = buff; }
	
}
