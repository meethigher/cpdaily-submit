

import java.util.Calendar;
import java.util.Scanner;

public class Main {
	/**
	 * 返回结果 
	 * submited 今日已提交 
	 * error 今日提交失败 
	 * success 今日提交成功 
	 * noform 未发现最新表单
	 * 
	 * @return
	 */
	public static String start() {
		String[] fields = CpDaily.getForm();
		String result;// 存储运行的结果，邮件通知
		boolean flag;
		// 判断是否有新表单，yes为有，no为没有
		if (fields[0].equals("yes")) {
			flag = true;
			// 判断是否提交，1为已提交，0为未提交
			if (fields[1].equals("1")) {
				System.out.println("今日表单已经提交过了！");
				result = "submited";
			} else {
				System.out.println("正在准备提交今日表单...");
				String schoolTaskWid = CpDaily.getSchool(fields);
				String message = CpDaily.submit(fields, schoolTaskWid, Data.address);
				// 判断是否提交成功
				if (message.equals("SUCCESS")) {
					System.out.println("今日表单已成功提交！");
					result = "success";
				} else {
					System.out.println("今日表单提交失败，失败报告->" + message);
					result = "error";
				}
			}
		} else {
			System.out.println("尚未发布最新表单！");
			result = "noform";
		}
		return result;

	}

	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(System.in);
		System.out.print("请输入开始监测的小时：");
		int hour = Integer.parseInt(input.nextLine());// 小时
		System.out.print("请输入结束监测的小时：");
		int endHour=Integer.parseInt(input.nextLine());
		input.close();
		Calendar c;
		System.out.println("正在运行...");
		boolean flag;// 若监测并成功提交，则true;若未监测到发布表单，则false
		while (true) {
			c = Calendar.getInstance();
			String result = null;
			int sleepHour=0;
			if (c.get(Calendar.HOUR_OF_DAY) >= hour&&c.get(Calendar.HOUR_OF_DAY)<endHour) {
				result = start();
			}else if(c.get(Calendar.HOUR_OF_DAY)>=endHour) {
				result="submited";
			}
			/*
			 * 若success或者error或者submited，则等到第二天的时间再执行
			 * 若noform，则每隔1小时查询是否发布新表单
			 */
			if ("success".equals(result)) {
				System.out.print(SendMail.send(new String[] { "今日校园问卷提交通知", "今日表单提交结果：成功！" }));
				sleepHour=24-c.get(Calendar.HOUR_OF_DAY)+hour;
				System.out.println("程序休眠"+sleepHour+"小时后运行");
			} else if ("error".equals(result)) {
				System.out.print(SendMail.send(new String[] { "今日校园问卷提交通知", "今日表单提交结果：失败。请手动提交" }));
				sleepHour=24-c.get(Calendar.HOUR_OF_DAY)+hour;
				System.out.println("程序休眠"+sleepHour+"小时后运行");
			} else if ("noform".equals(result)) {
				sleepHour=1;
				System.out.println("程序休眠"+sleepHour+"小时后运行");
			} else if ("submited".equals(result)) {
				sleepHour=24-c.get(Calendar.HOUR_OF_DAY)+hour;
				System.out.println("程序休眠"+sleepHour+"小时后运行");
			}
			Thread.sleep(1000*60*60*sleepHour);
			
		}

	}
}
