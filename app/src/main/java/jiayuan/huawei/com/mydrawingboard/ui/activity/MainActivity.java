package jiayuan.huawei.com.mydrawingboard.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jiayuan.huawei.com.mydrawingboard.R;
import jiayuan.huawei.com.mydrawingboard.constants.Constants;
import jiayuan.huawei.com.mydrawingboard.paths.MyBasePath;
import jiayuan.huawei.com.mydrawingboard.ui.custom.MySMattingView;
import jiayuan.huawei.com.mydrawingboard.ui.custom.PopwindowDialog;

public class MainActivity extends AppCompatActivity {
    private MySMattingView custom_matting;
    private DrawerLayout mDrawerLayout;
    private Bitmap orgBitmap = null;

    @Bind(R.id.action_redo)
     ImageView action_redo;
    @Bind(R.id.action_undo)
     ImageView action_undo;
    @Bind(R.id.action_earser)
     ImageView action_earser;
    @Bind(R.id.action_pen)
     ImageView action_pen;
    @Bind(R.id.action_matrix)
     ImageView action_matrix;


    private PopwindowDialog dialog;


    private int mattingArray[] = new int[]{
            MyBasePath.MATTING_TYPE_NORMAL,
            MyBasePath.MATTING_TYPE_EARSER,
            MyBasePath.MATTING_TYPE_EMBOSS,
            MyBasePath.MATTING_TYPE_BLUR
    };

    private float paintSizeArray[] = new float[]{Constants.PAINT_SIZE_ONE,
            Constants.PAINT_SIZE_TWO,
            Constants.PAINT_SIZE_THREDD,
            Constants.PAINT_SIZE_FOUR,
            Constants.PAINT_SIZE_FIVE,
            Constants.PAINT_SIZE_SIX};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        findViewByIds();
        findLeftNaviViewByIds();
        requestService();
    }

    private void initData() {
        orgBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.splash);

    }

    private void findViewByIds() {
        custom_matting = (MySMattingView) findViewById(R.id.custom_matting);
        custom_matting.addbitmap(null);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);


        dialog = new PopwindowDialog(this);

    }

    private void findLeftNaviViewByIds() {
//        image_color = (ImageView) findViewById(R.id.image_color);
    }

    private void requestService() {
        custom_matting.setPaintWidth(paintSizeArray[0]);
    }

    @OnClick({R.id.action_redo, R.id.action_undo, R.id.action_matrix, R.id.action_earser, R.id.action_pen})
    public void buttonClicks(View view) {
        switch (view.getId()) //得到被点击的item的itemId
        {
            case R.id.action_redo: //这里的Id就是布局文件中定义的Id，在用R.id.XXX的方法获取出来
//                dialog.showEarserPopup(toolbar);
                break;
            case R.id.action_undo:
//                dialog.showPenPopup(toolbar);
//                custom_matting.undo();
                break;
            case R.id.action_matrix://设置的是画笔模式还是拖动缩放模式
                custom_matting.setToggleDrawingMode();
                break;
            case R.id.action_earser://设置橡皮模式
                int earserType = custom_matting.getPenType();
                if (earserType == MyBasePath.MATTING_TYPE_EARSER) {//选择橡皮宽度大小

                } else {
                    custom_matting.setPaintEarserMode();
                }
                break;
            case R.id.action_pen://设置笔类型
                int penType = custom_matting.getPenType();
                if (penType == MyBasePath.MATTING_TYPE_EARSER) {
                    custom_matting.setPaintPenMode();
                } else {//选择画笔类型

                }
//                custom_matting.setPaintType(MyBasePath.MATTING_TYPE_EARSER);
                break;
        }
    }
}
