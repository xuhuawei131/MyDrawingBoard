package jiayuan.huawei.com.mydrawingboard;

import android.app.Application;

import jiayuan.huawei.com.mydrawingboard.constants.Constants;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        this.instance=this;
        Constants.Swidth=this.getResources().getDisplayMetrics().widthPixels;
        Constants.Sheight=this.getResources().getDisplayMetrics().heightPixels;
        Constants.Sdesity=this.getResources().getDisplayMetrics().density;
    }
    public static MyApplication getAppInstance(){
        return instance;
    }
}
