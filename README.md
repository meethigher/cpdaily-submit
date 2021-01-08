# 使用的话，只需要修改collection.properties即可。

不需要密码，适用于所有学校。

思路看篇文章[今日校园实现自动监测并提交最新表单](https://meethigher.top/blog/2020/cpdaily-automation/)

历史源码

* [1.0](https://github.com/meethigher/cpdaily-submit/tree/1.0)
* [2.0](https://github.com/meethigher/cpdaily-submit/tree/2.0)

使用方法

1. 登录http(s)://你学校的host.campusphere.net/portal/index.html

2. 电脑直接用开发者工具、或者点击地址栏的小锁头选择cookie获取MOD_AUTH_CAS即可
3. 运行Main.java

更新记录

* 2021-01-08：更新加密算法密钥，感谢ZimoLoveShuang大佬

* 2021-01-06：
  1. 更改为配置文件获取参数
  2. 今日校园校园新版不再自动填充，脚本适配为通过晒选关键字选择选项

* 2020-05-08：自动提交问卷