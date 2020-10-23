import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class ClientHandler implements Runnable {
	public String name;
	public UUID id;
	public Socket s;
	public BufferedReader in;
	public PrintWriter out;
	private static HashMap<UUID, ClientHandler> clients = new HashMap<UUID, ClientHandler>();

	public ClientHandler(Socket s, UUID id, String name, HashMap<UUID, ClientHandler> c) throws IOException{
		this.name =  name;
		this.id = id;
		this.s = s;
		clients = c;

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
						case "help":
							out.println("All commands starts with '..'");
							out.println("Commands\t\tDesc");
							out.println("list\t\tTo list all the online users");
							out.println("exit\t\tTo exit the chat");
							out.println("help\t\tTo show this message");
							break;
						case "list":
							out.println("-------Users Online(" + clients.size() + ")-------");
							for(ClientHandler client: clients.values()){
								out.println(">>> " + client.name +"#"+ client.id.toString().substring(30));
							}
							break;

						case "exit":
							out.println("Exiting...");
							clients.remove(this.id);
							sendToAll(this.name + " has left the chat");
							break;

						default:
							out.println("SERVER>>>No command named: " + cmd);

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
		for(ClientHandler client: clients.values()){
				client.out.println(msg);
		}
	}
}

class Server {
	private static int PORT = 9001;
	private static HashMap<UUID, ClientHandler> clients = new HashMap<UUID, ClientHandler>();
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
				UUID id =  UUID.randomUUID();
				ClientHandler client = new ClientHandler(s, id, name, clients);
				clients.put(id, client);
				System.out.println(name +"#"+id+ " has connected.");

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
