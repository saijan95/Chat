
public class ChatServerTestProgram {
	public static void main(String[] args) { 
		ChatServerProcesses proc = new ChatServerProcesses();
		proc.login("saijan", "Google", "sfgsdfg", "fsedg");
		proc.login("firefox", "firefox", "Firefox", "sfgsfdg");
		
		System.out.println(proc.isOnline("firefox"));
		System.out.println(proc.isOnline("saijan"));
		
	}
}

