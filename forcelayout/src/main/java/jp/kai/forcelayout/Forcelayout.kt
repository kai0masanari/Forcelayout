package jp.kai.forcelayout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Pair
import android.view.MotionEvent
import android.view.View

/**
 * Created by kai on 2017/05/01.
 * Main Class
 */

open class Forcelayout(mContext: Context): View(mContext){
    //instance
    private val properties: Properties = Properties(mContext)
    private var targetnode = -1

    /**
     * Create Builder
     */
    fun with(): Properties {
        return properties.prepare()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touch_x = event.x.toInt()
        val touch_y = event.y.toInt()

        when (event.action) {

            MotionEvent.ACTION_DOWN -> if (targetnode == -1) {
                for (i in 0..properties.nodeindex - 1) {
                    if (properties.nodes[i].x + properties.nodes[i].width >= touch_x &&
                        properties.nodes[i].x <= touch_x &&
                        properties.nodes[i].y + properties.nodes[i].height >= touch_y &&
                        properties.nodes[i].y <= touch_y) {

                        targetnode = i
                    }
                }
            }

            MotionEvent.ACTION_MOVE ->
                if (targetnode != -1) {
                    properties.nodes[targetnode].x = touch_x - properties.nodes[targetnode].width / 2
                    properties.nodes[targetnode].y = touch_y - properties.nodes[targetnode].height / 2
                }

            MotionEvent.ACTION_UP -> targetnode = -1

            MotionEvent.ACTION_CANCEL -> targetnode = -1
        }
        return true
    }

    // draw function
    override fun dispatchDraw(canvas: Canvas) {
        val paint = Paint()

        if(properties.isReady) {
            //draw link's line
            for (i in 0..properties.nedges - 1 ) {
                if (properties.edges[i].group) {
                    val e = properties.edges[i]
                    val x1 = (properties.nodes[e.from].x + properties.nodes[e.from].width / 2).toFloat()
                    val y1 = (properties.nodes[e.from].y + properties.nodes[e.from].height / 2).toFloat()
                    val x2 = (properties.nodes[e.to].x + properties.nodes[e.to].width / 2).toFloat()
                    val y2 = (properties.nodes[e.to].y + properties.nodes[e.to].height / 2).toFloat()

                    paint.strokeWidth = STROKE_WIDTH
                    paint.color = COLOR_BLACK
                    canvas.drawLine(x1, y1, x2, y2, paint)
                }
            }

            /** draw node images and labels */
            val iterator: Iterator<Pair<String, Bitmap>> = properties.nodesList.iterator()
            var index: Int = 0
            while (iterator.hasNext()) {
                val pair: Pair<String, Bitmap> = iterator.next()

                canvas.drawBitmap(pair.second, properties.nodes[index].x.toFloat(), properties.nodes[index].y.toFloat(), paint)
                paint.textSize = FONT_SIZE
                paint.color = COLOR_BLACK
                canvas.drawText(properties.nodes[index].nodename, (properties.nodes[index].x + properties.nodes[index].width).toFloat(), (properties.nodes[index].y + properties.nodes[index].height + 30.0).toFloat(), paint)

                index++
            }

            /** calculate spring-like forces */
            properties.relax()
        }

        invalidate()
    }
}