import java.net.Socket;
import java.io.*;

class ServerConnection implements Runnable{
  Socket s;
  String name;
  BufferedReader in;
  public ServerConnection (Socket s, String name) throws IOException{
    this.s = s;
    this.name = name;
    in = new BufferedReader(new InputStreamReader(s.getInputStream()));                                                                       }                                                                   
  public void run(){
    try {
      while(true){                                                            
				String msg = in.readLine();
        if(!msg.equals("null") && !msg.startsWith(this.name)){
					System.out.println(msg);
        }
      }
    }catch(IOException e){
			//e.printStackTrace();
			System.out.println("Something went Wrong(Exiting.)");

    }
		finally{
			try{
				in.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
  }

}


class Client {
	private static String ADDR = "127.0.0.1";
	private static int PORT;
	private static String name;

	public static void main(String args[]) throws IOException {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("+---------DOSChat--------+");
		System.out.print("Enter the IP Add.(blank if local) : ");
		String ad = keyboard.readLine();
		if(!ad.equals("")){
			ADDR = ad;
		}

		System.out.print("Enter the PORT : ");
		PORT = Integer.parseInt(keyboard.readLine());
		System.out.println("port is " + PORT);

		Socket s = new Socket(ADDR, PORT);
		System.out.println("Connected to server..");


		System.out.print("Username : ");
		name = keyboard.readLine();
		
		ServerConnection in = new ServerConnection(s, name);
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);

		out.println(name);
		
		new Thread(in).start();
		System.out.println(".exit to quit chat");
		System.out.println(name + "> ");
		
		while(true){
			String msg = keyboard.readLine();
			if(msg.equals(".exit")) break;
			out.println(msg);
		}
		//closing rhe socket
		out.close();
		s.close();
	}

}
