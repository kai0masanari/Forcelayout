package jp.kai.forcelayout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Pair
import jp.kai.forcelayout.Util.Companion.getDisplayMetrics
import jp.kai.forcelayout.Util.Companion.resizeBitmap
import jp.kai.forcelayout.model.Edge
import jp.kai.forcelayout.model.Node
import java.util.*


/**
 * Created by kai on 2017/05/01.
 * Builder Class
 */

//FIX-ME relax()を別のクラスに切り分けたい
class Properties(private val mContext: Context){
    /** node's and link's */
    private var nodes = arrayOfNulls<Node>(200)
    private var edges = arrayOfNulls<Edge>(500)
    private var nodeindex: Int = 0
    private var nedges: Int = 0
    private var nodename_array = ArrayList<String>()
    private var nodeslist = ArrayList<Pair<String, Bitmap>>()

    /** draw area */
    private var display_width: Float = 0f
    private var display_height: Float = 0f
    private var drawAreaWidth: Float = 0.toFloat() //draw area = screen size
    private var drawAreaHeight: Float = 0.toFloat()

    /** spring-like force */
    private var distance: Int = 300
    private var bounce: Double = 0.08
    private var gravity: Double = 0.04

    //TODO ノードの大きさなどは別のBuilderで設定するように変えたい
    private var reduction: Int = 30
    private var nodeswidth: Int = 150 //node's width

    //TODO ここで初期化処理を行う
    init {
        nodeindex = 0

    }

    fun prepare(): Properties{
        val mDisplay = getDisplayMetrics(mContext)
        display_width = mDisplay.width.toFloat()
        display_height = mDisplay.height.toFloat()

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

            drawAreaWidth = display_width - imgwidth
            drawAreaHeight = display_height - imgheight

            addNode(pair.first, nodeindex, imgwidth, imgheight)

            nodename_array.add(pair.first)

            nodeslist.add(Pair(pair.first, bitmap))
            nodeindex++
        }

        return this
    }

    private fun links(linkmaps: List<String>): Properties{
        for (i in 0..nodename_array.size - 1) {
            for (j in 0..nodename_array.size - 1) {
                if (i != j) {
                    addEdge(i, j)
                }
            }
        }

        for (k in 0..linkmaps.size - 1) {
            val pair = linkmaps[k].split("-")
            //val pair: Array<String> = linkmaps[k].split("-")

            if (pair.size == 2) {
                for (i in 0..nedges - 1) {
                    if (edges[i]!!.from == nodename_array.indexOf(pair[0]) && edges[i]!!.to == nodename_array.indexOf(pair[1]) || edges[i]!!.to == nodename_array.indexOf(pair[0]) && edges[i]!!.from == nodename_array.indexOf(pair[1])) {
                        edges[i]!!.group = true
                    }
                }
            }
        }
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

    fun addNode(lbl: String, index: Int, width: Int, height: Int) {
        val n = Node()

        n.x = drawAreaWidth * Math.random()
        n.y = (drawAreaHeight - 10) * Math.random() + 10
        n.nodename = lbl
        n.width = width.toDouble()
        n.height = height.toDouble()
        n.dx = 0.0
        n.dy = 0.0

        nodes[index] = n
    }

    fun addEdge(from: Int, to: Int) {
        val e = Edge()
        e.from = from
        e.to = to
        e.group = false
        edges[nedges++] = e
    }

    fun relax() {
        for (i in 0..nodeindex - 1) {
            var fx = 0.0
            var fy = 0.0

            for (j in 0..nodeindex - 1) {

                val distX = ((nodes[i]!!.x + nodes[i]!!.width / 2 - (nodes[j]!!.x + nodes[j]!!.width / 2)).toInt()).toDouble()
                val distY = ((nodes[i]!!.y + nodes[i]!!.height / 2 - (nodes[j]!!.y + nodes[j]!!.height / 2)).toInt()).toDouble()
                var rsq = distX * distX + distY * distY
                val rsq_round = rsq.toInt() * 100
                rsq = (rsq_round / 100).toDouble()

                var coulombdist_x = COULOMB * distX
                var coulombdist_y = COULOMB * distY
                val coulombdist_round_x = coulombdist_x.toInt() * 100
                val coulombdist_round_y = coulombdist_y.toInt() * 100
                coulombdist_x = (coulombdist_round_x / 100).toDouble()
                coulombdist_y = (coulombdist_round_y / 100).toDouble()



                if (rsq != 0.0 && Math.sqrt(rsq) < distance) {
                    fx += coulombdist_x / rsq
                    fy += coulombdist_y / rsq
                }
            }

            //gravity : node - central
            var distX_c = 0.0
            var distY_c = 0.0
            distX_c = display_width / 2 - (nodes[i]!!.x + nodes[i]!!.width / 2)
            distY_c = display_height / 2 - (nodes[i]!!.y + nodes[i]!!.height / 2)

            fx += gravity * distX_c
            fy += gravity * distY_c


            //node in group : from - to
            for (j in 0..nedges - 1 - 1) {
                var distX = 0.0
                var distY = 0.0
                if (edges[j]!!.group) {
                    if (i == edges[j]!!.from) {
                        distX = nodes[edges[j]!!.to]!!.x - nodes[i]!!.x
                        distY = nodes[edges[j]!!.to]!!.y - nodes[i]!!.y

                    } else if (i == edges[j]!!.to) {
                        distX = nodes[edges[j]!!.from]!!.x - nodes[i]!!.x
                        distY = nodes[edges[j]!!.from]!!.y - nodes[i]!!.y
                    }
                }
                fx += bounce * distX
                fy += bounce * distY
            }

            nodes[i]!!.dx = (nodes[i]!!.dx + fx) * ATTENUATION
            nodes[i]!!.dy = (nodes[i]!!.dy + fy) * ATTENUATION


            nodes[i]!!.x += nodes[i]!!.dx
            nodes[i]!!.y += nodes[i]!!.dy

        }

    }
}
