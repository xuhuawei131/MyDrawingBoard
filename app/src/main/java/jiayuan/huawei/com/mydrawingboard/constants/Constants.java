package jiayuan.huawei.com.mydrawingboard.constants;

import android.net.Uri;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public class Constants {

    /**字体大小集合**/
    public static final float PAINT_SIZE_ONE=14.0F;
    public static final float PAINT_SIZE_TWO=16.0F;
    public static final float PAINT_SIZE_THREDD=18.0F;
    public static final float PAINT_SIZE_FOUR=20.0F;
    public static final float PAINT_SIZE_FIVE=22.0F;
    public static final float PAINT_SIZE_SIX=24.0F;
    /**误差距离**/
    public static final float TOUCH_TOLERANCE = 4;

    /**屏幕宽高密度**/
    public static int Swidth;
    public static int Sheight;
    public static float Sdesity;
    /**图片质量**/
    public static final int PIC_QUALITY[]=new int[]{50,75,90};

    public static final String TEMP_CROP_FILE="temp.png";
    /**相册或者照相截取图片之后缓存的文件夹路径**/
    public static final String TEMP_CROP_PATH="/NetSearch/CROP/";
    /**相册或者照相截取图片之后缓存的文件绝对路径**/
    public static String TEMP_CROP_ABSFILE;

    /**退出的广播**/
    public static final String ACTION_EXIT="com.netsearch.action.exits";

    public static final String CACHE_FILE="/NetSearch/LogCache/";
    /**服务器返回图片数据的时候**/
    public static final String BITMAP_TEMP_DIR="/cacheTemp/";

    public static Uri tempUri;

    public static final String ACTION_SELECTED_PIC="com.selected.pic.action";



}
