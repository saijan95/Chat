
public class Contact {
	private String name;
	private String status;
	private String username;
	private String ip;
	private int port;
	
	public Contact(String username, String name, String status) {
		this.name = name;
		this.username = username;
		this.status = status;
		this.ip = null;
	}
	
	protected void setOnline() {
		status = "ONLINE";
	}
	
	protected void setOffline() {
		status = "OFFLINE";
	}
	
	protected void setIp(String ip) {
		this.ip = ip;
	}
	
	protected void setPort(int port) {
		this.port = port;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	protected String getName() { return name; }
	protected String getStatus() { return status; }
	protected String getUsername() { return username; }
	public String toString() { return username; }
}
