package lab3;

import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*
*
* @author Sarah Alzahrani
*/


public class User {

	private static Socket mySocket;

	public static void main(String[] args) throws Exception {

		mySocket = new Socket("localhost", 7676);
		BufferedReader networkInput = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()), true);		
		
		out.println(userInput.readLine());
		out.println(userInput.readLine());
		out.println(userInput.readLine());
		out.println(userInput.readLine());
	
		while (networkInput.readLine()!=null){
		String networkIn=networkInput.readLine();
		System.out.println(networkIn);}
		
		
		
	}
}
