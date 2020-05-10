站在巨人的肩膀上，我所写的东西，也会开源分享出来！

授人以鱼不如授人以渔，我把思路分享出来。

截止到5月11日，已经完美运行三天了。各种小bug也已修复。

源码放在这里[今日校园实现自动检测并提交最新表单](https://github.com/meethigher/cpdaily-submit)，思路的话也可以直接访问我[博客](https://meethigher.top/blog/2020/cpdaily-automation/)

# 一、抓包（重点）

## 1.1 如何抓包

Fiddler4电脑端与手机端抓包的教程，我不多bb了。点这个[链接](https://blog.csdn.net/c406495762/article/details/76850843)里面有具体步骤，这个大佬是专门学习机器学习的，他的博客放到[这里](https://cuijiahua.com/)。

一开始，我是用的Fiddler4来进行抓包的，但是涉及到ssl-pinning的问题。导致抓包失败，也就是抓取过程中，一堆灰色链接。同时，手机上的网路也会被断开。

之后，我就开始考虑，要不用手机抓包，就下载了[HttpCannary](https://github.com/MegatronKing/HttpCanary)，HttpCanary有普通版和高级版之分，建议下载高级版，同样会遇到ssl-pinning的问题，具体解决方法请移步到[系统证书的安装，解决APP抓不到包](https://www.bilibili.com/video/BV1Qe411s7q9?from=search&seid=10930251094898400074)，下载软件点[这里](https://lanzous.com/b0c2a09rg)

## 1.2 我的解决方法

此节是废话，请略过。

我一开始用幸运破解器，很幸运的是，虽然显示破解授权失败，但打开的时候，发现能用了。

下一步，就是安装系统证书了，我就把手机root了，我的手机是小米的，直接root就行。

但是，还是在安装系统证书的过程中出了问题，不能往系统里面移动证书，原因是没有权限。

后来百度了一下，发现，MIUI的root权限，从Android7之后，就没有完整的root权限了。除非刷第三方系统。

但是我又舍不得自己的MIUI12。

接下来，就准备用模拟器了，我下载的是[逍遥安卓模拟器](http://www.xyaz.cn/)。安卓的是4.4版本系统，刷入了[xposed框架](http://xposed.appkg.com/)，安装了[justtrustme模块](https://github.com/Fuzion24/JustTrustMe)。

然后，打开Fiddler抓包，Yes！

有了Cpdaily-Extension跟MOD_AUTH_CAS这两条数据，就能模拟提交了。

抓包问题就此解决。耗费了我一整天的时间

# 二、Java模拟get跟post请求

这个算是套模板吧，都是这个套路，直接上代码。不要拿来就用，还是根据实际情况进行修改的。

```java
/**
 * HttpUtil类
 * @author kit chen
 * @description 用来模拟发送post请求
 */
public class HttpUtil {
	public static String sendPost(String url, String param,Map<String,String> headers) {
		BufferedWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			Set<Entry<String, String>> set=headers.entrySet();
			for(Entry<String,String> header:set) {
				conn.setRequestProperty(header.getKey(), header.getValue());
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			out.write(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
```

# 三、模拟请求中的问题

通过抓包获取了请求头跟请求参数，请求参数还好说。

在抓取请求头的时候，遇到了点问题。

通过测试发现，如果想要成功提交内容，需要Cpdaily-Extension跟MOD_AUTH_CAS。

如果设备不去主动退出的话，Cpdaily-Extension是会**一直存在有效**的。类似的像QQ也是这样的。我在抓取Cpdaily-Extension的过程中，尝试访问各种页面，但是都没法获取到这个的值。哪怕是在登录的时候，也没有这个参数。只有在提交的时候，才能抓取到。

像MOD_AUTH_CAS这个的值，类似于session，是有有效期的。我目前还没有测试一直让这个东西保持有效。这个值，大概6个小时左右，就会失效了，需要重新登录网页获取。

问了大佬，好像是涉及到了[cas单点登录](https://blog.csdn.net/anumbrella/article/details/80821486)的知识，这块我也不懂。学！

# 四、监测表单并返回表单号

今日校园有个获取今日最新表单的接口，如果没有的话，里面某个数据会是个空数组。

接口`/wec-counselor-collector-apps/stu/collector/queryCollectorProcessingList` 

post的请求参数`{"pageSize": 6,"pageNumber": 1}`

这个pageSize是指返回几条数据，响应请求中，我记得返回的json字符串，最多就只有6个参数，所以这个传个6就行了。大于等于6

返回的内容

```json
{
	"code": "0",
	"message": "SUCCESS",
	"datas": {
		"totalSize": 0,
		"pageSize": 6,
		"pageNumber": 1,
		"rows": [
            "wid":"xx",
            "formWid":"xx",
            "isHandled":"0"
            ...
        ]
	}
}
```

我们要的就是这个wid、formWid跟isHandled。isHandled表示是否提交，非0为提交。

监测到有rows有数据，并且isHandled未提交的时候，我们就可以进行模拟提交了。

# 五、获取学校表单号

这个还有一个坑就是，每天的学校的表单号schoolTaskWid不是固定的，由此，我们就需要来模拟请求来获取schoolTaskWid

接口`/wec-counselor-collector-apps/stu/collector/detailCollector` 

post请求参数 `{"collectorWid": 传进来第三步获取的collectWid}` 

> 注意：
>
> 这sb接口开发者，一开始定义的是collectWid，结果后面又成了collectorWid，一开始在这边把我给坑了，需要注意。
>
> 这个接口获取的是collectorWid，第三步获取的是collectWid，其实他俩是一个东西。

大致的返回内容

```json
{
	"code": "0",
	"message": "SUCCESS",
	"datas": {
		"collector": {
			"wid": "8888",
			"formWid": "164",
			"priority": "5",
			"endTime": "8888-88-88 88:00:00",
			"currentTime": "8888-88-88 88:00:00",
			"schoolTaskWid": "8888",
			"isConfirmed": 1,
			"senderUserName": "牛逼学院(牛逼老师)",
			"createTime": "8888-88-88 88:00:00",
			"attachmentUrls": null,
			"attachmentNames": null,
			"attachmentSizes": null,
			"isUserSubmit": 1,
			"fetchStuLocation": true,
			"address": "xx省xx市xx县" 
            //这个根据地图上面的为准，大致就是省市县
		},
		"form": {
			"wid": "164",
			"formTitle": "8月8日学生身体健康状况调查",
			"formContent": "https://wecres.cpdaily.com/counselor/1076158768111098/content/d1c0daf5604af56fbccfadaf28cdbd82.html",
			"backReason": null,
			"isBack": 0,
			"attachments": []
		}
	}
}
```

这里我们需要获取schoolTaskWid。

# 六、获取详细表单

今日校园还有一条验证就是每次请求的表的id，以及表选项的id都是变化的。

所有，我们还需要抓取表的详细表单。

接口`/wec-counselor-collector-apps/stu/collector/getFormFields`

参数`{"pageSize":30,"pageNumber":1,"formWid":formWid,"collectorWid":collectorWid}`

返回的内容，依你们老师的设定为准，不放结果了。

# 七、模拟Post请求提交

## 7.1 思路

通过上面，我们获取到了formWid，collectWid，schoolTaskWid，address，form。

接下来，我们构造出post的json字符串请求体。

发送！

成功！

## 7.2 扩展-邮件通知

如果提交成功，或者提交失败。都会发通知邮件给我。

但是，试了几次，经常报错`554 DT:SPM`，也就是被当做垃圾邮件驳回了。

尝试了好几个邮箱，像139,163,qq邮箱，都会有这个问题，网上说设置端口啊，不要25，要465...开启ssl...等等

如果涉及到敏感词，什么签到、自动，仍然没有一个奏效的，都没当成垃圾邮件处理。

刚好看到一个大佬写的博客，建议用腾讯企业邮箱，试了一下，果然ok了。

[记录一次Could not connect to SMTP host: smtp.163.com, port: 25的解决办法](https://blog.csdn.net/qq_38410730/article/details/86538298?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1)

> 腾讯云或者阿里云，是默认禁用25端口的

我附上发邮件的代码

```java
public class SendMail {
	public static String send(String[] mail) {
		Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.exmail.qq.com");// 主机名
        properties.put("mail.smtp.port", "587");// 端口号
        properties.put("mail.smtp.auth", "true");//设置smtp是否需要认证
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "false");// 设置是否显示debug信息 true 会在控制台显示相关信息
        try{
            Session session = Session.getInstance(properties);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("xxxxxxxxx@xxxxx.onexmail.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("meethigher@qq.com"));
            message.setSubject(mail[0]);
            message.setText(mail[1]);
            message.setSentDate(new Date());
            Transport transport = session.getTransport();
            transport.connect("xxxxxxxxx@xxxxxx.onexmail.com", "xxxxxx");//登录发信账号
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return "邮件发送成功";
        }catch (Exception e){
            return "邮件发送失败";
        }
	}
}
```

## 7.3 运行结果

去我博客看吧

# 八、总结

## 8.1 感谢大佬

[python版今日校园自动签到、填表](https://www.cnblogs.com/FSHOU/p/12425456.html)

[python版今日校园自动填报脚本](https://github.com/Itswag/cpdaily_submit)

[java定时刷新网页](https://www.jianshu.com/p/c6059b265dd0)

[手机抓包教程第四节——系统证书的安装，解决APP抓不到包](https://www.bilibili.com/video/BV1Qe411s7q9?from=search&seid=6692535577953796826)

[记录一次Could not connect to SMTP host: smtp.163.com, port: 25的解决办法](https://blog.csdn.net/qq_38410730/article/details/86538298?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1)

[java抓取网页指定元素/内容](https://blog.csdn.net/yfx000/article/details/56831023)

[模拟登陆系列](https://github.com/CharlesPikachu/DecryptLogin)

[QQ空间模拟登录](https://mp.weixin.qq.com/s/Awnj0x_E7XJtbOW_7OBwbA)

还是那句话，站在巨人的肩膀上

## 8.2 个人收获

连着两天，早上5点就起。

因为这个是学校的老师发的表单，我没有老师权限，没法模拟。

所以，只能，每天早起，在表单未提交之前，进行测试。这也是最难受的一点。

今天专业课考试，我当时写代码写得正在兴头上，结果作业没给交上去。估计这学期也就考个60分吧。无所谓了，反正我也不是学霸，计较个锤子。而且，我这学期，也都没咋听课，天天在家里就知道玩。每次都下定决心好好学习，结果，嘿嘿...

总得来说，抓包还是很关键的。

这个博客算是整理整个的思路，如果思路看明白了，那么，实现这样的功能就不难了。

我看网上也有不少的类似程序，但是大多数是Python开发的，确实，Python在处理数据的时候，很舒服，给我的感觉，就是跟JavaScript一样，就是舒服。PHP如果实现这个功能的话，也会比较轻松。

由于我现在正在学java，还没学透，正好可以拿这个练练手。毕竟，java就像是我的初恋，虽然她身材很臃肿，但我依然爱她啊！

处理过程中，真的感觉**人生苦短，多用python**。

像这样的功能，好多编程语言都能实现，Java不是不能，只是太繁琐了。

如果我再次写这个的话，首选Python，其次Nodejs。

在分层方面，还是觉得自己做地不太好，像学过的继承多态，我在处理的过程中，就是一直在硬写、闷头写。自己的代码也能看得出毛病来，但是想优化，却又不知从何改起。一方面，是对学过的东西，没有彻底理解；另一方面，也是自己的项目经验太少了。

一定要花时间，弥补这方面的不足！