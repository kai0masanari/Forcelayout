package jp.kai.forcelayout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import java.util.ArrayList

/**
 * Created by kai on 2017/05/01.
 * Main Class
 */

open class Forcelayout(private var mContext: Context): View(mContext){
    //instance
    lateinit private var properties: Properties

    private var targetnode = -1

    //styles of node and link
//    private val roundsize = 5
    private var drawstroke = true
    private val strokewidth = 5
    private var strokecolor = Color.BLACK
//    private var drawlabel = true
    private var fontsize = 30
    private var fontcolor = Color.BLACK

    /**
     * Create Builder
     */
    fun with(context: Context): Properties {
        mContext = context
        properties = Properties(context)

        return properties.prepare()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touch_x = event.x.toInt()
        val touch_y = event.y.toInt()

        when (event.action) {

            MotionEvent.ACTION_DOWN -> if (targetnode == -1) {
                for (i in 0..properties.nodeindex - 1) {

                    if (properties.nodes[i]!!.x + properties.nodes[i]!!.width >= touch_x &&
                        properties.nodes[i]!!.x <= touch_x &&
                        properties.nodes[i]!!.y + properties.nodes[i]!!.height >= touch_y &&
                        properties.nodes[i]!!.y <= touch_y) {

                        targetnode = i

                    }
                }
            }

            MotionEvent.ACTION_MOVE ->

                if (targetnode != -1) {
                    properties.nodes[targetnode]!!.x = touch_x - properties.nodes[targetnode]!!.width / 2
                    properties.nodes[targetnode]!!.y = touch_y - properties.nodes[targetnode]!!.height / 2
                }

            MotionEvent.ACTION_UP -> targetnode = -1

            MotionEvent.ACTION_CANCEL -> targetnode = -1
        }

        return true
    }


    // draw function
    override fun dispatchDraw(canvas: Canvas) {
        val paint = Paint()

        //draw link's line
        for (i in 0..properties.nedges - 1 - 1) {
            if (properties.edges[i]!!.group) {
                val e = properties.edges[i]!!
                val x1 = (properties.nodes[e.from]!!.x + properties.nodes[e.from]!!.width / 2).toFloat()
                val y1 = (properties.nodes[e.from]!!.y + properties.nodes[e.from]!!.height / 2).toFloat()
                val x2 = (properties.nodes[e.to]!!.x + properties.nodes[e.to]!!.width / 2).toFloat()
                val y2 = (properties.nodes[e.to]!!.y + properties.nodes[e.to]!!.height / 2).toFloat()

                if (drawstroke) {
                    paint.strokeWidth = strokewidth.toFloat()
                    paint.color = strokecolor
                    val pts = floatArrayOf(x1, y1, x2, y2)
                    canvas.drawLines(pts, paint)
                }
            }
        }

        /** draw node images and labels */
        val iterator: Iterator<Pair<String, Bitmap>> = properties.nodeslist.iterator()
        var index: Int = 0
        while(iterator.hasNext()){
            val pair: Pair<String, Bitmap> = iterator.next()

            canvas.drawBitmap(pair.second, properties.nodes[index]!!.x.toInt().toFloat(), properties.nodes[index]!!.y.toInt().toFloat(), paint)
            paint.textSize = fontsize.toFloat()
            paint.color = fontcolor
            canvas.drawText(properties.nodes[index]!!.nodename, (properties.nodes[index]!!.x + properties.nodes[index]!!.width).toInt().toFloat(), (properties.nodes[index]!!.y + properties.nodes[index]!!.height + 30.0).toInt().toFloat(), paint)

            index++
        }

//        /** draw nodes and labels */
//        for (i in convertlist.indices) {
//            canvas.drawBitmap(nodebitmap_array[i], properties.nodes[i]!!.x.toInt().toFloat(), properties.nodes[i]!!.y.toInt().toFloat(), paint)
//
//            paint.textSize = fontsize.toFloat()
//            paint.color = fontcolor
//            canvas.drawText(properties.nodes[i]!!.nodename, (properties.nodes[i]!!.x + properties.nodes[i]!!.width).toInt().toFloat(), (properties.nodes[i]!!.y + properties.nodes[i]!!.height + 30.0).toInt().toFloat(), paint)
//        }

        /** calculate spring-like forces */
        if (properties.nedges != 0) {
            properties.relax()
        }

        invalidate()
    }



}