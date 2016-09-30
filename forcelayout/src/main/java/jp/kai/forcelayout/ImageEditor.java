package jp.kai.forcelayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by kai on 2016/09/30.
 */

public class ImageEditor {
    public ImageEditor(){

    }

    public static Bitmap resizeBitmap(Bitmap src, int width){
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();

        // get Screen size
        Matrix matrix = new Matrix();

        float widthScale = (float)width / (float)srcWidth;
        matrix.postScale(widthScale, widthScale);


        // resize
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
        src.recycle();
        src = null;

        return dst;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, int round) {

        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();

        final Rect rect   = new Rect(0, 0, width, height);
        final RectF rectf = new RectF(0, 0, width, height);

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.drawRoundRect(rectf, width / round, height / round, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


}
