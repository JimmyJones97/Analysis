package com.example.pwd61.analysis.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:PlasmaView
 * Created by pwd61 on 9/5/2019 11:03 AM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
// Custom view for rendering plasma.
//
// Note: suppressing lint wrarning for ViewConstructor since it is
//       manually set from the activity and not used in any layout.
@SuppressLint("ViewConstructor")
class PlasmaView extends View {
    private Bitmap mBitmap;
    private long mStartTime;
    // load our native library
    static {
        System.loadLibrary("plasma");
    }
    // implementend by libplasma.so
    private static native void renderPlasma(Bitmap  bitmap, long time_ms);

    public PlasmaView(Context context, int width, int height) {
        super(context);
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        mStartTime = System.currentTimeMillis();
    }

    @Override protected void onDraw(Canvas canvas) {
        renderPlasma(mBitmap, System.currentTimeMillis() - mStartTime);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        // force a redraw, with a different time-based pattern.
        invalidate();
    }
}

