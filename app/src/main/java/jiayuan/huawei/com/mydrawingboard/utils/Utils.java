package jiayuan.huawei.com.mydrawingboard.utils;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public class Utils {
    // 触碰两点间距离
    public static  float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt((double)(x * x + y * y));
    }
    // 取手势中心点
    public static void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.x=x / 2;
        point.y=y / 2;
        point.set(x / 2, y / 2);
    }

}
