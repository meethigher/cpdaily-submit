package auto_login;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * Cpdaily ��Ϣ���һϵ�в�������
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021��1��10��
 */
public class Cpdaily {

	/**
	 * ��ȡ�������ر���Ϣ����
	 * 
	 * @return
	 */
	public static String[] getForm() {
		String todayData = HttpUtil.sendPost(Data.queryCollector, "{\"pageSize\": 6,\"pageNumber\": 1}",
				Data.getSubHeaders());
		JSONArray todayForm = null;
		try {
			// ����Ϣ
			todayForm = JSONObject.fromObject(todayData).getJSONObject("datas").getJSONArray("rows");
		} catch (Exception e) {
			System.out.println("��ȡ����Ϣʧ�ܣ�������ֹ�������¼�Ƿ�ʧЧ");
			System.exit(0);
		}
		String flag;// flag�����жϱ��β�ѯ�Ƿ�������
		String isHandled = "", collectWid = "", formWid = "";
		if (todayForm.size() < 1) {// û������
			flag = "no";
		} else {// ������
			flag = "yes";
			isHandled = todayForm.getJSONObject(0).getString("isHandled");
			collectWid = todayForm.getJSONObject(0).getString("wid");
			formWid = todayForm.getJSONObject(0).getString("formWid");
		}

		String[] fields = { flag, isHandled, collectWid, formWid };
		return fields;
	}

	/**
	 * ��ȡ��ϸ��������schoolTaskWid
	 * 
	 * @param fields
	 * @return
	 */
	public static String getSchool(String[] fields) {
		String param = "{\"collectorWid\":\"" + fields[2] + "\"}";
		// ��ϸ��
		String todayData = HttpUtil.sendPost(Data.detailCollector, param, Data.getSubHeaders());
		String todaySchool = null;
		try {
			todaySchool = JSONObject.fromObject(todayData).getJSONObject("datas").getJSONObject("collector")
					.get("schoolTaskWid").toString();
		} catch (Exception e) {
			System.out.println("��ȡѧУ��Ϣʧ�ܣ�������ֹ�������¼�Ƿ�ʧЧ");
			System.exit(0);
		}
		return todaySchool;
	}

	/**
	 * ��ȡ������ϸ�������ҵ�ѧУ��23���ʾ��Ҵ����pageSize������30
	 * 
	 * @param fields
	 * @return
	 */
	public static String getFields(String[] fields) {
		String param = "{\"pageSize\": 30, \"pageNumber\": 1, \"formWid\": " + fields[3] + ", \"collectorWid\": "
				+ fields[2] + "}";
		// ������
		String todayData = HttpUtil.sendPost(Data.formFields, param, Data.getHeaders());
		JSONArray todayRows = null;
		try {
			todayRows = JSONObject.fromObject(todayData).getJSONObject("datas").getJSONArray("rows");
		} catch (Exception e) {
			System.out.println("��ȡ����ϸ��Ϣʧ�ܣ�������ֹ�������¼�Ƿ�ʧЧ");
			System.exit(0);
		}
		return filterFields(todayRows);
	}

	/**
	 * ���˱���δѡ�������
	 * 
	 * @param array
	 * @return
	 */
	public static String filterFields(JSONArray array) {
		ArrayList list = new ArrayList();
		for (Object o : array) {
			JSONObject item = JSONObject.fromObject(o);
			item.put("fieldItems", changeJsonArray(item.get("fieldItems").toString()));
			list.add(item);
		}
		return list.toString();
	}

	/**
	 * ������fieldItems�е�null����ɾ����ֻ�������Ϲؼ��ֵ�����
	 * 
	 * @param fieldItems
	 * @return
	 */
	public static String changeJsonArray(String fieldItems) {
		JSONArray array = JSONArray.fromObject(fieldItems);
		ArrayList list = new ArrayList();
		for (Object o : array) {
			JSONObject item = JSONObject.fromObject(o);
			if (isSelected(item.getString("content"))) {
				item.put("isSelected", 1);
				list.add(item);
			}
		}
		return list.toString();
	}

	/**
	 * ѡ����йؼ��ֵ�ѡ��
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isSelected(String name) {
		String[] items = Data.key.split(",");
		for (String i : items) {
			if (i.equals(name))
				return true;
		}
		return false;
	}

	/**
	 * ����post���ύ�������ؽ��
	 * 
	 * @param fields
	 * @param schoolTaskWid
	 * @param address
	 * @return
	 */
	public static String submit(String[] fields, String schoolTaskWid, String address) {
		String formFields = getFields(fields);
		String param = new Form(fields[3], address, fields[2], schoolTaskWid, formFields).toString();
		System.out.println("�������ݣ�" + param);
		String result = HttpUtil.sendPost(Data.submitForm, param, Data.getSubHeaders());
		return JSONObject.fromObject(result).get("message").toString();
	}
}
