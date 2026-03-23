package com.douyin.autopay;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.content.Intent;
import android.net.Uri;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainHook implements IXposedHookLoadPackage {
    
    private static final String SERVER_URL = "http://43.162.112.203:5000/payment";
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.ss.android.ugc.aweme")) {
            return;
        }
        
        XposedBridge.log("=== 抖音支付拦截插件已加载 ===");
        
        // Hook Intent.setData - 拦截所有URI跳转
        XposedHelpers.findAndHookMethod(
            Intent.class,
            "setData",
            Uri.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Uri uri = (Uri) param.args[0];
                    if (uri != null) {
                        String uriStr = uri.toString();
                        
                        // 拦截支付宝链接
                        if (uriStr.startsWith("alipays://") || uriStr.startsWith("alipay://")) {
                            XposedBridge.log(">>> 拦截到支付宝链接: " + uriStr);
                            sendToServer(uriStr);
                        }
                        
                        // 拦截微信支付链接
                        if (uriStr.startsWith("weixin://") || uriStr.contains("wxpay")) {
                            XposedBridge.log(">>> 拦截到微信支付链接: " + uriStr);
                            sendToServer(uriStr);
                        }
                    }
                }
            }
        );
    }
    
    private void sendToServer(final String paymentUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(SERVER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                
                String json = "{\"url\":\"" + paymentUrl.replace("\"", "\\\"") + "\"}";
                
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();
                
                int code = conn.getResponseCode();
                XposedBridge.log(">>> 发送到服务器，状态码: " + code);
                
            } catch (Exception e) {
                XposedBridge.log(">>> 发送失败: " + e.getMessage());
            }
        }).start();
    }
}
