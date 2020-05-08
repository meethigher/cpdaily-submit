package cpdailyAlpha;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 存储数据以及接口的类，部分需根据自己情况修改
 * @author kit chen
 *
 */
public class Data {
	/**
	 * 定位的地点
	 */
	public static final String address = "中国xx省xx市xx市";
	/**
	 * 学校的host
	 */
	public static final String host="https://你们学校.campusphere.net";
	/**
	 * 验证登录接口
	 */
	public static final String doLogin = host+"/iap/doLogin";
	/**
	 * 长春工业大学登录地址，不同学校不同
	 */
	public static final String loginUrl = "https://你们学校.campusphere.net/iap/login?service=https%3A%2F%2F.campusphere.net%2Fportal%2Flogin";
	/**
	 * 获取表单信息接口
	 */
	public static final String detailCollector = host+"/wec-counselor-collector-apps/stu/collector/detailCollector";
	/**
	 * 查询最新表单接口
	 */
	public static final String queryCollector = host+"/wec-counselor-collector-apps/stu/collector/queryCollectorProcessingList";
	/*
	 * 获取表单详细信息接口
	 */
	public static final String formFields=host+"/wec-counselor-collector-apps/stu/collector/getFormFields";
	/**
	 * 提交最新表单接口
	 */
	public static final String submitForm = host+"/wec-counselor-collector-apps/stu/collector/submitForm";
	
	/**
	 * 发件方邮箱，经过多次测试，腾讯企业邮箱是最稳定的
	 */
	public static final String fromMail="fafaf@gsgeg.onexmail.com";
	
	/**
	 * 腾讯企业邮箱密码
	 */
	public static final String fromMailPw="agdgushhfr466";
	
	/**
	 * 接收方邮箱
	 */
	public static final String toMail="meethigher@qq.com";
	/**
	 * Cpdaily-Extension需要根据实际情况
	 * @return
	 */
	public static Map<String, String> getSubHeaders() {
		Map<String, String> map = getHeaders();
		map.put("CpdailyStandAlone", "0");
		map.put("extension", "1");
		map.put("Cpdaily-Extension",
				"pHfasfmpjgBDFcbU+kEqAptH9XxFXqW5Afafaf xAJT8d1vAjJJAcTMofafasfMy2oySUl8QPIbvbnD/CXOa+Blv01iFxgqgiiy 5clnH13vy4OiDafD6CeI5h/gf+zI8EhyufCpcvfysmZiHyT8NWYNQuEy1nrK Ei2LtZtKxrx+37tNQ2tRHSEI5a+HXrm2Q6Y15+0BHHyg7EjIm1kymMTOcgAm imVlgyaeFHJ05Wd2");
		return map;
	}
	/**
	 * Cookie需要根据实际情况
	 * @return
	 */
	public static Map<String, String> getHeaders() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("tenantId", "ccut");
		map.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 10; MI 9 Build/QKQ1.190825.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 okhttp/3.8.1");
		map.put("Content-Type", "application/json;charset=utf-8");
		map.put("Host", "ccut.campusphere.net");
		map.put("Connection", "Keep-Alive");
		map.put("Accept-Encoding", "gzip");
		map.put("Cookie",
				"acw_tc=76b20fef15888014645921542e22c988f6643e4fde483d65f1e96045a37fc1; MOD_AUTH_CAS=ST-iap:1018612572276819107:ST:88af7wetw11:28 2020/5/4-01a0-4fa3-a33d-b0ca9c9458a1:20200504230017; CASTGC=iap-1018612572276819107-TGT-8faf1855-83cf-46e2-b01b-c6c0109754a9; AUTHTGC=iap-1018612572276819107-TGT-87bf1855-83cf-46e2-b01b-c6sadf45sh6sa54a9");
		return map;
	}

}
