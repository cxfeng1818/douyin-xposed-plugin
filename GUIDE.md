# Xposed插件开发指南

## 环境准备

1. **安装Android Studio**
2. **Root设备 + LSPosed框架**
   - 推荐使用Magisk + LSPosed
3. **抖音APP**（目标应用）

## 构建步骤

```bash
# 1. 用Android Studio打开项目
# 2. 构建APK
./gradlew assembleDebug

# 3. 安装到手机
adb install app/build/outputs/apk/debug/app-debug.apk

# 4. 在LSPosed中激活模块
# 5. 勾选"抖音"作为作用域
# 6. 重启抖音
```

## 工作原理

1. **Hook购买按钮** - 检测"立即购买"点击
2. **Hook订单提交** - 自动确认订单
3. **拦截支付链接** - 捕获alipay://链接
4. **发送到外部** - 通过广播/文件传递

## 优势

- ✅ 无需Appium，直接在APP内运行
- ✅ 更稳定，不受UI变化影响
- ✅ 可以Hook内部逻辑
- ✅ 支持后台自动化

## 下一步

需要逆向抖音找到关键类名和方法名，使用：
- **JadX** - 反编译APK
- **Frida** - 动态调试
