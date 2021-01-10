package auto_login;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * Data
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021��1��6��
 */
public class Data {

	/**
	 * session
	 */
	public static String modAuthCas = "";
	/**
	 * �ύ�ʾ�ѡ��Ĺؼ���
	 */
	public static final String key = Config.getKey();
	/**
	 * ���������䣬������β��ԣ���Ѷ��ҵ���������ȶ���
	 */
	public static final String fromMail = Config.getFromail();

	/**
	 * ��Ѷ��ҵ��������
	 */
	public static final String fromMailPw = Config.getFromailPw();

	/**
	 * ѧ��
	 */
	public static final String id = Config.getId();
	/**
	 * ����
	 */
	public static final String pw = Config.getPw();
	/**
	 * ��λ�ĵص�
	 */
	public static final String address = Config.getPoi();

	/**
	 * ���շ�����
	 */
	public static final String toMail = Config.getTomail();

	/**
	 * ѧУ��host
	 */
	public static final String host = Config.getHost();

	/**
	 * �ύʱ�õ�����ͷ
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
	 * ���ύʱ�õ�����ͷ
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
	 * ����session
	 */
	public static final String keepingUrl = host + "/portal/index.html";
	/**
	 * ��ȡ����Ϣ�ӿ�
	 */
	public static final String detailCollector = host + "/wec-counselor-collector-apps/stu/collector/detailCollector";
	/**
	 * ��ѯ���±��ӿ�
	 */
	public static final String queryCollector = host
			+ "/wec-counselor-collector-apps/stu/collector/queryCollectorProcessingList";
	/**
	 * ��ȡ����ϸ��Ϣ�ӿ�
	 */
	public static final String formFields = host + "/wec-counselor-collector-apps/stu/collector/getFormFields";
	/**
	 * �ύ���±��ӿ�
	 */
	public static final String submitForm = host + "/wec-counselor-collector-apps/stu/collector/submitForm";

}