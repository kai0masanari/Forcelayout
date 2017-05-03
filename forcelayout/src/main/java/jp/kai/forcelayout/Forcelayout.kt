package jp.kai.forcelayout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import jp.kai.forcelayout.Util.Companion.getCroppedBitmap
import java.util.*

/**
 * Created by kai on 2017/05/01.
 * Main Class
 */

open class Forcelayout(private var mContext: Context): View(mContext){
    //instance
    lateinit private var properties: Properties

//    private val nodeslist = HashMap<String, Bitmap>()

    internal var nodename_array = ArrayList<String>()
    internal var nodebitmap_array = ArrayList<Bitmap>()
    private val convertlist = ArrayList<String>()


    private var targetnode = -1

    //styles of node and link
    private val roundsize = 5
    private var nodearea_width: Float = 0.toFloat() //draw area = screen size
    private var nodearea_height: Float = 0.toFloat()
    //value of stroke
    private var drawstroke = true
    private val strokewidth = 5
    private var strokecolor = Color.BLACK
    //value of label
    private var drawlabel = true
    private var fontsize = 30
    private var fontcolor = Color.BLACK


    init {
        init_nodes()
    }

    private fun init_nodes() {
        val nodes = arrayOfNulls<Node>(200)
        nodename_array.clear()
        nodebitmap_array.clear()
        convertlist.clear()
    }

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
                for (i in 0..nodeindex - 1) {
                    if (nodes[i].x + nodes[i].width >= touch_x && nodes[i].x <= touch_x && nodes[i].y + nodes[i].height >= touch_y && nodes[i].y <= touch_y) {
                        targetnode = i
                    }
                }
            }

            MotionEvent.ACTION_MOVE ->

                if (targetnode != -1) {
                    nodes[targetnode].x = touch_x - nodes[targetnode].width / 2
                    nodes[targetnode].y = touch_y - nodes[targetnode].height / 2
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
        //        if(edges.length != 0){
        for (i in 0..nedges - 1 - 1) {
            if (edges[i].group) {
                val e = edges[i]
                val x1 = (nodes[e.from].x + nodes[e.from].width / 2).toFloat()
                val y1 = (nodes[e.from].y + nodes[e.from].height / 2).toFloat()
                val x2 = (nodes[e.to].x + nodes[e.to].width / 2).toFloat()
                val y2 = (nodes[e.to].y + nodes[e.to].height / 2).toFloat()

                if (drawstroke) {
                    paint.strokeWidth = strokewidth.toFloat()
                    paint.color = strokecolor
                    val pts = floatArrayOf(x1, y1, x2, y2)
                    canvas.drawLines(pts, paint)
                }
            }
        }
        //        }

        //draw node's image
        for (str in nodeslist.keys) {
            if (convertlist.indexOf(str) == -1) {
                val _bitmap = nodeslist[str]
                nodebitmap_array.add(getCroppedBitmap(nodeslist[str], roundsize))
                convertlist.add(str)
            }
        }


        //draw label
        for (i in convertlist.indices) {
            canvas.drawBitmap(nodebitmap_array[i], nodes[i].x.toInt().toFloat(), nodes[i].y.toInt().toFloat(), paint)

            if (drawlabel) {
                paint.textSize = fontsize.toFloat()
                paint.color = fontcolor
                canvas.drawText(nodes[i].nodename!!, (nodes[i].x + nodes[i].width).toInt().toFloat(), (nodes[i].y + nodes[i].height + 30.0).toInt().toFloat(), paint)
            }
        }

        if (nedges != 0) {

            properties.relax()
        }

        invalidate()
    }



}