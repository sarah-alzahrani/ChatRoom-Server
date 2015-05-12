package lab3;

import java.net.Socket;

/*
*
* @author Sarah Alzahrani
*/

public class UserDetails {
    Socket socket;
	String UserName;
	int JoinID;
	
	public UserDetails(Socket socket, String userName, int joinID) {
		super();
		this.socket = socket;
		UserName = userName;
		JoinID = joinID;
	}

	@Override
	public String toString() {
		return this.UserName.toString();
	}

	public synchronized Socket getSocket() {
		return socket;
	}

	public String getUserName() {
		return UserName;
	}

	public synchronized int getJoinID() {
		return JoinID;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public void setJoinID(int joinID) {
		JoinID = joinID;
	}
	
	
	
	
	
}
