package me.ranol.servertransfer;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.UUID;

import org.eclipse.swt.widgets.List;

public class AuthService {
	private static HashMap<Auth, String> uuid = new HashMap<>();
	private static List list;

	private AuthService() {
	}

	public static void connect(List list) {
		AuthService.list = list;
	}

	public static void forceUpdate() {
		list.removeAll();
		list.setItems(uuid.keySet().stream().map(Auth::toString).collect(Collectors.toList()).toArray(new String[0]));
	}

	public static class Auth {
		String id;
		String pwd;
		String salt = "";

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
			return "Auth [" + id + ", " + pwd + ", Salt=" + salt + "]";
		}

		public boolean equalsHash(Object obj) {
			if (!(obj instanceof Auth))
				return false;
			Auth a = (Auth) obj;
			return PasswordSaver.hashing(a.pwd).equals(pwd) && a.id.equals(id);
		}
	}

	public static boolean canLogin(String id, String pwd, String salt) {
		Auth auth = new Auth(id, pwd);
		auth.setSalt(salt);
		for (Auth a : uuid.keySet()) {
			if (auth.equalsFullyHash(a))
				return true;
		}
		return false;
	}

	public static boolean canLogin(String id, String pwd) {
		Auth auth = new Auth(id, pwd);
		for (Auth a : uuid.keySet()) {
			if (auth.equalsHash(a))
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
		list.add(temp.toString());
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
