# 使用的话，只需要修改collection.properties即可。适合单选问卷！

auto_cookie与auto_login是不同的版本，可以单独使用。auto_cookie是去年5月份搞的，今年有时间，就把auto_login的也整了。

auto_cookie不需要密码，需要抓包到MOD_AUTH_CAS，脚本自动保持登录，适用于所有学校

auto_login只需要配置好collection.properties即可，免抓包。网上千篇一律自动登录的，我看了一下，好像都是NOCLOUD学校的。auto_login支持今日校园CLOUD学校，截止到2021年1月11，我应该是第一个开源这类学校自动登录源码的吧，网上还有人付费卖，啧啧

auto_cookie实现思路看这篇文章[今日校园实现自动监测并提交最新表单](https://meethigher.top/blog/2020/cpdaily-automation/)

auto_login实现思路看这篇文章[今日校园自动登录教程](https://meethigher.top/blog/2021/cpdaily-autologin/)

历史源码（2021年1月11日）

* [1.0](https://github.com/meethigher/cpdaily-submit/tree/1.0)：已失效
* [2.0](https://github.com/meethigher/cpdaily-submit/tree/2.0)：已失效
* [2.1](https://github.com/meethigher/cpdaily-submit/tree/2.1)
* [3.0](https://github.com/meethigher/cpdaily-submit/tree/3.0)

更新记录

* 2021-01-11：添加自动登录版本，监测到登录失效自动登录。也就是auto_login下的代码。

* 2021-01-08：更新加密算法密钥，感谢ZimoLoveShuang大佬

* 2021-01-06：
  1. 更改为配置文件获取参数
  2. 今日校园校园新版不再自动填充，脚本适配为通过晒选关键字选择选项

* 2020-05-08：自动提交问卷