package jp.kai.forcelayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import android.view.ScaleGestureDetector
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

open class Forcelayout : View {
    /** instance */
    private val forceProperty: ForceProperty
    private val nodeProperty: NodeProperty = NodeProperty()
    private val linkProperty: LinkProperty = LinkProperty()

    private var targetNode = -1

    private var touchX: Float = 0f
    private var touchY: Float = 0f

    private var pointF: PointF = PointF()
    private var scaleFactor = 1f
    private var detector: ScaleGestureDetector? = null

    private var startX = 0f
    private var startY = 0f
    private var translateX = 0f
    private var translateY = 0f
    private var previousTranslateX = 0f
    private var previousTranslateY = 0f

    // TODO enumか何かで管理する: pan/expand/drag
    private var zooming: Boolean = false

    init {
        detector = ScaleGestureDetector(context, ScaleListener())
    }

    constructor(mContext: Context) : super(mContext) {
        forceProperty = ForceProperty(mContext)
    }

    constructor(mContext: Context, attrs: AttributeSet) : super(mContext, attrs) {
        forceProperty = ForceProperty(mContext)
    }

    constructor(mContext: Context, attrs: AttributeSet, defStyleAttr: Int) : super(mContext, attrs, defStyleAttr) {
        forceProperty = ForceProperty(mContext)
    }

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
        this.touchX = event.x
        this.touchY = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {

                //TODO 位置補正はタッチ時の判定でやったほうが楽そうなので、頑張る translateとscaleFactorあたりを調査

                if (targetNode == -1) {
                    for (i in 0 until forceProperty.nodeIndex) {
                        if (forceProperty.nodes[i].x + forceProperty.nodes[i].width >= (touchX - translateX) / scaleFactor &&
                                forceProperty.nodes[i].x <= (touchX - translateX) / scaleFactor &&
                                forceProperty.nodes[i].y + forceProperty.nodes[i].height >= (touchY - translateY) / scaleFactor &&
                                forceProperty.nodes[i].y <= (touchY - translateY) / scaleFactor) {

                            targetNode = i
                        }
                    }
                    if (targetNode == -1) {
                        // TODO koko?
                        startX = event.x - previousTranslateX
                        startY = event.y - previousTranslateY
                    }

                } else {
                    forceProperty.nodes[targetNode].x = ((touchX - translateX) / scaleFactor - forceProperty.nodes[targetNode].width / 2)
                    forceProperty.nodes[targetNode].y = ((touchY - translateY) / scaleFactor - forceProperty.nodes[targetNode].height / 2)

                }


            }

            MotionEvent.ACTION_MOVE -> {
                if (!zooming) {

                    if (targetNode == -1) {
                        // TODO koko?
                        translateX = event.x - startX
                        translateY = event.y - startY

                    } else {
//                        forceProperty.nodes[targetNode].x = touchX - forceProperty.nodes[targetNode].width / 2
//                        forceProperty.nodes[targetNode].y = touchY - forceProperty.nodes[targetNode].height / 2
                        forceProperty.nodes[targetNode].x = ((touchX - translateX) / scaleFactor - forceProperty.nodes[targetNode].width / 2)
                        forceProperty.nodes[targetNode].y = ((touchY - translateY) / scaleFactor - forceProperty.nodes[targetNode].height / 2)
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                zooming = false
                targetNode = -1

                previousTranslateX = translateX
                previousTranslateY = translateY
            }

            MotionEvent.ACTION_POINTER_UP -> {
                zooming = false

                previousTranslateX = translateX
                previousTranslateY = translateY
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                midPoint(pointF, event)
                zooming = true
                targetNode = -1
            }

            MotionEvent.ACTION_CANCEL -> {
                zooming = false
                targetNode = -1

                previousTranslateX = translateX
                previousTranslateY = translateY
            }

        }
        detector!!.onTouchEvent(event)

        return true
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM))
            return true
        }
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    // draw function
    override fun dispatchDraw(canvas: Canvas) {
        val paint = Paint()

        // scale the canvas
        canvas.scale(scaleFactor, scaleFactor, pointF.x, pointF.y)

        canvas.translate(translateX / scaleFactor, translateY / scaleFactor)

        if (targetNode != -1) {
            forceProperty.nodes[targetNode].x = ((touchX - translateX) / scaleFactor - forceProperty.nodes[targetNode].width / 2)// / scaleFactor
            forceProperty.nodes[targetNode].y = ((touchY - translateY) / scaleFactor - forceProperty.nodes[targetNode].height / 2)// / scaleFactor
        }

        if (forceProperty.isReady) {
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
            if (GraphStyle.isImgDraw) {
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
            } else {
                paint.color = GraphStyle.nodeColor

                for (i in 0 until forceProperty.nodeIndex) {
                    paint.color = GraphStyle.nodeColor
                    canvas.drawCircle((forceProperty.nodes[i].x + GraphStyle.nodesWidth / 2).toFloat(), (forceProperty.nodes[i].y + GraphStyle.nodesWidth / 2).toFloat(), (GraphStyle.nodesWidth / 2).toFloat(), paint)

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

    private fun midPoint(pointF: PointF, motionEvent: MotionEvent) {
        val x = motionEvent.getX(0) + motionEvent.getX(1)
        val y = motionEvent.getY(0) + motionEvent.getY(1)
        pointF.set(x / 2, y / 2)
    }
}