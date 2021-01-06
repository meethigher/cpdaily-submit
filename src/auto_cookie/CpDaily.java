package auto_cookie;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * CpDaily 信息表的一系列操作方法
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021年1月6日
 */
public class CpDaily {

	/**
	 * 获取表单，返回表单信息数组
	 * 
	 * @return
	 */
	public static String[] getForm() {
		String todayData = HttpUtil.sendPost(Data.queryCollector, "{\"pageSize\": 6,\"pageNumber\": 1}",
				Data.getSubHeaders());
		JSONArray todayForm = null;
		try {
			// 表单信息
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
	 * 获取详细表单，返回schoolTaskWid
	 * 
	 * @param fields
	 * @return
	 */
	public static String getSchool(String[] fields) {
		String param = "{\"collectorWid\":\"" + fields[2] + "\"}";
		// 详细表单
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
	 * 获取表单的详细变量。我的学校是23条问卷，我传入的pageSize参数是30
	 * 
	 * @param fields
	 * @return
	 */
	public static String getFields(String[] fields) {
		String param = "{\"pageSize\": 30, \"pageNumber\": 1, \"formWid\": " + fields[3] + ", \"collectorWid\": "
				+ fields[2] + "}";
		// 表单变量
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
	 * 过滤表单中未选择的数据
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
	 * 用来将fieldItems中的null数据删掉，只保留符合关键字的数据
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
	 * 选择带有关键字的选项
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
}
