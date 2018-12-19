package jiayuan.huawei.com.mydrawingboard.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import java.util.ArrayList;

import jiayuan.huawei.com.mydrawingboard.R;
import jiayuan.huawei.com.mydrawingboard.constants.Constants;
import jiayuan.huawei.com.mydrawingboard.paths.MyEmbossPath;
import jiayuan.huawei.com.mydrawingboard.paths.MyNormalPath;
import jiayuan.huawei.com.mydrawingboard.paths.MyBasePath;
import jiayuan.huawei.com.mydrawingboard.paths.MyEraserPath;
import jiayuan.huawei.com.mydrawingboard.paths.MyBlurPath;
import jiayuan.huawei.com.mydrawingboard.utils.Utils;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public class MySMattingView extends SurfaceView implements Runnable, Callback {
    private boolean isRunning = false;

    private Context context;
    private Paint mCoverPaint;
    private Bitmap mBitmap;//原图
    private Bitmap mBitmapB;// 背景图
    private Bitmap mBitmapMain;


    private Canvas mCanvasBg;// 背景canvas
    private Canvas mCanvasMain;//画布 所有的笔画都在上面

    private Path mPath;//画线路径

    private float mX, mY;// 临时点坐标
    private int currentType=MyBasePath.MATTING_TYPE_NORMAL;//当前的画笔类型 包括了 橡皮
    private int drawType=currentType;//能画出来笔的类型

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int TouchMode = NONE;//触摸模式

    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float scale = 1.0f;

    private RectF rect;
    private float totalScale = 1.0f;
    private int bWidth;
    private int bHeight;

    private final float[] mMatrixValues = new float[9];
    private static ArrayList<MyBasePath> actionList = null;

    private MyBasePath action;
    //---------------------end------------------------
    private Path mPathCircle = new Path();
    private Matrix matrixMagn = new Matrix();
    private Matrix martixSaveMagn = new Matrix();

    //放大镜的半径
    private static final int RADIUS = Constants.Swidth / 8;
    private float currentPenSize = 14.0f;
    private float currentEarserSize=14.0f;
    private int currentPaintColor=Color.BLUE;
    private int margin;

    /**
     * 放大镜的图片
     **/
    private ShapeDrawable drawable;
    /**
     * 是否抬起
     **/
    private boolean isUp = true;
    /**
     * 放大镜的起始位置
     **/
    private PointF endMagic = new PointF();
    private boolean isPenMode = true;//是画笔模式还是矩阵移动缩放模式
    private  Canvas canvas;

    public MySMattingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MySMattingView(Context context) {
        super(context);
        initView(context);
    }

    public MySMattingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        getHolder().addCallback(this);

        //设置放大镜的参数
        mPathCircle.addRect(0, 0, RADIUS, RADIUS, Path.Direction.CW);

        resetRect();
        //初始化画笔
        actionList = new ArrayList<MyBasePath>();
        //设置覆层的图片半透明的效果
        mCoverPaint = new Paint();
        mCoverPaint.setAlpha(127);
    }

    // 添加bitmap
    public void addbitmap(Bitmap bitmap) {
        recycleBitmap(mBitmap);
        recycleBitmap(mBitmapB);
        recycleBitmap(mBitmapMain);

        start.set(0, 0);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.demo1);
        }

        mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        bWidth = mBitmap.getWidth();
        bHeight = mBitmap.getHeight();

        //前台图片  后台图片
        mBitmapB = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);

        mBitmapMain = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
        mCanvasBg = new Canvas(mBitmapB);

        mCanvasMain = new Canvas(mBitmapMain);
        //放大镜的  shader
        BitmapShader shader = new BitmapShader(mBitmapMain,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        //圆形的drawable
        drawable = new ShapeDrawable(new RectShape());
        drawable.getPaint().setShader(shader);
        drawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);

        martixSaveMagn.reset();
        //为了将图片设置为居中状态未设置的矩阵
        savedMatrix.reset();
        margin = (Constants.Swidth - bWidth) / 2;
        matrix.set(savedMatrix);
        matrix.postTranslate(margin, 0);


        resetRect();
        matrix.mapRect(rect);
        matrix.getValues(mMatrixValues);
        totalScale = mMatrixValues[0];
    }


    /**
     * 功能描述：
     * 重置位置
     *
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2014年11月30日 下午11:00:02</p>
     * <p/>
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    private void resetRect() {
        if (rect == null) {
            rect = new RectF();
        }
        rect.top = 0;
        rect.left = 0;
        rect.right = bWidth;
        rect.bottom = bHeight;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            draw();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isPenMode) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            isUp = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 每次down下去重新new一个Path
                    touch_start((x - rect.left) / totalScale, (y - rect.top) / totalScale);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move((x - rect.left) / totalScale, (y - rect.top) / totalScale);
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    break;
            }
        } else { // 移动或放大缩小
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    TouchMode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = Utils.spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        Utils.midPoint(mid, event);
                        TouchMode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    TouchMode = NONE;
                    resetRect();
                    matrix.mapRect(rect);
                    matrix.getValues(mMatrixValues);
                    totalScale = mMatrixValues[0];
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (TouchMode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    } else if (TouchMode == ZOOM) {
                        float newDist = Utils.spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * 刷新绘制的功能就在这里
     */
    private void draw() {
        try{
        canvas = getHolder().lockCanvas();// getHolder().lockCanvas(new Rect()); 可以进行区域刷新
       if(canvas!=null){
           canvas.drawColor(Color.WHITE);
           // 将前面已经画过得显示出来
           canvas.drawBitmap(mBitmap, matrix, null);
           canvas.drawBitmap(mBitmapB, matrix, null);

           //放大镜图层的背景
           mCanvasMain.drawBitmap(mBitmap, 0, 0, null);
           mCanvasMain.drawBitmap(mBitmapB, 0, 0, null);

           canvas.drawBitmap(mBitmapMain, matrix, null);

           if (isPenMode) {//如果是画笔模式 那么就要绘制放大镜
               if (isUp) {//如果手指已经离开 那么放大镜就要消失
                   clearCanvas(mCanvasMain);
               }
               matrixMagn.set(martixSaveMagn);
               matrixMagn.setTranslate(-endMagic.x + RADIUS, -endMagic.y + RADIUS);
               drawable.getPaint().getShader().setLocalMatrix(matrixMagn);
               drawable.draw(canvas);
           }
       }
        }catch (Exception e){
        }finally {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 设置的是画笔模式还是拖动缩放模式
     */
    public void setToggleDrawingMode() {
        if (isPenMode) {
            isPenMode = false;
        } else {
            isPenMode = true;
        }
    }

    /**
     * 功能描述：
     * 开始触摸点
     *
     * @param x
     * @param y <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2014年11月13日 下午11:57:59</p>
     */
    private void touch_start(float x, float y) {
        action = getPenTypeInstance(x, y);
        mPath = action.getCurrentPath();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }


    /**
     * 功能描述：
     * 移动
     *
     * @param x
     * @param y <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2014年11月13日 下午11:58:16</p>
     */
    private void touch_move(float x, float y) {
        endMagic.x = x;
        endMagic.y = y;
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        // 触摸间隔大于阈值才绘制路径
        if (dx >= Constants.TOUCH_TOLERANCE || dy >= Constants.TOUCH_TOLERANCE) {
            // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
            action.Move(x, y);
            mPath.lineTo(x, y);
            mCanvasBg.drawPath(mPath, action.getCurrentPaint());
        }
    }

    private void touch_up() {
        // 保存一次次画的所有线
        if (actionList != null) {
            actionList.add(action);
        }
        isUp = true;
    }

    /**
     * 功能描述：
     * 清空画板
     *
     * @param canvas <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2014年12月1日 下午12:26:28</p>
     */
    public void clearCanvas(Canvas canvas) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    /**
     * 功能描述：
     * 设置画笔类型
     *
     * @param type <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2014年11月20日 下午2:54:01</p>
     */
    public void setPaintType(int type) {
        this.currentType = type;
        if(currentType!=MyBasePath.MATTING_TYPE_EARSER){
            drawType=currentType;
        }
    }

    public void setPaintPenMode(){
        currentType=drawType;
    }

    /**
     * 设置橡皮模式
     */
    public void setPaintEarserMode(){
        currentType=MyBasePath.MATTING_TYPE_EARSER;
    }

    /**
     * 获取
     * @return
     */
    public int getPenType(){
        return currentType;
    }

    /**
     * 设置画笔的颜色
     * @param color
     */
    public void setPenColor(int color){
        currentPaintColor=color;
    }

    /**
     * 设置画笔的宽度
     * @param size
     */
    public void setPenSize(float size){
        currentPenSize =size;
    }

    /**
     * 设置橡皮的宽度
     * @param size
     */
    public void setEarserSize(float size){
        currentEarserSize =size;
    }

    /**
     * 功能描述：
     * 返回当前指定类型的笔
     *
     * @param x
     * @param y
     * @return <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2014年11月14日 下午9:52:49</p>
     */
    private MyBasePath getPenTypeInstance(float x, float y) {
        if (currentType == MyBasePath.MATTING_TYPE_NORMAL) {
            return new MyNormalPath(x, y, currentPenSize, currentPaintColor);
        } else if(currentType ==MyBasePath.MATTING_TYPE_EARSER){//橡皮模式
            return new MyEraserPath(x, y, currentEarserSize);
        }else if(currentType ==MyBasePath.MATTING_TYPE_BLUR){
            return new MyBlurPath(x, y, currentPenSize,currentPaintColor);
        }else if(currentType ==MyBasePath.MATTING_TYPE_EMBOSS){
            return new MyEmbossPath(x, y, currentPenSize,currentPaintColor);
        }
        return null;
    }

    public void undo() {//从新创建一个bitmap 然后将这些线路画在上面
        if (actionList != null && actionList.size() > 0) {
            resetBitmap();
            actionList.remove(actionList.size() - 1);
            for (MyBasePath action : actionList) {
                if (action instanceof MyNormalPath) {
                    action.Draw(mCanvasBg);
                } else if (action instanceof MyEraserPath) {
                    action.Draw(mCanvasBg);
                }
                action.Draw(mCanvasMain);
            }
        }
    }

    private void resetBitmap() {
        clearCanvas(mCanvasMain);
        bWidth = mBitmap.getWidth();
        bHeight = mBitmap.getHeight();
        // 后台图片
        mBitmapB = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888);
        mCanvasBg = new Canvas(mBitmapB);
    }


    public void setPaintWidth(float size) {
        currentPenSize = size;
    }

    /**
     * 功能描述：
     * 回收图片
     *
     * @param bitmap <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     * @author WAH-WAY(xuwahwhy@163.com)
     * <p>创建日期 ：2014年12月4日 下午4:35:17</p>
     */
    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }


}
