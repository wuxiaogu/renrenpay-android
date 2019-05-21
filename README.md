AlipayQRHook
------

支付宝可以通过简书上面的方法去做，使用url的方式来生成付款码

列子：alipays://platformapi/startapp?appId=09999988&actionType=toAccount&goBack=NO&amount=1.00&userId=2088112172418889&memo=QQ_123321
-------

说明
------
这个工程是一个基于XPosed或VirtualXPosed的插件项目，用于自动生成支付宝或者微信的付款二维码，可以自定义金额以及备注信息，项目中去除了hook用户登录信息的功能，不会泄露任何支付宝用户信息，只能作为学习目的使用。

警告
------
这是一个已学习为目的的工程，请不要擅自用于商业使用，产生的问题概不负责！！！

注意
------
支付宝10.1.22
目前仅支持微信7.0.3版本

使用
------
1. 手机安装XPosed或VirtualXPosed.（推荐使用VirtualXPosed，手机免root，演示视频使用的VirtualXPosed）
2. 克隆这个项目，在AS中编译生成app-debug.apk。
3. 在VirtualXPosed中安装支付宝或者微信，然后安装app-debug.apk，具体使用方法可以参考VirtualXPosed工程。
4. 在VirtualXPosed插件管理中勾选此插件，然后重启VirtualXPosed。
5. 打开支付宝和微信。
6. 打开服务器登陆管理员用户
7. 打开应用管理，点击测试，设置金额既可以在页面中收到付款码
8. 使用手机支付，成功后手机会有回调显示
