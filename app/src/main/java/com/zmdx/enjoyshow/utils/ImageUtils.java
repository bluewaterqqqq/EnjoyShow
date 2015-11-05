
package com.zmdx.enjoyshow.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtils {
    @SuppressWarnings("unused")
    private static final String TAG = "ImageUtils";

    /**
     * Create a bitmap object from a drawable object.
     * 
     * @return null may be returned if the drawable object has no intrinsic
     *         width/height.
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return drawable2Bitmap(drawable, false);
    }

    /**
     * If not case directReturn,we will create a new empty bitmap for drawing. 
     * @param drawable
     * @param directReturn
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable, boolean directReturn) {
        if (drawable == null) {
            return null;
        }
        ;

        /**
         * In that case, we cannot release the returned Bitmap anymore.
         */
        if (directReturn && drawable instanceof BitmapDrawable) {
            Bitmap tmp = ((BitmapDrawable) drawable).getBitmap();
            if (tmp != null) {
                return tmp;
            }
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            // No intrinsic width/height, such as a solid color
            return null;
        }

        Drawable clone = drawable.getConstantState().newDrawable();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                clone.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        clone.setBounds(0, 0, width, height);
        clone.draw(canvas);
        return bitmap;
    }


    /**
     * Create a new bitmap by scaling the source bitmap to new width and height.
     */
    public static Bitmap scaleTo(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        if (src == null) {
            return null;
        }

        int width = src.getWidth();
        int height = src.getHeight();
        if (width == newHeight && height == newHeight) {
            return src;
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap target = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);

        if (recycle && src != target) {
            src.recycle();
        }

        return target;
    }

}
