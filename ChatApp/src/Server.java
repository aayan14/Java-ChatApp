import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {

	ServerSocket server;
	Socket socket;
	BufferedReader br;
	PrintWriter out;

	public Server() throws IOException {

		server = new ServerSocket(7777);
		System.out.println("Ready to accept connection...");
		socket = server.accept();

		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());

		startReading();
		startWriting();

	}

	public void startReading() throws IOException {

		Runnable r1 = () -> {
			System.out.println("Reader started");
			try {
			while (true) {
				String message;
				
					message = br.readLine();

					if (message.equals("BYE")) {
						System.out.println("Client ended the chat");
						socket.close();
						break;
					}
					System.out.println("Client: " + message);
				} 
			}
			catch (Exception e) {
//				e.printStackTrace();
				System.out.println("CONNECTION CLOSED");
			}

		};
		new Thread(r1).start();
	}

	public void startWriting() {
		Runnable r2 = () -> {
			System.out.println("Writer started");
			try {
			while(!socket.isClosed()){
				
					
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();
					out.println(content);
					out.flush();
					
					if(content.equals("BYE")){
						socket.close();
						break;
					}
					
				}
			System.out.println("CONNECTION CLOSED");
			}
			catch (Exception e) {
//				e.printStackTrace();
			}
		};
		new Thread(r2).start();

	}

	public static void main(String[] args) throws IOException {
		System.out.println("Server");
		new Server();
	}
}
