package dong.duan.photoedittor.edit_library.drawboard.action;

import android.graphics.Canvas;

public interface MotionAction {

    void actionDown(Canvas canvas, float x, float y);

    void actionMove(Canvas canvas, float x, float y);

    void actionUp(Canvas canvas, float x, float y);
}
