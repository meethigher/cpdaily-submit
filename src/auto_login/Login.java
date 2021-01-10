package auto_login;

import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * 
 * Login 用来登录获取cookie的工具类
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021年1月7日-2021年1月10日
 */
public class Login {
	private static String host = Data.host;
	private static String id = Data.id;
	private static String pw = Data.pw;
	// 最大试错次数
	private static int maxError = 10;
	// 这个值用来登录时携带，服务端有验证
	private static String lt;
	// 用来存放cookie
	private static String cookie;
	// 用来获取MOD_CAS_AUTH，返回值中ticket后面的值就是
	public static String doLogin = host + "/iap/doLogin";
	// 用来登录
	public static String login = host + "/portal/login";
	// 用来获取lt
	public static String getLt = host + "/iap/login?service=" + host + "/portal/login";
	// 用来验证lt
	public static String checkLt = host + "/iap/security/lt";
	// 用来获取验证码
	public static String getCaptcha = host + "/iap/generateCaptcha?ltId=";
	// 用来存放MOD_AUTH_CAS
	public static String MOD_AUTH_CAS = null;
	// 用于验证登录状态
	public static String task = host + "/portal/task/queryTodoTask";

	/**
	 * 通过正则截取字符串
	 * 
	 * @param s
	 * @param regex
	 * @return
	 */
	public static String getSub(String s, String regex) {
		// "(?<==)\\S+$"，正则用来提取=号之后的东西
		Matcher matcher = Pattern.compile(regex).matcher(s);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return null;
	}

	/**
	 * 请求头
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
		// 这个必须带着，不然登录时，要多一步获取cookie的步骤
		map.put("X-Requested-With", "XMLHttpRequest");
		map.put("Cookie", cookie);
		return map;
	}

	/**
	 * 获取验证码
	 * 
	 * @param url
	 * @return
	 */
	public static String getCaptcha(String url) {
		String s = CaptchaDecoding.parseCaptcha(CaptchaDecoding.downloadCaptcha(url, null));
		return s.substring(0, 5);
	}

	/**
	 * 获取LT
	 * 
	 * @param conn
	 * @return
	 */
	public static String getLt(HttpURLConnection conn) {
		return getSub(conn.getHeaderField("Location"), "(?<==)\\S+$");
	}

	/**
	 * 获取响应头中的cookie
	 * 
	 * @param conn
	 * @return
	 */
	public static String getCookie(HttpURLConnection conn) {
		return conn.getHeaderField("Set-Cookie").split(";")[0];
	}

	/**
	 * 生成登录请求体
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
	 * 进行登录
	 * 
	 * @param param
	 * @return
	 */
	public static String login(String param) {
		JSONObject object = JSONObject.fromObject(HttpUtil.sendPost(doLogin, param, getHeaders(cookie)));
		// 下面这串代码是开发时为了验证异步请求。结果证明需要。使用时直接注释，不用管
//		HttpURLConnection postConn = HttpUtil.postConn(doLogin,param,getHeaders(cookie));
//		System.out.println("输出："+postConn.getHeaderField("Location").replace(host+"/portal/login?", ""));

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
	 * 获取成功登录状态的cookie
	 * 
	 * @return
	 */
	public static String getAccess() {
		String captcha, body;
		System.out.println("获取登录数据...");
		HttpURLConnection conn = HttpUtil.getConn(getLt, null);
		lt = getLt(conn);
		System.out.println("获取lt：" + lt);
		cookie = getCookie(conn);
		System.out.println("获取cookie：" + cookie);
		int i = 1;
		String loginResult = null;
		while (i <= maxError) {
			captcha = getCaptcha(getCaptcha + lt);
			System.out.println("识别captcha：" + captcha);
			body = getLoginBody(captcha);
			System.out.println("生成body..." );
			System.out.print("正在尝试第" + i + "次登录：");
			loginResult = login(body);
			if ("success".equals(loginResult)) {
				break;
			} else if ("captchaError".equals(loginResult)) {
				System.out.println("captcha识别不正确！");
			} else if ("ltError".equals(loginResult)) {
				System.out.println("lt不匹配！");
			} else if ("upError".equals(loginResult)) {
				System.out.println("账户密码不匹配！");
			} else {
				System.out.println("检查账户是否冻结、今日校园官方系统是否异常、lt或账号密码是否为空，或者直接联系开发者meethigher@qq.com！");
			}
			i++;
		}
		if ("success".equals(loginResult)) {
			System.out.println("登录成功！");
			return MOD_AUTH_CAS;
		} else {
			System.out.println("登录失败！");
		}
		return null;
	}

	/**
	 * 验证是否已经失效
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
