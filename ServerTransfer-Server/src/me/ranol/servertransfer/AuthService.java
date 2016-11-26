package me.ranol.servertransfer;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

public class AuthService {
	private static HashMap<Auth, String> uuid = new HashMap<>();

	private AuthService() {
	}

	public static class Auth {
		String id;
		String pwd;

		public Auth() {
			this("guest");
		}

		public Auth(String id) {
			this(id, "guest");
		}

		public Auth(String id, String pwd) {
			this.id = id;
			this.pwd = pwd;
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
			return a.pwd.equals(pwd) && a.id.equals(id);
		}

		public boolean equalsFullyHash(Object obj) {
			if (!(obj instanceof Auth))
				return false;
			Auth a = (Auth) obj;
			return PasswordSaver.hashing(a.pwd).equals(pwd) && a.id.equals(id);
		}

		@Override
		public String toString() {
			return "Auth [" + id + ", " + pwd + "]";
		}
	}

	public static boolean canLogin(String id, String pwd) {
		Auth auth = new Auth(id, pwd);
		for (Auth a : uuid.keySet()) {
			if (auth.equalsFullyHash(a))
				return true;
		}
		return false;
	}

	public static boolean validUUID(String id) {
		return uuid.containsValue(id);
	}

	public static String getUUID(String id) {
		if (!uuid.containsKey(new Auth(id)))
			return "";
		return uuid.get(new Auth(id));
	}

	public static String newAccount(String id, String pwd) {
		if (uuid.containsKey(new Auth(id)))
			return uuid.get(new Auth(id));
		Auth temp = new Auth(id, pwd);
		UUID uid = UUID.randomUUID();
		while (uuid.containsKey(uid.toString()))
			uid = UUID.randomUUID();
		uuid.put(temp, uid.toString());
		return uid.toString();
	}

	public static boolean removeByUUID(String uid) {
		if (uuid.containsValue(uid)) {
			Auth key = null;
			for (Entry<Auth, String> e : uuid.entrySet()) {
				if (e.getValue().equals(uid)) {
					key = e.getKey();
					break;
				}
			}
			if (key != null) {
				uuid.remove(key);
				return true;
			}
		}
		return false;
	}

	public static boolean removeById(String id) {
		Auth auth = new Auth(id);
		if (uuid.containsKey(auth)) {
			uuid.remove(auth);
			return true;
		}
		return false;
	}

	public static boolean exists(String id) {
		return uuid.containsKey(new Auth(id));
	}

	public static Auth getAccount(String id, String pwd) {
		if (canLogin(id, pwd)) {
			Auth result = null;
			for (Entry<Auth, String> e : uuid.entrySet()) {
				if (e.getKey().id.equals(id)) {
					result = e.getKey();
					break;
				}
			}
			if (result != null) {
				return result;
			}
		}
		return new Auth(id, pwd);
	}
}
