package jp.kai.banemodel;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by kai on 2016/09/03.
 */
public class NodeResize {
    private static final String TAG = "imageResize";

    public static Bitmap resizeBitmap(Bitmap src, int w_px, int h_px){


        int srcWidth = src.getWidth(); // 元画像のwidth
        int srcHeight = src.getHeight(); // 元画像のheight
        Log.d(TAG, "srcWidth = " + String.valueOf(srcWidth)
                + " px, srcHeight = " + String.valueOf(srcHeight) + " px");

        // 画面サイズを取得する
        Matrix matrix = new Matrix();

        float widthScale = w_px / srcWidth;
        float heightScale = h_px / srcHeight;
        if (widthScale > heightScale) {
            matrix.postScale(heightScale, heightScale);
        } else {
            matrix.postScale(widthScale, widthScale);
        }
        // リサイズ
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
        src = null;
        return dst;
    }
}
