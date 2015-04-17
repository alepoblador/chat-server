
import java.net.InetAddress;

public class BlockedIP {
	private InetAddress ip;
	private long time;
	private String user;
	
	/** Constructor
	 * 
	 */
	public BlockedIP( InetAddress i, String u ) {
		ip = i;
		user = u;
		time = System.currentTimeMillis();
	}

	/** isBlocked - return true if user/ip have been blocked within the blocktime
	 * 
	 * @param BLOCK_TIME
	 * @return
	 */
	public boolean isBlocked( long BLOCK_TIME ) {
		return (  !(time+BLOCK_TIME < System.currentTimeMillis() )  );
	}
	
	public InetAddress getIPaddress() { return ip; }
	public String getUsername() { return user; } 
	
}
