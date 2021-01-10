package auto_login;

import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * 
 * Login ������¼��ȡcookie�Ĺ�����
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021��1��7��-2021��1��10��
 */
public class Login {
	private static String host = Data.host;
	private static String id = Data.id;
	private static String pw = Data.pw;
	// ����Դ����
	private static int maxError = 10;
	// ���ֵ������¼ʱЯ�������������֤
	private static String lt;
	// �������cookie
	private static String cookie;
	// ������ȡMOD_CAS_AUTH������ֵ��ticket�����ֵ����
	public static String doLogin = host + "/iap/doLogin";
	// ������¼
	public static String login = host + "/portal/login";
	// ������ȡlt
	public static String getLt = host + "/iap/login?service=" + host + "/portal/login";
	// ������֤lt
	public static String checkLt = host + "/iap/security/lt";
	// ������ȡ��֤��
	public static String getCaptcha = host + "/iap/generateCaptcha?ltId=";
	// �������MOD_AUTH_CAS
	public static String MOD_AUTH_CAS = null;
	// ������֤��¼״̬
	public static String task = host + "/portal/task/queryTodoTask";

	/**
	 * ͨ�������ȡ�ַ���
	 * 
	 * @param s
	 * @param regex
	 * @return
	 */
	public static String getSub(String s, String regex) {
		// "(?<==)\\S+$"������������ȡ=��֮��Ķ���
		Matcher matcher = Pattern.compile(regex).matcher(s);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return null;
	}

	/**
	 * ����ͷ
	 * 
	 * @param cookie
	 * @return
	 */
	public static Map<String, String> getHeaders(String cookie) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 11; MI 11 Build/QKQ1.190825.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 okhttp/3.8.1");
		map.put("Content-Type", "application/x-www-form-urlencoded");
		map.put("Host", host);
		map.put("Connection", "Keep-Alive");
		map.put("Accept-Encoding", "gzip");
		// ���������ţ���Ȼ��¼ʱ��Ҫ��һ����ȡcookie�Ĳ���
		map.put("X-Requested-With", "XMLHttpRequest");
		map.put("Cookie", cookie);
		return map;
	}

	/**
	 * ��ȡ��֤��
	 * 
	 * @param url
	 * @return
	 */
	public static String getCaptcha(String url) {
		String s = CaptchaDecoding.parseCaptcha(CaptchaDecoding.downloadCaptcha(url, null));
		return s.substring(0, 5);
	}

	/**
	 * ��ȡLT
	 * 
	 * @param conn
	 * @return
	 */
	public static String getLt(HttpURLConnection conn) {
		return getSub(conn.getHeaderField("Location"), "(?<==)\\S+$");
	}

	/**
	 * ��ȡ��Ӧͷ�е�cookie
	 * 
	 * @param conn
	 * @return
	 */
	public static String getCookie(HttpURLConnection conn) {
		return conn.getHeaderField("Set-Cookie").split(";")[0];
	}

	/**
	 * ���ɵ�¼������
	 * 
	 * @param captcha
	 * @return
	 */
	public static String getLoginBody(String captcha) {
		if (captcha == null)
			captcha = "";
		return "username=" + id + "&password=" + pw + "&mobile=&dllt=&captcha=" + captcha + "&rememberMe=false&" + "lt="
				+ lt;
	}

	/**
	 * ���е�¼
	 * 
	 * @param param
	 * @return
	 */
	public static String login(String param) {
		JSONObject object = JSONObject.fromObject(HttpUtil.sendPost(doLogin, param, getHeaders(cookie)));
		// �����⴮�����ǿ���ʱΪ����֤�첽���󡣽��֤����Ҫ��ʹ��ʱֱ��ע�ͣ����ù�
//		HttpURLConnection postConn = HttpUtil.postConn(doLogin,param,getHeaders(cookie));
//		System.out.println("�����"+postConn.getHeaderField("Location").replace(host+"/portal/login?", ""));

		String string = null;
		if ("REDIRECT".equals(object.get("resultCode"))) {
			string = "success";
			HttpUtil.sendGet(object.getString("url"), getHeaders(""));
			MOD_AUTH_CAS = getSub(object.getString("url"), "(?<==)\\S+$");
		} else if ("CAPTCHA_NOTMATCH".equals(object.get("resultCode"))) {
			string = "captchaError";
		} else if ("LT_NOTMATCH".equals(object.get("resultCode"))) {
			string = "ltError";
		} else if ("FAIL_UPNOTMATCH".equals(object.get("resultCode"))) {
			string = "upError";
		} else {
			string = "error";
		}
		return string;
	}

	/**
	 * ��ȡ�ɹ���¼״̬��cookie
	 * 
	 * @return
	 */
	public static String getAccess() {
		String captcha, body;
		System.out.println("��ȡ��¼����...");
		HttpURLConnection conn = HttpUtil.getConn(getLt, null);
		lt = getLt(conn);
		System.out.println("��ȡlt��" + lt);
		cookie = getCookie(conn);
		System.out.println("��ȡcookie��" + cookie);
		int i = 1;
		String loginResult = null;
		while (i <= maxError) {
			captcha = getCaptcha(getCaptcha + lt);
			System.out.println("ʶ��captcha��" + captcha);
			body = getLoginBody(captcha);
			System.out.println("����body..." );
			System.out.print("���ڳ��Ե�" + i + "�ε�¼��");
			loginResult = login(body);
			if ("success".equals(loginResult)) {
				break;
			} else if ("captchaError".equals(loginResult)) {
				System.out.println("captchaʶ����ȷ��");
			} else if ("ltError".equals(loginResult)) {
				System.out.println("lt��ƥ�䣡");
			} else if ("upError".equals(loginResult)) {
				System.out.println("�˻����벻ƥ�䣡");
			} else {
				System.out.println("����˻��Ƿ񶳽ᡢ����У԰�ٷ�ϵͳ�Ƿ��쳣��lt���˺������Ƿ�Ϊ�գ�����ֱ����ϵ������meethigher@qq.com��");
			}
			i++;
		}
		if ("success".equals(loginResult)) {
			System.out.println("��¼�ɹ���");
			return MOD_AUTH_CAS;
		} else {
			System.out.println("��¼ʧ�ܣ�");
		}
		return null;
	}

	/**
	 * ��֤�Ƿ��Ѿ�ʧЧ
	 * 
	 * @return
	 */
	public static boolean isOff() {
		String result = HttpUtil.sendPost(task, "", getHeaders("MOD_AUTH_CAS=" + MOD_AUTH_CAS));
		if (result.indexOf("WEC-REDIRECTURL") > 0) {
			return true;
		} else {
			return false;
		}
	}
}
