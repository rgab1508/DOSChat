import java.net.Socket;
import java.io.*;

class Client {
	private static String ADDR = "127.0.0.1";
	private static int PORT;
	private static String name;

	public static void main(String args[]) throws IOException {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("+---------DOSChat--------+");
		System.out.print("Enter the PORT : ");
		PORT = Integer.parseInt(keyboard.readLine());
		System.out.println("port is " + PORT);

		Socket s = new Socket(ADDR, PORT);
		System.out.println("Connected to server..");

		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);

		System.out.println("Username : ");
		name = keyboard.readLine();

		out.println(name);

		while(true){
			System.out.print(name + "> ");
			String req = keyboard.readLine();
			out.println(req);
			String res = in.readLine();
			System.out.println(res);
		}
	}

}
