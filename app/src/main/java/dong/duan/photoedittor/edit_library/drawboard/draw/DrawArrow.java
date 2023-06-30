package dong.duan.photoedittor.edit_library.drawboard.draw;


import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;

public class DrawArrow extends Draw {
    private float downX;
    private float downY;
    private float lastX;
    private float lastY;

    public DrawArrow() {
    }

    public void setPoint(PointF point0, PointF point1) {
        this.downX = point0.x;
        this.downY = point0.y;
        this.lastX = point1.x;
        this.lastY = point1.y;
    }

    @Override
    public void actionDown(Canvas canvas, float x, float y) {
        super.actionDown(canvas, x, y);
        downX = x;
        downY = y;
    }

    @Override
    public void actionMove(Canvas canvas, float x, float y) {
        super.actionMove(canvas, x, y);
        lastX = x;
        lastY = y;
        canvas.drawLine(downX, downY, lastX, lastY, paint);
    }

    @Override
    public void draw(Canvas canvas) {
        // Draw the line
        canvas.drawLine(downX, downY, lastX, lastY, paint);

        // Calculate the coordinates for the arrowhead
        float arrowSize = 20f;
        float angle = (float) Math.atan2(lastY - downY, lastX - downX);
        float arrowX1 = lastX - arrowSize * (float) Math.cos(angle + Math.PI / 4);
        float arrowY1 = lastY - arrowSize * (float) Math.sin(angle + Math.PI / 4);
        float arrowX2 = lastX - arrowSize * (float) Math.cos(angle - Math.PI / 4);
        float arrowY2 = lastY - arrowSize * (float) Math.sin(angle - Math.PI / 4);

        // Draw the arrowhead
        Path arrowPath = new Path();
        arrowPath.moveTo(lastX, lastY);
        arrowPath.lineTo(arrowX1, arrowY1);
        arrowPath.moveTo(lastX, lastY);
        arrowPath.lineTo(arrowX2, arrowY2);
        canvas.drawPath(arrowPath, paint);
    }
}
