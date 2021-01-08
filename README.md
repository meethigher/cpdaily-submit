# 使用的话，只需要修改collection.properties即可。

不需要密码，需要抓包到MOD_AUTH_CAS，脚本自动保持登录，适用于所有学校

思路看篇文章[今日校园实现自动监测并提交最新表单](https://meethigher.top/blog/2020/cpdaily-automation/)

历史源码（2021年1月9日）

* [1.0](https://github.com/meethigher/cpdaily-submit/tree/1.0)：已失效
* [2.0](https://github.com/meethigher/cpdaily-submit/tree/2.0)：已失效
* [2.1](https://github.com/meethigher/cpdaily-submit/tree/2.1)
* [最新版](https://github.com/meethigher/cpdaily-submit)

使用方法

1. [查看所有学校信息](https://static.campushoy.com/apicache/tenantListSort就是)，找到你学校的id，如果是ccut这种的，直接第3步，如果是一串字母数字，跳到第2步
2. https://mobile.campushoy.com/v6/config/guest/tenant/info?ids=你学校的id，找到idsUrl。比方说，有https://ccut.campusphere.net/iap，则ccut即你的id，跳到第三步。
3. 登录http(s)://你学校.campusphere.net/portal/index.html，比如[长春工业大学](https://ccut.campusphere.net/portal/index.html)
4. 电脑直接用开发者工具、或者点击地址栏的小锁头选择cookie获取MOD_AUTH_CAS即可。
5. 手机使用HttpCanary抓取浏览器记录即可。
6. 运行Main.java

更新记录

* 2021-01-08：更新加密算法密钥，感谢ZimoLoveShuang大佬

* 2021-01-06：
  1. 更改为配置文件获取参数
  2. 今日校园校园新版不再自动填充，脚本适配为通过晒选关键字选择选项

* 2020-05-08：自动提交问卷