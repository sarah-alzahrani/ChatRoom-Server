package lab3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

/*
*
* @author Sarah Alzahrani
*/

public class ChatServer {

 ServerSocket serverSocket;
 static Hashtable<Socket, PrintWriter> UserList = new Hashtable<Socket, PrintWriter>();
 static ArrayList<ChatRoom> roomList = new ArrayList<ChatRoom>();
 
 static public void main(String args[])throws Exception{
 new ChatServer(7676);}

		
 public ChatServer(int port) throws IOException{
	 listen(port);}
	
 private void listen(int port) throws IOException {
	  serverSocket = new ServerSocket(port);
	  System.out.println("Start Listening on port: " + port);
	  while (true) {
	  Socket socket = serverSocket.accept();
	  PrintWriter print = new PrintWriter( new DataOutputStream(socket.getOutputStream()));
	  UserList.put(socket, print);
	  new ThreadHandler(this, socket);}}


  public class ThreadHandler extends Thread {
	
	private Socket socket;
	int UserID=1;


	public ThreadHandler(ChatServer chatServer, Socket socket) {
		this.socket= socket;
		start();}

	
	public void run() {
		try {
		BufferedReader RecievingSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter SendingSocket = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
		String userName;
			
		while (true) {
		String message = RecievingSocket.readLine();

		switch (message) {
				
		case "KILL_SERVICE":
			  socket.close();
			  RecievingSocket.close();
			  SendingSocket.close();
			  System.exit(0);
			  break;
				
		case "HELO":
			  InetAddress addr = InetAddress.getLocalHost(); 
			  String ip = addr.getHostAddress(); 
		      String HELO = message + "\n" + "IP:" +ip+ "\nPort:" + socket.getLocalPort() + "\n"+ "StudentID:14331356";
		      SendingSocket.println(HELO);
			  break;

		case "LEAVE_CHATROOM":
		     int ChatRoomRef1 = Integer.parseInt(message.split(": ")[1]);
			 int joinID1 = Integer.parseInt(RecievingSocket.readLine().split(": ")[1]);
			 userName = RecievingSocket.readLine().split(": ")[1];
			 ChatRoom chatRoom1 = ChatServer.roomList.get(ChatRoomRef1); 
			 for (int i = 0; i < chatRoom1.UsersList.size(); i++) {
			 UserDetails c = chatRoom1.UsersList.get(i);
			 if (c.getJoinID() == joinID1) {
			 chatRoom1.removeUser(c);}}
			  // leaving response 
			 SendingSocket.println("LEFT_CHATROOM: " +ChatRoomRef1);
			 SendingSocket.println("JOIN_ID: " + joinID1);
			 break;	
			 
		case "DISCONNECT":
			 RecievingSocket.readLine();// TCP 0
			 RecievingSocket.readLine();//port
			 RecievingSocket.readLine();//client name
			 RecievingSocket.close(); 
			 SendingSocket.close(); //
			 break;
			 
		case "JOIN_CHATROOM":
			  ChatRoom chatRoom , Room= null;
			  int ChatRoomRef = 0;
			  boolean flag = false;
			  
			  String ChatRoomName = message.split(": ")[1];//chatroom
			  @SuppressWarnings("unused")
			  String Ip= RecievingSocket.readLine();//ip address
			  @SuppressWarnings("unused")
			  String port=RecievingSocket.readLine();// port
			  userName = RecievingSocket.readLine().split(": ")[1];//user name
			  int joinID = UserID;
			  UserDetails User = new UserDetails(socket, userName,joinID); // initialize a new user

			  synchronized (roomList) {
		      for (int i = 0; i < roomList.size(); i++) {
			  Room = roomList.get(i);
			  if ((ChatRoomName.trim().equals(Room.getChatRoomName().trim()))) {
			  flag = true;
			  chatRoom = Room;
			  ChatRoomRef = roomList.indexOf(chatRoom);
			  chatRoom.addUser(User);}}
			  
		      if (!flag) {
			  chatRoom = new ChatRoom(ChatRoomName);
			  chatRoom.addUser(User);
			  roomList.add(chatRoom);
			  ChatRoomRef = roomList.indexOf(chatRoom);}}
			  // joining response 
			  SendingSocket.println("JOINED_CHATROOM: " + ChatRoomName);
			  SendingSocket.println("SERVER_IP: " + "0");
			  SendingSocket.println("PORT: " + "0");
			  SendingSocket.println("ROOM_REF: " + ChatRoomRef);
			  SendingSocket.println("JOIN_ID: " + joinID);
			  UserID++;
			  break;		  
			  
		case "CHAT":
		      int ChatRoomRef11 = Integer.parseInt(message.split(": ")[1]);
			  @SuppressWarnings("unused")
			  int joinID11 = Integer.parseInt(RecievingSocket.readLine().split(": ")[1]);
			  String UserName = RecievingSocket.readLine().split(": ")[1];
			  String text = RecievingSocket.readLine().split(": ")[1];
			
			  ChatRoom chatRoom11=roomList.get(ChatRoomRef11);
			  for (int i = 0; i < chatRoom11.UsersList.size(); i++) {
			  UserDetails User1 = chatRoom11.UsersList.get(i);
			  PrintWriter Send = new PrintWriter(new DataOutputStream(User1.socket.getOutputStream()));
			  // The server should send the following message to every client presently connected to the chat room
			  Send.println("CHAT: "+ ChatRoomRef11);
			  Send.println("CLIENT_NAME: "+ UserName);
			  Send.println("MESSAGE: "+ text);}					
	          break;	
	          
		default:
        break;
        }}}
		catch (IOException e) {
			e.printStackTrace();
		}} 	
}}
