
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Client {

	public static void main( String[] args ) {
		try {
			String host = args[0];
			int port = Integer.parseInt(args[1] );
			Socket sock = new Socket( host, port );
			
			BufferedWriter out = new BufferedWriter (new OutputStreamWriter (sock.getOutputStream ()));
			BufferedReader in = new BufferedReader (new InputStreamReader (sock.getInputStream ()));
			
			// start clientWriter to read input
			clientWriter cw = new clientWriter( sock, out );
			cw.start();
			
			String servcmd;
			
			// wait for server
			while ( true ) {
				servcmd = in.readLine();
				if ( servcmd == null ) {
					System.exit(1);
				} else if (servcmd.equals (">Username: ") || servcmd.equals (">Password: ")||servcmd.equals (">Command: ")){
					System.out.print (servcmd);
				} else  {
					System.out.println (servcmd);
				}

			}
			
		} catch (SocketException s ) {
			System.out.println(s);
		} catch (IOException i) {
			System.out.println(i);
		}
		
		
	} //end main
	
}
