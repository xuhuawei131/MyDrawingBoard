package jiayuan.huawei.com.mydrawingboard.paths;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public abstract class MyBasePath {

    public static final int MATTING_TYPE_NORMAL =0;
    public static final int MATTING_TYPE_EARSER=1;
    public static final int MATTING_TYPE_EMBOSS =2;
    public static final int MATTING_TYPE_BLUR=3;

    protected Paint paint;
    public int size;
    public int color;

    public Paint getCurrentPaint(){
        return paint;
    }

    public abstract  void Draw(Canvas canvas);
    public abstract void Move(float mx, float my);
    public abstract Path getCurrentPath();
}
