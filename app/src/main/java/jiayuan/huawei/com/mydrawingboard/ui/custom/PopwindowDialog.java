package jiayuan.huawei.com.mydrawingboard.ui.custom;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import jiayuan.huawei.com.mydrawingboard.R;

/**
 * $desc$
 *
 * @author xuhuawei
 * @time $date$ $time$
 */
public class PopwindowDialog {

    public interface OnPopWindowDialogListener{
        public void onPopWindowEarserSzieSelected(int size);
        public void onPopWindowPenSzieSelected(int size,int color);
    }

    private View popupLayout;
    private View popupEraserLayout;
    private ColorPicker mColorPicker;
    private int oldColor;
    private ImageView strokeImageView, eraserImageView;
    private int seekBarStrokeProgress, seekBarEraserProgress;
    private int size;
    private Context context;

    private OnPopWindowDialogListener listener;

    public PopwindowDialog(Context context){
        this.context=context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(ActionBarActivity
                .LAYOUT_INFLATER_SERVICE);
        popupLayout = inflater.inflate(R.layout.popup_sketch_stroke, null);

        LayoutInflater inflaterEraser = (LayoutInflater) context.getSystemService(ActionBarActivity
                .LAYOUT_INFLATER_SERVICE);
        popupEraserLayout = inflaterEraser.inflate(R.layout.popup_sketch_eraser, null);


        strokeImageView = (ImageView) popupLayout.findViewById(R.id.stroke_circle);
        final Drawable circleDrawable = context.getResources().getDrawable(R.drawable.circle);
        size = circleDrawable.getIntrinsicWidth();

        eraserImageView = (ImageView) popupEraserLayout.findViewById(R.id.earser_circle);

        setSeekbarProgress(35, false);
        setSeekbarProgress(50, true);

        // Stroke color picker initialization and event managing
        mColorPicker = (ColorPicker) popupLayout.findViewById(R.id.stroke_color_picker);
        mColorPicker.addSVBar((SVBar) popupLayout.findViewById(R.id.svbar));
        mColorPicker.addOpacityBar((OpacityBar) popupLayout.findViewById(R.id.opacitybar));
        mColorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
//                mSketchView.setStrokeColor(color);
            }
        });
    }

    public void setOnPopWindowDialogListener(OnPopWindowDialogListener listener){
            this.listener=listener;
    }


    private void setSeekbarProgress(int progress, boolean isEarser) {
        int calcProgress = progress > 1 ? progress : 1;

        int newSize = Math.round((size / 100f) * calcProgress);
        int offset = Math.round((size - newSize) / 2);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(newSize, newSize);
        lp.setMargins(offset, offset, offset, offset);
        if (!isEarser) {
            strokeImageView.setLayoutParams(lp);
            seekBarStrokeProgress = progress;
        } else {
            eraserImageView.setLayoutParams(lp);
            seekBarEraserProgress = progress;
        }

//        mSketchView.setSize(newSize, eraserOrStroke);
    }


    public void showEarserPopup(View anchor){
            PopupWindow popup = new PopupWindow(context);
            popup.setContentView( popupEraserLayout);
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setFocusable(true);
            popup.showAsDropDown(anchor);
            SeekBar mSeekBar = (SeekBar) popupEraserLayout.findViewById(R.id.stroke_seekbar);
//            isErasing ? popupEraserLayout .findViewById(R.id.stroke_seekbar) :

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    setSeekbarProgress(progress, true);
                }
            });
            mSeekBar.setProgress(seekBarEraserProgress);
        }

        public void showPenPopup(View anchor) {
          final int oldColor = mColorPicker.getColor();


            PopupWindow popup = new PopupWindow(context);
            popup.setContentView( popupLayout);
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setFocusable(true);
            popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (mColorPicker.getColor() != oldColor)
                        mColorPicker.setOldCenterColor(oldColor);
                }
            });
            popup.showAsDropDown(anchor);
            SeekBar mSeekBar = (SeekBar) popupLayout.findViewById(R.id.stroke_seekbar);
//            isErasing ? popupEraserLayout .findViewById(R.id.stroke_seekbar) :

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    setSeekbarProgress(progress, false);
                }
            });
            mSeekBar.setProgress(seekBarStrokeProgress);
            mColorPicker.setColor(oldColor);
            mColorPicker.setOldCenterColor(oldColor);

        }

}
