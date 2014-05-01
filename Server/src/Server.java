/*
 * James Lee
 * CS449 Introduction to Computer Security
 * Homework 4
 * Question 1
 * SSL Client-Server with Java
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Server Class
 * 
 * 		- Listens to Port
 * 		- Establishes the connection
 * 		- Reading/Writing from Socket
 */

public class Server {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		String portPrompt = args[0];
		int portNum = Integer.parseInt(args[1]);
		String dirPrompt = args[2];
		String directory = args[3];
		String pcksPrompt = args[4];
		String pcksFile = args[5];
		String authPrompt = args[6];
		String authFile = args[7];

		if (!portPrompt.equals("-p") | !dirPrompt.equals("-d")
				| !pcksPrompt.equals("-k") | !authPrompt.equals("-a")) {
			System.out.println("Incorrect Parameters.");
			System.out
					.println("Must run as \"java Server -p <port> -d <directory> -k <pcks_file> -a <authN_file>\"");
		}

		createSocket(portNum);

	}

	/*
	 * listenToPort() Creates a socket for the server Existing code was taken
	 * from:
	 * http://docs.oracle.com/javase/tutorial/networking/sockets/clientServer
	 * .html
	 */
	private static void createSocket(int portNum) throws IOException {
		// create a ServerSocket object for the port number of the user
		try (ServerSocket serverSocket = new ServerSocket(portNum); 
				// accepts connection from client
				Socket clientSocket = serverSocket.accept(); 
				/*
				 * If a connection between client and server is successful,
				 * accept() creates a new Socket Object bound to the same local
				 * port. The Server will continue to listen to the client via
				 * this socket.
				 */

				// Reader and Writer from and to client
				PrintWriter out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));) {
			String inputLine, outputLine;

			// Initiate conversation with client
			FileTransferProtocol ftp = new FileTransferProtocol();
			outputLine = ftp.processInput(null);
			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {
				outputLine = ftp.processInput(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye."))
					break;
			}
		}
	}
}