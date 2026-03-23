# 抖音Xposed插件 - 自动下单

## 方案优势
- 直接运行在抖音APP内部
- 无需Appium，更稳定
- 可以Hook内部方法，绕过UI限制
- 支持后台运行

## 技术栈
- LSPosed/Xposed框架
- Java/Kotlin
- Frida（可选，动态调试）

## 功能
1. Hook商品页，自动点击购买
2. Hook订单页，自动提交
3. 拦截支付链接
4. 通过Intent发送到外部APP
