package jiayuan.huawei.com.mydrawingboard.paths;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 *
 * 功能描述： 橡皮擦功能
 *
 * @author WAH-WAY(xuwahwhy@163.com)
 *
 *         <p>
 *         修改历史：(修改人，修改时间，修改原因/内容)
 *         </p>
 */
public class MyEraserPath extends MyBasePath {
	private Path path;
	public MyEraserPath(float x, float y, float size) {
		path = new Path();

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStrokeWidth(size);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // ���û��ʵĺۼ���͸���ģ��Ӷ����Կ�������ͼƬ
		paint.setDither(true);


		path.moveTo(x, y);
		path.lineTo(x, y);
	}

	public void Draw(Canvas canvas) {
		canvas.drawPath(path, paint);
	}

	public void Move(float mx, float my) {
		path.lineTo(mx, my);
	}

	@Override
	public Path getCurrentPath() {
		return path;
	}
}
