package net.luculent.libcore.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description: 圆形进度圈
 * @Author: yanlei.xia
 * @CreateDate: 2021/11/17 17:59
 */
public class CircleProgressBar extends View {

    private Paint paint;
    private float width = 100f;
    private float barWidth = 10f;

    private int angleDiff = 0;

    int[] colors = new int[]{Color.parseColor("#7EA9FD"), Color.parseColor("#E4EDFE"), Color.parseColor("#7EA9FD")};

    @SuppressLint("Recycle")
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(barWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FF6666"));
        paint.setShader(new SweepGradient(width * 0.5f, width * 0.5f, colors, null));
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(barWidth, barWidth, width - barWidth, width - barWidth, 0f + angleDiff, 270f, false, paint);
        } else {
            canvas.drawArc(new RectF(barWidth, barWidth, width - barWidth, -barWidth), 0f + angleDiff, 270f, false, paint);
        }
        angleDiff += 6;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        if (width == 0)
            width = 100f;
        barWidth = width * 0.11f;
    }
}
