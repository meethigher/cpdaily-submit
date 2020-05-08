package cpdailyAlpha;

public class Main {
	public static void main(String[] args) throws Exception {
		while (true) {
			// 返回的数组中，下标0表示是否有数据，下标1表示isHandled，下标2表示collectWid，下标3表示formWid
			String[] fields = CpDaily.getForm();
			/*
			 * 用来决定接下来运行间隔的时间 
			 * 如果是true，提交今日表单，24小时后再次监测新表单 
			 * 如果是false，继续监测是否发布最新表单，间隔2小时
			 */
			String result;//存储运行的结果，邮件通知
			boolean flag;
			if (fields[0].equals("yes")) {
				flag = true;
				if (fields[1].equals("1")) {
					System.out.println("今日表单已经提交过了！");
				} else {
					System.out.println("正在准备提交今日表单...");
					String schoolTaskWid = CpDaily.getSchool(fields);
					String message = CpDaily.submit(fields, schoolTaskWid, Data.address);
					if (message.equals("SUCCESS")) {
						result="今日表单已成功提交！";
						System.out.println(result);
					} else {
						result="今日表单提交失败，失败报告->" + message;
						System.out.println(result);
					}
					SendMail.send(new String[] {"今日校园提交通知",result});
				}
			} else {
				flag = false;
				System.out.println("尚未发布最新表单！");
			}
			if (flag) {
				System.out.println("24小时后再次监测是否有新表单！");
				Thread.sleep(1000 * 60 * 60 * 24);
			} else {
				System.out.println("2小时后监测今日是否发布表单！");
				Thread.sleep(1000 * 60 * 60 * 2);
			}
		}
	}
}
