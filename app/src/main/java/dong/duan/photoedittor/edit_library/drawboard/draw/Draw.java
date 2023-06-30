package dong.duan.photoedittor.edit_library.drawboard.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import dong.duan.photoedittor.edit_library.drawboard.action.MotionAction;

public abstract class Draw implements MotionAction {

    Paint paint;

    public Draw(){

    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void actionDown(Canvas canvas, float x, float y) {
        Log.d(getClass().getSimpleName(),String.format("actionDown: %f, %f", x, y));
    }

    @Override
    public void actionMove(Canvas canvas, float x, float y) {
        Log.d(getClass().getSimpleName(),String.format("actionMove: %f, %f", x, y));
    }

    @Override
    public void actionUp(Canvas canvas, float x, float y) {
        Log.d(getClass().getSimpleName(),String.format("actionUp: %f, %f", x, y));
    }

    public abstract void draw(Canvas canvas);
}
