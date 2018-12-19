package jiayuan.huawei.com.mydrawingboard.paths;

import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;

/**
 *
 * 功能描述：
 * 镂空画笔
 * @author WAH-WAY(xuwahwhy@163.com)
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class MyEmbossPath extends MyBasePath {
	private Path path;
	private EmbossMaskFilter mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);

	public MyEmbossPath(float x, float y, float size, int color) {
		path=new Path();

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(color);
		paint.setStrokeWidth(size);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setMaskFilter(mEmboss);

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