package Object;

public class User implements java.io.Serializable {
	
	private static final long serialVersionUID = 3650424480931763566L;
	private String userName, domain, password;
	
	public User(){}
	
	public User(String u, String d, String p){
		this.userName = u;
		this.domain = d;
		this.password = p;
	}
	
	public String getUserName() {return this.userName;}
	public String getDomain() {return this.domain;}
	public String getPassword() {return this.password;}
	
	public String changeEmail(String newUser, String newDomain) {
		// TODO construct this function later
		return null;
	}

}
