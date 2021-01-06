package auto_cookie;

import net.sf.json.JSONObject;

/**
 * 
 * Form
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021Äê1ÔÂ6ÈÕ
 */
public class Form {
	private String formWid;
	private String address;
	private String collectWid;
	private String schoolTaskWid;
	private boolean uaIsCpadaily = true;
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
		data.put("uaIsCpadaily", uaIsCpadaily);
		data.put("form", form);
		return data.toString();
	}
}
