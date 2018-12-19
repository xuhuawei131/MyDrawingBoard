package jiayuan.huawei.com.mydrawingboard.paths;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;

/**
 *
 * 功能描述：
 * 模糊画笔
 * @author WAH-WAY(xuwahwhy@163.com)
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class MyBlurPath extends MyBasePath {
	private Path path;
	public MyBlurPath(float x, float y, float size, int color) {
		path=new Path();
		MaskFilter mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(color);
		paint.setStrokeWidth(size);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setMaskFilter(mBlur);

		path.moveTo(x, y);
		path.lineTo(x, y);
	}

	public void Draw(Canvas canvas) {
		canvas.drawPath(path, paint);
	}
	public void Move(float mx,float my){
		path.lineTo(mx, my);
	}
	@Override
	public Path getCurrentPath() {
		return path;
	}

}