import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class ClientHandler implements Runnable {
	public String name;
	public Socket s;
	public BufferedReader in;
	public PrintWriter out;
	public ArrayList<ClientHandler> clients = new ArrayList<>();

	public ClientHandler(Socket s, String name, ArrayList<ClientHandler> clients) throws IOException{
		this.name =  name;
		this.s = s;
		this.clients = clients;

			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
			sendToAll(this.name + " has joined the Chat.");
	}

	public void run(){
		try {
			while(true){
			
				String msg = in.readLine();
				if(msg.startsWith("..")){
					//Server commands
					String cmd = msg.substring(2);
					switch(cmd){
						case "list":
							out.println("-------Users Online(" + this.clients.size() + ")-------");
							for(ClientHandler client: this.clients){
								out.println(">>> " + client.name);
							}
							break;

						case "exit":
							out.println("Exiting...");
							sendToAll(this.name + " has left the chat");
							break;

						default:
							out.println("No command named: " + cmd);

					}
				}
				else{
					sendToAll(this.name +"> " + msg);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void sendToAll(String msg){
		for(ClientHandler client: this.clients){
				client.out.println(msg);
		}
	}
}

class Server {
	private static int PORT = 9001;
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	public static void main(String args[]) throws IOException {
		System.out.println("Server serving.......");
	
		ServerSocket ss = new ServerSocket(PORT);
	
		// s is Client Socket
		try {
			while (true){
				Socket s = ss.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));	
				String name = in.readLine();
				ClientHandler client = new ClientHandler(s, name, clients);
				clients.add(client);
				System.out.println(name + " has connected.");

				pool.execute(client);
			}
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ss.close();
		}
	}
}
