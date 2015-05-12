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
				
		     /*
		      * the first to cases are from the previous multithreaded server which this server built on top of it
		      */
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
			 /*
			  * A client leaves a chat room by sending Leave message
			  * in response to the leave message , user will be find from specific room
			  * then this user will be removed and left message will be send to the others
			  */
		     int ChatRoomRef1 = Integer.parseInt(message.split(": ")[1]);
			 int joinID1 = Integer.parseInt(RecievingSocket.readLine().split(": ")[1]);
			 userName = RecievingSocket.readLine().split(": ")[1];
			 ChatRoom chatRoom1 = ChatServer.roomList.get(ChatRoomRef1); 
			 for (int i = 0; i < chatRoom1.UsersList.size(); i++) {
			 UserDetails userLeaving = chatRoom1.UsersList.get(i);
			 if (userLeaving.getJoinID() == joinID1) {
			 chatRoom1.removeUser(userLeaving);}}
			  // leaving response 
			 SendingSocket.println("LEFT_CHATROOM: " +ChatRoomRef1);
			 SendingSocket.println("JOIN_ID: " + joinID1);
			 break;	
			 
		case "DISCONNECT":
			/*
			 * To terminate the client/server connection, a client will send the following Discconect message to the server, 
			 * which should respond by terminating the connection.
			 */
			 @SuppressWarnings("unused")
			 String TCP=RecievingSocket.readLine();//TCP 0
			 @SuppressWarnings("unused")
			 String Port= RecievingSocket.readLine();//port
			 @SuppressWarnings("unused")
			 String name= RecievingSocket.readLine();//client name
			 RecievingSocket.close(); 
			 SendingSocket.close(); //
			 break;
			 
		case "JOIN_CHATROOM":
			  ChatRoom chatRoom , Room= null;
			  int ChatRoomRef = 0;
			  boolean flag = false;
			  
			  String ChatRoomName = message.split(": ")[1];// get the chatroom name
			  @SuppressWarnings("unused")
			  String Ip= RecievingSocket.readLine();//ip address will not be used in this method
			  @SuppressWarnings("unused")
			  String port=RecievingSocket.readLine();// port will not be used in this method
			  userName = RecievingSocket.readLine().split(": ")[1];//get the user name from the message
			  int joinID = UserID; // create a new id for the new joined user
			  UserDetails User = new UserDetails(socket, userName,joinID); // initialize a new user
              
			  /*
			   * the following condition is to test if the chat room that user entered exist or not in the chat room list
			   * if it exists then the user will be added to the room list, if it is not in the roomlist then a new instance 
			   * of chat room will be created and added to the roomlist as well as the new user
			   */
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
			  ChatRoomRef = roomList.indexOf(chatRoom);
			  chatRoom.addUser(User);
			  roomList.add(chatRoom);
			  }}
			  /*
			   *  joining response 
			   *  The Server responds with the following message, which must be sent before any chat messages are forwarded to the client.
			   *  The server should then send a message to the chat room indicating that the client has joined the chat room.
			   */
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
			  /*
			   * The server should send the following message to every client presently connected to the chat room
			   * ChatRoom is not in the chat message , this will be extracted by using the room reference
			   * the message then will be sent to all the useres in that chat room
			   */
			  ChatRoom chatRoom11=roomList.get(ChatRoomRef11);
			  for (int i = 0; i < chatRoom11.UsersList.size(); i++) {
			  UserDetails User1 = chatRoom11.UsersList.get(i);
			  PrintWriter Send = new PrintWriter(new DataOutputStream(User1.socket.getOutputStream()));
			  Send.println("CHAT: "+ ChatRoomRef11);
			  Send.println("CLIENT_NAME: "+ UserName);
			  Send.println("MESSAGE: "+ text);}					
	          break;	
	          
		default:
        break;
        }}}
		catch (IOException e) {
			System.out.println("ERROR_CODE: 444");
			System.out.println("ERROR_DESCRIPTION: A failure has accured in response to the recieveing of your message");
		}} 	
}}
