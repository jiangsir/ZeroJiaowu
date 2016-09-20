package tw.jiangsir.Utils;

public class Resource {
	public static enum MESSAGE {
		Privilege_UNKNOWN("Message.UNKNOWN"), // 未知
		Privilege_FORBIDDEN("Message.FORBIDDEN"), // 明確禁止的
		Privilege_NOTDEFINE("Message.NotDefine"), // 未定義
		Privilege_ALLOWED("Message.Allow"); // 明確允許的
		private String key;

		private MESSAGE(String key) {
			this.key = key;
		}

		public String getKey() {
			return this.key;
		}
	}

}
