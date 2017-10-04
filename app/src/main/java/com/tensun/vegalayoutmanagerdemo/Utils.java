package com.tensun.vegalayoutmanagerdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Utils {

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    /******************************以下代码段是为处理顶部状态栏的************************************/

    /**
     * 沉浸式状态栏
     * 區分出3種情況: 未滿Android4.4的, 未滿Android5.0的, 以及Android5.0以上的
     */
    public static boolean immerseStatusBar(Activity activity) {
        boolean success = false;                                                                    // 針對 未滿Android4.4的處理方式

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {                               // 針對 Android5.0以上的處理方式
            success = true;
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);                            // 修改狀態欄的顏色為透明, setStatusBarColor(): Android5.0 API21以上 才支援此方法
            activity.getWindow()                                                                    // 取得該activity 的窗口控制
                    .getDecorView()                                                                 // 从window中获取到decorView, decorView是window中的最顶层view
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);             // 全屏的概念, 隐藏导航栏, 状态栏浮在布局上
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {                         // 針對 未滿Android5.0的處理方式
            success = true;
            activity.getWindow()
                    .setFlags(
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    );
        }
        return success;
    }

    public static boolean setDarkMode(Activity activity) {
        return setDarkMode(activity, true);
    }

    /**
     * darkMode设置
     *
     * @return 是否成功
     */
    public static boolean setDarkMode(Activity activity, boolean darkMode) {
        String brand = Build.BRAND;                                                                // 获取设备品牌

        boolean success = false;
        if (brand.contains("Xiaomi")) {                                                             // 如果設備品牌是小米
            success = setXiaomiDarkMode(activity, darkMode);
        } else if (brand.contains("Meizu")) {                                                       // 如果設備品牌是魅族
            success = setMeizuDarkMode(activity, darkMode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {                               // 如果Android6.0以上時
            final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    darkMode ? (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            );
            success = true;
        }

        return success;
    }

    private static boolean setXiaomiDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean setMeizuDarkMode(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }
}
