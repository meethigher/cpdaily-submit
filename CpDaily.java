package cpdailyAlpha;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * CpDaily类，有关通过接口获取数据的一系列请求方法
 * 
 * @author kit chen
 *
 */
public class CpDaily {
	/**
	 * 返回数据
	 * 
	 * @return
	 */
	public static String personalData() {
		return Data.form;
	}

	/**
	 * 进行post表单提交，并返回结果
	 * 
	 * @param fields
	 * @param schoolTaskWid
	 * @param address
	 * @return
	 */
	public static String submit(String[] fields, String schoolTaskWid, String address) {
		String formFields = getFields(fields);
		String param = new Form(fields[3], address, fields[2], schoolTaskWid, formFields).toString();
		String result = HttpUtil.sendPost(Data.submitForm, param, Data.getSubHeaders());
		return JSONObject.fromObject(result).get("message").toString();
	}

	/**
	 * 获取并返回具体的表单ID
	 * 
	 * @return
	 */
	public static String[] getForm() {
		String todayData = HttpUtil.sendPost(Data.queryCollector, "{\"pageSize\": 6,\"pageNumber\": 1}",
				Data.getSubHeaders());
		JSONArray todayForm = null;
		try {
			todayForm = JSONObject.fromObject(todayData).getJSONObject("datas").getJSONArray("rows");
		} catch (Exception e) {
			System.out.println("获取表单信息失败，程序终止。请检查登录是否失效");
			System.exit(0);
		}
		String flag;// flag用来判断本次查询是否有数据
		String isHandled = "", collectWid = "", formWid = "";
		if (todayForm.size() < 1) {// 没有数据
			flag = "no";
		} else {// 有数据
			flag = "yes";
			isHandled = todayForm.getJSONObject(0).getString("isHandled");
			collectWid = todayForm.getJSONObject(0).getString("wid");
			formWid = todayForm.getJSONObject(0).getString("formWid");
		}

		String[] fields = { flag, isHandled, collectWid, formWid };
		return fields;
	}

	/**
	 * 获取并返回学校的ID
	 * 
	 * @param fields
	 * @return
	 */
	public static String getSchool(String[] fields) {
		String param = "{\"collectorWid\":\"" + fields[2] + "\"}";
		String todayData = HttpUtil.sendPost(Data.detailCollector, param, Data.getSubHeaders());
		String todaySchool = null;
		try {
			todaySchool = JSONObject.fromObject(todayData).getJSONObject("datas").getJSONObject("collector")
					.get("schoolTaskWid").toString();
		} catch (Exception e) {
			System.out.println("获取学校信息失败，程序终止。请检查登录是否失效");
			System.exit(0);
		}
		return todaySchool;
	}

	/**
	 * 获取表单的详细信息。我的学校是23条问卷，我传入的pageSize参数是30
	 * 
	 * @param fields
	 * @return
	 */
	public static String getFields(String[] fields) {
		String param = "{\"pageSize\": 30, \"pageNumber\": 1, \"formWid\": " + fields[3] + ", \"collectorWid\": "
				+ fields[2] + "}";
		String todayData = HttpUtil.sendPost(Data.formFields, param, Data.getHeaders());
		JSONArray todayRows = null;
		try {
			todayRows = JSONObject.fromObject(todayData).getJSONObject("datas").getJSONArray("rows");
		} catch (Exception e) {
			System.out.println("获取表单详细信息失败，程序终止。请检查登录是否失效");
			System.exit(0);
		}
		return filterFields(todayRows);
	}

	/**
	 * 过滤掉表单中未选择的数据
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
	 * 用来将fieldItems中的null数据删掉，只保留isSelected=1的数据
	 * 
	 * @param fieldItems
	 * @return
	 */
	public static String changeJsonArray(String fieldItems) {
		JSONArray array = JSONArray.fromObject(fieldItems);
		ArrayList list = new ArrayList();
		for (Object o : array) {
			JSONObject item = JSONObject.fromObject(o);
			if ("1".equals(item.get("isSelected").toString())) {
				list.add(item);
			}

		}
		return list.toString();
	}

}
