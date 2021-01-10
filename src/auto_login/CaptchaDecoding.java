package auto_login;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * 
 * CaptchaDecoding ����ʶ����֤��
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021��1��10��
 */
public class CaptchaDecoding {
	/**
	 * �ƶ�������֤��
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public static File downloadCaptcha(String url, Map<String, String> headers) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// ��������false��������Զ��ض���Ŀ���ַ
			conn.setInstanceFollowRedirects(false);
			if (headers != null) {
				Set<Entry<String, String>> set = headers.entrySet();
				for (Entry<String, String> header : set) {
					conn.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			conn.connect();
			is = conn.getInputStream();
			fos = new FileOutputStream("captcha.jpg");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		} catch (Exception e) {
			System.out.println("��ȡ��֤�����");
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (fos != null)
					fos.close();
			} catch (Exception e2) {

			}
		}
		return new File("captcha.jpg");
	}

	/**
	 * ʶ����֤��
	 * 
	 * @param file
	 * @return
	 */
	public static String parseCaptcha(File file) {
		Tesseract tess = new Tesseract();
		//������������ʱ����
		tess.setDatapath(ClassLoader.getSystemResource("tessdata").getPath().substring(1));
		//jar������ʱ����
//		String tesspath =  System.getProperty("user.dir");
//		tess.setDatapath(tesspath+"/tessdata");
		tess.setLanguage("eng");
		try {
			return tess.doOCR(file).replace(" ", "");
		} catch (TesseractException e) {
			System.out.println("������֤�����");
			e.printStackTrace();
			return null;
		}
	}
}
