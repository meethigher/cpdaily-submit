package auto_login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * HttpUtil ��������Get��Post���󹤾���
 * 
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021��1��6��
 */
public class HttpUtil {
	/**
	 * ����GET��������
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public static HttpURLConnection getConn(String url, Map<String, String> headers) {
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// ��������false��������Զ��ض���Ŀ���ַ
			conn.setInstanceFollowRedirects(false);
			if (headers != null) {
				Set<Entry<String, String>> set = headers.entrySet();
				for (Entry<String, String> header : set) {
					conn.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			conn.connect();
			return conn;
		} catch (Exception e) {
			System.out.println("��ȡ��������ʧ�ܣ�");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * ����Post��������
	 * 
	 * @param url
	 * @param param
	 * @param headers
	 * @return
	 */
	public static HttpURLConnection postConn(String url, String param, Map<String, String> headers) {
		BufferedWriter out = null;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// ��������false��������Զ��ض���Ŀ���ַ
			conn.setInstanceFollowRedirects(false);
			if (headers != null) {
				Set<Entry<String, String>> set = headers.entrySet();
				for (Entry<String, String> header : set) {
					conn.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			conn.setDoOutput(true);
			out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			out.write(param);
			out.flush();
			conn.connect();
			return conn;
		} catch (Exception e) {
			System.out.println("��ȡ��������ʧ�ܣ�");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����get���� 20200506
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String sendGet(String url, Map<String, String> headers) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// ��������false��������Զ��ض���Ŀ���ַ
			conn.setInstanceFollowRedirects(false);
			if (headers != null) {
				Set<Entry<String, String>> set = headers.entrySet();
				for (Entry<String, String> header : set) {
					conn.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("����GET��������쳣��");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * ����post���� 20200506
	 * 
	 * @param url
	 * @param param
	 * @param headers
	 * @return
	 */
	public static String sendPost(String url, String param, Map<String, String> headers) {
		BufferedWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// ��������false��������Զ��ض���Ŀ���ַ
			conn.setInstanceFollowRedirects(false);
			if (headers != null) {
				Set<Entry<String, String>> set = headers.entrySet();
				for (Entry<String, String> header : set) {
					conn.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			out.write(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("���� POST ��������쳣��");
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
