
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Scanner;

class clientWriter extends Thread implements ActionListener {
	private Socket socket;
	private BufferedWriter out;
	private int TIME_OUT = 1800000;
	javax.swing.Timer idle_timer;
	
	/** Constructor
	*/
	public clientWriter( Socket s, BufferedWriter b ) {
		socket = s;
		out = b;
		idle_timer = new javax.swing.Timer( TIME_OUT, this );
		idle_timer.start();
	}
	
	/** run - thread waits for input
	*/
	public void run() {
		Scanner in = new Scanner( System.in);
		
		// wait for input
		while ( true ) {
			String input = in.nextLine();
			idle_timer.restart();
			
			try {
				out.write( input, 0, input.length() );
				out.newLine();
				out.flush();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	/** actionlistener for if user inactive
	*/
	public void actionPerformed( ActionEvent e) {
		try {
			out.write("logout");
			out.newLine();
			out.flush();
		} catch ( Exception e2 ){
			e2.printStackTrace();
		} finally {
			System.out.println("You have been logged out due to inactivity.");
			System.exit(0);
		}
	}
} // end clientWriter class