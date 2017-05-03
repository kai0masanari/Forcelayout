package jp.kai.forcelayout

import android.graphics.*

/**
 * Created by kai on 2017/05/03.
 */

class EditImage{
    fun resizeBitmap(src: Bitmap, width: Int): Bitmap {
        val src = src
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