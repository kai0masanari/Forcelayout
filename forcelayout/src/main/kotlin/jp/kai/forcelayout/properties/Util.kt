package jp.kai.forcelayout.properties

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Matrix
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuffXfermode
import android.graphics.PorterDuff
import android.view.Display
import android.view.WindowManager

/**
 * Created by kai on 2017/05/03.
 */

class Util {
    companion object{
        fun getDisplayMetrics(context: Context): Display {
            val winMan = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            return winMan.defaultDisplay
        }

        fun resizeBitmap(src: Bitmap, width: Int): Bitmap {
            val srcWidth = src.width
            val srcHeight = src.height

            // get Screen size
            val matrix = Matrix()

            val widthScale = width.toFloat() / srcWidth.toFloat()
            matrix.postScale(widthScale, widthScale)

            // resize
            val dst = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true)
            src.recycle()

            return dst
        }

        fun getCroppedBitmap(bitmap: Bitmap, round: Int): Bitmap {
            val width = bitmap.width
            val height = bitmap.height

            val rect = Rect(0, 0, width, height)
            val rectf = RectF(0f, 0f, width.toFloat(), height.toFloat())

            val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val paint = Paint()
            paint.isAntiAlias = true

            canvas.drawRoundRect(rectf, (width / round).toFloat(), (height / round).toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)

            return output
        }
    }
}