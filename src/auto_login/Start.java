package auto_login;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * 
 * Start 整个脚本执行入口
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021年1月6日
 */
public class Start {
	private static int sh;
	private static int eh;

	public static void checkLogin() {
		Data.modAuthCas = Login.getAccess();
		if (Login.isOff()) {
			System.out.println("登录失败次数过多，脚本已经退出！请重试或者联系meethigher@qq.com!");
			System.exit(0);
		}
	}

	public static String submit() {
		if (Login.isOff()) {
			checkLogin();
		}
		String[] form = Cpdaily.getForm();
		if ("yes".equals(form[0])) {
			if ("1".equals(form[1])) {
				System.out.println("今日已经提交过了！");
				return "submited";
			} else {
				String schoolTaskWid = Cpdaily.getSchool(form);
				String message = Cpdaily.submit(form, schoolTaskWid, Data.address);
				if ("SUCCESS".equals(message)) {
					System.out.println("今日表单已成功提交！");
					System.out.println(SendMail.send(new String[] { "问卷提交成功通知", "时间：" + new Date().toLocaleString()
							+ "\n" + "学号：" + Data.id + "\n地址：" + Data.address + "\n提交关键字：" + Data.key }));
					return "success";
				} else {
					System.out.println("今日表单提交失败，失败报告->" + message);
					System.out.println(SendMail.send(
							new String[] { "问卷提交失败通知", "时间：" + new Date().toLocaleString() + "\n" + "失败：" + message }));
					return "error";
				}
			}
		} else {
			System.out.println("尚未发表最新表单！");
			return "noform";
		}
	}

	/*
	 * 减去1秒是去掉程序运行的时间
	 */
	public static int getDelay(String result, Calendar c) {
		int delay;
		if ("success".equals(result) || "error".equals(result)) {
			delay = 1000 * 60 * 60 * 24 - 1000 * 1;
		} else if ("noform".equals(result)) {
			delay = 1000 * 60 * 30;
		} else {
			delay = (24 - c.get(Calendar.HOUR_OF_DAY) + sh) * 60 * 60 * 1000;
		}
		System.out.println("程序休眠" + delay / 1000 / 60 + "分钟后再次执行..");
		return delay;
	}

	public static void main(String[] args) {
		System.out.println("检测登录状态...");
		System.out.println("作者：https://github.com/meethigher");
		checkLogin();
		System.out.println("读取配置文件..");
		System.out.println("学号：" + Data.id);
		System.out.println("地址：" + Data.address);
		System.out.println("邮箱：" + Data.toMail);
		System.out.println("Auth：" + Data.modAuthCas);
		System.out.println("当前时间：" + new Date().toLocaleString());
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入开始监测的小时：");
		sh = scanner.nextInt();
		System.out.print("请输入结束监测的小时：");
		eh = scanner.nextInt();
		scanner.close();
		System.out.println("2021-01-10自动登录版本正在运行...");
		// 开启保持会话线程
		new Thread(() -> {
			while (true) {
				HttpUtil.sendGet(Data.keepingUrl, Data.getHeaders());
				try {
					Thread.sleep(1000 * 60 * 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 开启打卡线程
		new Thread(() -> {
			Calendar c;
			String result = null;
			boolean flag = false;
			while (true) {
				c = Calendar.getInstance();
				int currentHour = c.get(Calendar.HOUR_OF_DAY);
				if (sh <= currentHour && currentHour <= eh) {
					flag = true;
				} else {
					flag = false;
				}
				if (flag)
					result = submit();
				try {
					Thread.sleep(getDelay(result, c));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
