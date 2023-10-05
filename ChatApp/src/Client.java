import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	public Client(){
		
		
		try{
			System.out.println("Sending request...");
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("Connected!");
			
			
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			startReading();
			startWriting();

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void startReading() throws IOException {

		Runnable r1 = () -> {
			System.out.println("Reader started");
			try {
			while (!socket.isClosed()) {
				String message;
				
					message = br.readLine();

					if (message.equals("BYE")) {
						System.out.println("Server ended the chat");
						socket.close();
						break;
					}
					System.out.println("Server: " + message);
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
	public static void main(String[] args) {
		
		new Client();
	}
}
