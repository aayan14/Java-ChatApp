import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame{
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	private JLabel heading = new JLabel("Client");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto", Font.PLAIN, 20);
	
	public Client(){
		
		
		try{
			System.out.println("Sending request...");
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("Connected!");
			
			
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			createGui();
			handleEvents();
			startReading();
//			startWriting();

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private void handleEvents() {
		
		messageInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == 10){
					String contentToSend = messageInput.getText();
					messageArea.append("You:" + contentToSend + "\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
				}
				
			}
		});
		
	}
	private void createGui() {
		
		heading.setFont(font);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		messageArea.setFont(font);
		messageInput.setFont(font);
		setLayout(new BorderLayout());
		messageArea.setEditable(false);
		add(heading, BorderLayout.NORTH);
		JScrollPane js = new JScrollPane(messageArea);
		add(js, BorderLayout.CENTER);
		add(messageInput, BorderLayout.SOUTH);
		
		
		
		setTitle("Client ");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
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
						JOptionPane.showMessageDialog(this, "Chat Terminated");
						messageArea.setEnabled(false);
						socket.close();
						System.exit(0);
						break;
					}
					messageArea.append("Server: " + message + "\n");
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
