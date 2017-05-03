package jp.kai.forcelayout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Pair
import android.view.Display
import android.view.WindowManager

/**
 * Created by kai on 2017/05/01.
 * Builder Class
 */

//FIX-ME relax()を別のクラスに切り分けたい
class Properties(mContext: Context){
    private val mContext: Context = mContext

    /** draw area */
    private var display_width: Float = 0f
    private var display_height: Float = 0f
    private var nodearea_width: Float = 0.toFloat() //draw area = screen size
    private var nodearea_height: Float = 0.toFloat()

    /** spring-like force */
    private var distance: Int = 300
    private var bounce: Double = 0.08
    private var gravity: Double = 0.04

    //Node Size
    //TODO ノードの大きさなどは別のBuilderで設定するように変えたい
    private var reduction: Int = 30
    private var nodeswidth: Int = 150 //node's width

    private var nodeindex: Int = 0

    //TODO ここで初期化処理を行う
    init {
        nodeindex = 0
    }

    fun prepare(): Properties{
        val mDisplay = getDisplayMetrics(mContext)
        display_width = mDisplay.getWidth().toFloat()
        display_height = mDisplay.getHeight().toFloat()

        return this
    }

    private fun nodes(nodemaps: List<Pair<String, Int>>): Properties{
        val resource = mContext.resources
        val iterator :Iterator<Pair<String, Int>> = nodemaps.iterator()

        while (iterator.hasNext()){
            /** Node List */
            val pair: Pair<String, Int> = iterator.next()

            /** get image properties */
            val imageOptions = BitmapFactory.Options()
            imageOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
            imageOptions.inJustDecodeBounds = true
            BitmapFactory.decodeResource(resource, pair.second, imageOptions)
            val bitmapwidth = imageOptions.outWidth
            val bmfOptions = BitmapFactory.Options()

            /** resize image */
            reduction = bitmapwidth / nodeswidth
            if (reduction != 0) {
                bmfOptions.inSampleSize = reduction
            }

            var bitmap = BitmapFactory.decodeResource(resource, pair.second, bmfOptions)
            var imgheight = bmfOptions.outHeight
            var imgwidth = bmfOptions.outWidth

            if (imgwidth != nodeswidth) {
                bitmap = resizeBitmap(bitmap, nodeswidth)

                imgheight = bitmap.height
                imgwidth = bitmap.width
            }

            nodearea_width = display_width - imgwidth
            nodearea_height = display_height - imgheight

            addNode(pair.first, nodeindex, imgwidth, imgheight)

            nodename_array.add(pair.first)

            nodeslist.put(pair.first, bitmap)
            nodeindex++
        }

        return this
    }

    private fun links(): Properties{
        //TODO ここにはリンク処理
        return this
    }

    private fun linkStrength(bounce: Double): Properties{
        this.bounce = bounce
        return this
    }

    private fun distance(dictance: Int): Properties{
        this.distance = distance
       return this
    }

    private fun gravity(gravity: Double): Properties{
        this.gravity = gravity
        return this
    }

    //TODO スタイル等の情報はまた別のBuilderを作るべきかもしれない
//    private fun nodeSize(): Properties{
//        return this
//    }

    private fun getDisplayMetrics(context: Context): Display {
        val winMan = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val disp = winMan.defaultDisplay
        return disp
    }

}
