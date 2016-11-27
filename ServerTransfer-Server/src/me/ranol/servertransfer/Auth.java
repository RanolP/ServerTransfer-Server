package me.ranol.servertransfer;

public class Auth {

	String id;
	String pwd;
	String salt = "";
	public String nickname = "";

	public Auth() {
		this("guest");
	}

	public Auth(String id) {
		this(id, "guest");
	}

	public Auth(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
		nickname = id;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public int hashCode() {
		int result = 13;
		int c = id.hashCode();
		return 31 * result + c;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Auth && obj.hashCode() == hashCode();
	}

	public boolean equalsFully(Object obj) {
		if (!(obj instanceof Auth))
			return false;
		Auth a = (Auth) obj;
		return a.pwd.equals(pwd) && a.id.equals(id) && a.salt.equals(salt);
	}

	public boolean equalsFullyHash(Object obj) {
		if (!(obj instanceof Auth))
			return false;
		Auth a = (Auth) obj;
		return PasswordSaver.hashing(a.pwd).equals(pwd) && a.id.equals(id) && a.salt.equals(salt);
	}

	@Override
	public String toString() {
		return "Auth [" + id + ", " + pwd + ", Nick=" + nickname + "]";
	}

	public boolean equalsHash(Object obj) {
		if (!(obj instanceof Auth))
			return false;
		Auth a = (Auth) obj;
		return PasswordSaver.hashing(a.pwd).equals(pwd) && a.id.equals(id);
	}
}
