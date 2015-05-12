package lab3;

import java.util.ArrayList;

/*
*
* @author Sarah Alzahrani
*/

public class ChatRoom {
	
	String ChatRoomName;
	int ChatRoomRef;
	ArrayList<UserDetails> UsersList = new ArrayList<UserDetails>();
	
	public ChatRoom(String roomName) {
		this.ChatRoomName = roomName;
	}
	
	public synchronized String getChatRoomName() {
		return ChatRoomName;
	}
	
	public synchronized  void setChatRoomName(String chatRoomName) {
		ChatRoomName = chatRoomName;
	}
	
	public synchronized int getChatRoomRef() {
		return ChatRoomRef;
	}
	
	public synchronized void setChatRoomRef(int chatRoomRef) {
		ChatRoomRef = chatRoomRef;
	}
	
	public synchronized ArrayList<UserDetails> getUsersList() {
		return UsersList;
	}
	
	public synchronized void setUsersList(ArrayList<UserDetails> usersList) {
		UsersList = usersList;
	}
	
	public synchronized void addUser(UserDetails User) {
		UsersList.add(User);
	}
	
	public synchronized void removeUser(UserDetails User) {
		UsersList.remove(User);
	}
	

}
