package auto_cookie;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * Data
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021年1月6日
 */
public class Data {

	/**
	 * session
	 */
	public static String modAuthCas = "";
	/**
	 * 提交问卷选项的关键字
	 */
	public static final String key = Config.getKey();
	/**
	 * 发件方邮箱，经过多次测试，腾讯企业邮箱是最稳定的
	 */
	public static final String fromMail = Config.getFromail();

	/**
	 * 腾讯企业邮箱密码
	 */
	public static final String fromMailPw = Config.getFromailPw();

	/**
	 * 学号
	 */
	public static final String id = Config.getId();
	/**
	 * 定位的地点
	 */
	public static final String address = Config.getPoi();

	/**
	 * 接收方邮箱
	 */
	public static final String toMail = Config.getTomail();

	/**
	 * 学校的host
	 */
	public static final String host = Config.getHost();

	/**
	 * 提交时用的请求头
	 * 
	 * @return
	 */
	public static Map<String, String> getSubHeaders() {
		Map<String, String> map = getHeaders();
		map.put("CpdailyStandAlone", "0");
		map.put("extension", "1");
		map.put("Cpdaily-Extension", CpdailyExtension.generateCpdailyExtension(id));
		return map;
	}

	/**
	 * 非提交时用的请求头
	 * 
	 * @return
	 */
	public static Map<String, String> getHeaders() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 11; MI 11 Build/QKQ1.190825.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 okhttp/3.8.1");
		map.put("Content-Type", "application/json;charset=utf-8");
		map.put("Connection", "Keep-Alive");
		map.put("Accept-Encoding", "gzip");
		map.put("Cookie", "MOD_AUTH_CAS=" + modAuthCas);
		return map;
	}

	/**
	 * 保持session
	 */
	public static final String keepingUrl = host + "/portal/index.html";
	/**
	 * 获取表单信息接口
	 */
	public static final String detailCollector = host + "/wec-counselor-collector-apps/stu/collector/detailCollector";
	/**
	 * 查询最新表单接口
	 */
	public static final String queryCollector = host
			+ "/wec-counselor-collector-apps/stu/collector/queryCollectorProcessingList";
	/**
	 * 获取表单详细信息接口
	 */
	public static final String formFields = host + "/wec-counselor-collector-apps/stu/collector/getFormFields";
	/**
	 * 提交最新表单接口
	 */
	public static final String submitForm = host + "/wec-counselor-collector-apps/stu/collector/submitForm";

}
