package auto_login;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * Config ��ȡ�����ļ�������
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021��1��6��
 */
public class Config {
	private static String host;
	private static String id;
	private static String pw;
	private static String poi;
	private static String tomail;
	private static String fromail;
	private static String fromailPw;
	private static String key;

	public static Properties pro;
	static {
		InputStream is =null;
		ClassLoader cl = Config.class.getClassLoader();
		try {
			//����ǿ�����������ʱ����
			is = cl.getResourceAsStream("collection.properties");
			//�����jar��ʱ����
//			is=new FileInputStream(new File("collection.properties"));
			pro = new Properties();
			pro.load(is);
			host = pro.getProperty("host");
			id = pro.getProperty("id");
			pw = pro.getProperty("pw");
			poi = pro.getProperty("poi");
			tomail = pro.getProperty("tomail");
			fromail = pro.getProperty("fromail");
			fromailPw = pro.getProperty("fromailPw");
			key = pro.getProperty("key");
		} catch (IOException e) {
			System.out.println("��ȡ�����ļ����ִ���");
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getHost() {
		return host;
	}

	public static String getId() {
		return id;
	}

	public static String getPw() {
		return pw;
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
