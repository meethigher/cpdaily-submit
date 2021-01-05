package auto_release;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * Config 读取配置文件工具类
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 *
 */
public class Config {
	private static String host;
	private static String id;
	private static String poi;
	private static String tomail;
	private static String fromail;
	private static String fromailPw;
	private static String key;
	public static Properties pro;
	static {
		ClassLoader cl = Config.class.getClassLoader();
		InputStream is = cl.getResourceAsStream("collection.properties");
		try {
			pro = new Properties();
			pro.load(is);
			host = pro.getProperty("host");
			id = pro.getProperty("id");
			poi = pro.getProperty("poi");
			tomail = pro.getProperty("tomail");
			fromail = pro.getProperty("fromail");
			fromailPw = pro.getProperty("fromailPw");
			key = pro.getProperty("key");
		} catch (IOException e) {
			System.out.println("读取配置文件出现错误");
			e.printStackTrace();
		}
	}

	public static String getHost() {
		return host;
	}

	public static String getId() {
		return id;
	}

	public static String getPoi() {
		return poi;
	}

	public static String getTomail() {
		return tomail;
	}

	public static String getFromail() {
		return fromail;
	}

	public static String getFromailPw() {
		return fromailPw;
	}

	public static String getKey() {
		return key;
	}
}
