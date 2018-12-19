package jiayuan.huawei.com.mydrawingboard.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import jiayuan.huawei.com.mydrawingboard.MyApplication;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public class SharedPreUtil {

    public static int getSharedPaintSize(){
        SharedPreferences sp= MyApplication.getAppInstance().getSharedPreferences("drawingpad",Activity.MODE_PRIVATE);
        return sp.getInt("paintSize",15);

    }
    public static void setSharedPaintSize(int size){
        SharedPreferences sp= MyApplication.getAppInstance().getSharedPreferences("drawingpad",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("paintSize",size);

    }
    public static int getSharePaintColor(){
        SharedPreferences sp= MyApplication.getAppInstance().getSharedPreferences("drawingpad",Activity.MODE_PRIVATE);
        return sp.getInt("paintColor",15);
    }
    public static void setSharedPaintColor(int color ){
        SharedPreferences sp= MyApplication.getAppInstance().getSharedPreferences("drawingpad",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("paintColor",color);
    }

}
