package com.douyin.autopay;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.view.View;
import android.widget.Button;

public class MainHook implements IXposedHookLoadPackage {
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.ss.android.ugc.aweme")) {
            return;
        }
        
        XposedBridge.log("抖音自动下单插件已加载");
        
        // Hook商品详情页
        hookProductPage(lpparam);
        
        // Hook订单页
        hookOrderPage(lpparam);
        
        // Hook支付跳转
        hookPayment(lpparam);
    }
    
    private void hookProductPage(XC_LoadPackage.LoadPackageParam lpparam) {
        // Hook购买按钮点击
        XposedHelpers.findAndHookMethod(
            "android.view.View",
            lpparam.classLoader,
            "performClick",
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View) param.thisObject;
                    if (view instanceof Button) {
                        String text = ((Button) view).getText().toString();
                        if (text.contains("立即购买") || text.contains("购买")) {
                            XposedBridge.log("检测到购买按钮点击");
                            // 自动触发下单流程
                        }
                    }
                }
            }
        );
    }
    
    private void hookOrderPage(XC_LoadPackage.LoadPackageParam lpparam) {
        // Hook提交订单按钮
        XposedBridge.log("Hook订单提交");
    }
    
    private void hookPayment(XC_LoadPackage.LoadPackageParam lpparam) {
        // Hook支付宝跳转，拦截链接
        XposedHelpers.findAndHookMethod(
            "android.content.Intent",
            lpparam.classLoader,
            "setData",
            android.net.Uri.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    android.net.Uri uri = (android.net.Uri) param.args[0];
                    if (uri != null && uri.toString().startsWith("alipay")) {
                        XposedBridge.log("拦截支付链接: " + uri.toString());
                        // 发送到外部
                        sendToExternal(uri.toString());
                    }
                }
            }
        );
    }
    
    private void sendToExternal(String paymentUrl) {
        // 通过广播发送到外部APP
        XposedBridge.log("支付链接: " + paymentUrl);
    }
}

