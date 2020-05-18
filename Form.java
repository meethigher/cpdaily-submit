


import net.sf.json.JSONObject;

/**
 * 
 * Form类
 * @author kit chen
 * @description 通过这个类，返回所要提交的json字符串形式的表单数据
 */
public class Form {
	private String formWid;
	private String address;
	private String collectWid;
	private String schoolTaskWid;
	private String form;

	public Form() {
		super();
	}
	
	public Form(String formWid, String address, String collectWid, String schoolTaskWid, String form) {
		super();
		this.formWid = formWid;
		this.address = address;
		this.collectWid = collectWid;
		this.schoolTaskWid = schoolTaskWid;
		this.form = form;
	}

	@Override
	public String toString() {
		JSONObject data = new JSONObject();
		data.put("formWid", formWid);
		data.put("address", address);
		data.put("collectWid", collectWid);
		data.put("schoolTaskWid", schoolTaskWid);
		data.put("form", form);
		return data.toString();
	}
}
