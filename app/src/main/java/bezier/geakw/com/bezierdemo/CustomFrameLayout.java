package bezier.geakw.com.bezierdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by geakw on 2015/8/20.
 */
public class CustomFrameLayout extends FrameLayout {
    private float startX = 500;
    private float startY = 500;
    private float radius;
    private float DEFAULT_RADIUS = 35;
    private Paint paint;
    private ImageView iv;
    private float x;
    private float y;
    private boolean isTouch;
    private Rect rect = new Rect();
    private int[] location = new int[2];
    private float achorX;
    private float achorY;
    private Path path = new Path();

    public CustomFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        iv = new ImageView(context);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(params);
        iv.setImageResource(R.drawable.circle);
        addView(iv);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        iv.setX(startX - iv.getWidth() / 2);
        iv.setY(startY - iv.getHeight() / 2);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isTouch) {
            resetPath();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
            canvas.drawPath(path, paint);
            canvas.drawCircle(startX, startY, radius, paint);
            canvas.drawCircle(x, y, radius, paint);
            iv.setX(x - iv.getWidth() / 2);
            iv.setY(y - iv.getHeight() / 2);
        }
        super.onDraw(canvas);
    }

    private void resetPath() {
        path.reset();
        double distance = Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));
        radius = (float) (-distance / 15 + DEFAULT_RADIUS);
        float offsetX = (float) (radius * Math.sin(Math.atan((y - startY) / (x - startX))));
        float offsetY = (float) (radius * Math.cos(Math.atan((y - startY) / (x - startX))));

        float x1 = startX - offsetX;
        float y1 = startY + offsetY;

        float x2 = x - offsetX;
        float y2 = y + offsetY;

        float x3 = x + offsetX;
        float y3 = y - offsetY;

        float x4 = startX + offsetX;
        float y4 = startY - offsetY;

        path.moveTo(x1, y1);
        path.quadTo(achorX,achorY, x2, y2);
        path.lineTo(x3, y3);
        path.quadTo(achorX, achorY, x4, y4);
        path.lineTo(x1, y1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iv.getDrawingRect(rect);
                iv.getLocationOnScreen(location);
                rect.left = location[0];
                rect.top = location[1];
                rect.right = rect.right + location[0];
                rect.bottom = rect.bottom + location[1];
                if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    isTouch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                iv.setX(startX - iv.getWidth() / 2);
                iv.setY(startY - iv.getHeight() / 2);
                break;
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                iv.setX(startX - iv.getWidth() / 2);
                iv.setY(startY - iv.getHeight() / 2);
                break;
        }
        x = event.getX();
        y = event.getY();
        achorX = (event.getX() + startX) / 2;
        achorY = (event.getY() + startY) / 2;
        invalidate();
        return true;
    }
}
