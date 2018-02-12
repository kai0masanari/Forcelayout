package jp.kai.forcelayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import jp.kai.forcelayout.properties.ForceProperty
import jp.kai.forcelayout.properties.GraphStyle
import jp.kai.forcelayout.properties.GraphStyle.defaultColor
import jp.kai.forcelayout.properties.LinkProperty
import jp.kai.forcelayout.properties.NodeProperty

/**
 * Created by kai on 2017/05/01.
 * Main Class
 */

open class Forcelayout: View{
    /** instance */
    private var  forceProperty: ForceProperty
    private val nodeProperty: NodeProperty = NodeProperty()
    private val linkProperty: LinkProperty = LinkProperty()

    /** constructor */
    constructor(mContext: Context) : super(mContext) {
        forceProperty = ForceProperty(mContext)
    }
    constructor(mContext: Context, attrs: AttributeSet) : super(mContext, attrs) {
        forceProperty = ForceProperty(mContext)
    }

    private var targetNode = -1
    private var touch_x: Int = 0
    private var touch_y: Int = 0

    /**
     * Create Builders
     */
    fun node(): NodeProperty {
        return nodeProperty.prepare()
    }

    fun link(): LinkProperty {
        return linkProperty.prepare()
    }

    fun with(): ForceProperty {
        return forceProperty.prepare()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x.toInt()
        val touchY = event.y.toInt()

        this.touch_x = touchX
        this.touch_y = touchY

        when (event.action) {

            MotionEvent.ACTION_DOWN -> if (targetNode == -1) {
                (0 until forceProperty.nodeIndex)
                        .filter { forceProperty.nodes[it].x + forceProperty.nodes[it].width >= touchX && forceProperty.nodes[it].x <= touchX && forceProperty.nodes[it].y + forceProperty.nodes[it].height >= touchY && forceProperty.nodes[it].y <= touchY }
                        .forEach { targetNode = it }
            }else{
                if (targetNode != -1) {
                    forceProperty.nodes[targetNode].x = touchX - forceProperty.nodes[targetNode].width / 2
                    forceProperty.nodes[targetNode].y = touchY - forceProperty.nodes[targetNode].height / 2
                }
            }

            MotionEvent.ACTION_MOVE ->
                if (targetNode != -1) {
                    forceProperty.nodes[targetNode].x = touchX - forceProperty.nodes[targetNode].width / 2
                    forceProperty.nodes[targetNode].y = touchY - forceProperty.nodes[targetNode].height / 2
                }

            MotionEvent.ACTION_HOVER_MOVE ->
                if (targetNode != -1) {
                    forceProperty.nodes[targetNode].x = touchX - forceProperty.nodes[targetNode].width / 2
                    forceProperty.nodes[targetNode].y = touchY - forceProperty.nodes[targetNode].height / 2
                }

            MotionEvent.ACTION_HOVER_EXIT ->
                if (targetNode != -1) {
                    forceProperty.nodes[targetNode].x = touchX - forceProperty.nodes[targetNode].width / 2
                    forceProperty.nodes[targetNode].y = touchY - forceProperty.nodes[targetNode].height / 2
                }

            MotionEvent.ACTION_UP -> targetNode = -1

            MotionEvent.ACTION_CANCEL -> targetNode = -1
        }
        return true
    }

    // draw function
    override fun dispatchDraw(canvas: Canvas) {
        val paint = Paint()

        if (targetNode != -1) {
            forceProperty.nodes[targetNode].x = touch_x - forceProperty.nodes[targetNode].width / 2
            forceProperty.nodes[targetNode].y = touch_y - forceProperty.nodes[targetNode].height / 2
        }

        if(forceProperty.isReady) {
            //draw link's line
            for (i in 0 until forceProperty.nEdges) {
                if (forceProperty.edges[i].group) {
                    val e = forceProperty.edges[i]
                    val x1 = (forceProperty.nodes[e.from].x + forceProperty.nodes[e.from].width / 2).toFloat()
                    val y1 = (forceProperty.nodes[e.from].y + forceProperty.nodes[e.from].height / 2).toFloat()
                    val x2 = (forceProperty.nodes[e.to].x + forceProperty.nodes[e.to].width / 2).toFloat()
                    val y2 = (forceProperty.nodes[e.to].y + forceProperty.nodes[e.to].height / 2).toFloat()

                    paint.strokeWidth = GraphStyle.linkWidth
                    paint.color = GraphStyle.linkColor
                    canvas.drawLine(x1, y1, x2, y2, paint)
                }
            }
            if(GraphStyle.isImgDraw) {
                /** draw node images and labels */
                val iterator: Iterator<Pair<String, Bitmap>> = forceProperty.nodesList.iterator()
                var index = 0
                while (iterator.hasNext()) {
                    val pair: Pair<String, Bitmap> = iterator.next()

                    paint.color = defaultColor
                    canvas.drawBitmap(pair.second, forceProperty.nodes[index].x.toFloat(), forceProperty.nodes[index].y.toFloat(), paint)

                    paint.textSize = GraphStyle.fontSize
                    paint.color = GraphStyle.fontColor
                    canvas.drawText(forceProperty.nodes[index].nodename, (forceProperty.nodes[index].x + forceProperty.nodes[index].width).toFloat(), (forceProperty.nodes[index].y + forceProperty.nodes[index].height + 30.0).toFloat(), paint)

                    index++
                }
            }else{
                paint.color = GraphStyle.nodeColor

                for(i in 0 until forceProperty.nodeIndex){
                    paint.color = GraphStyle.nodeColor
                    canvas.drawCircle((forceProperty.nodes[i].x + GraphStyle.nodesWidth/2).toFloat(), (forceProperty.nodes[i].y + GraphStyle.nodesWidth/2).toFloat(), (GraphStyle.nodesWidth/2).toFloat(), paint)

                    paint.textSize = GraphStyle.fontSize
                    paint.color = GraphStyle.fontColor
                    canvas.drawText(forceProperty.nodes[i].nodename, (forceProperty.nodes[i].x + forceProperty.nodes[i].width).toFloat(), (forceProperty.nodes[i].y + forceProperty.nodes[i].height + 30.0).toFloat(), paint)
                }

            }
            /** calculate spring-like forces */
            forceProperty.relax()
        }

        invalidate()
    }
}