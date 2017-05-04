package jp.kai.forcelayout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Pair
import jp.kai.forcelayout.Util.Companion.getCroppedBitmap
import jp.kai.forcelayout.Util.Companion.getDisplayMetrics
import jp.kai.forcelayout.Util.Companion.resizeBitmap
import jp.kai.forcelayout.model.Edge
import jp.kai.forcelayout.model.Node
import java.util.ArrayList

/**
 * Created by kai on 2017/05/01.
 * Builder Class
 */

//FIX-ME relax()を別のクラスに切り分けたい
class Properties(private val mContext: Context){
    var isReady: Boolean = false

    /** node's and link's */
    var nodes = ArrayList<Node>(200)
    var edges = ArrayList<Edge>(500)
    var nodeindex: Int = 0
    var nedges: Int = 0
    private var nodeNameArray = ArrayList<String>()
    var nodesList = ArrayList<Pair<String, Bitmap>>()

    /** draw area */
    private var displayWidth: Float = 0f
    private var displayHeight: Float = 0f
    private var drawAreaWidth: Float = 0f //draw area = screen size
    private var drawAreaHeight: Float = 0f

    /** spring-like force */
    private var distance: Int = 300
    private var bounce: Double = 0.08
    private var gravity: Double = 0.04

    //TODO ノードの大きさなどは別のBuilderで設定するように変えたい
    private var reduction: Int = 30
    private var nodeswidth: Int = 150 //node's width
    private val roundSize = 5

    fun prepare(): Properties{
        val mDisplay = getDisplayMetrics(mContext)
        displayWidth = mDisplay.width.toFloat()
        displayHeight = mDisplay.height.toFloat()

        return this
    }

    fun nodes(nodemaps: ArrayList<Pair<String, Int>>): Properties{
        initNodes()

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
            val bitmapWidth = imageOptions.outWidth
            val bmfOptions = BitmapFactory.Options()

            /** resize image */
            reduction = bitmapWidth / nodeswidth
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

            drawAreaWidth = displayWidth - imgwidth
            drawAreaHeight = displayHeight - imgheight

            addNode(pair.first, imgwidth, imgheight)

            nodeNameArray.add(pair.first)

            nodesList.add(Pair(pair.first, getCroppedBitmap(bitmap, roundSize)))
        }

        return this
    }

    fun links(linkMaps: List<String>): Properties{
        initEdges()

        for (i in 0..nodeNameArray.size - 1) {
            for (j in 0..nodeNameArray.size - 1) {
                if (i != j) {
                    addEdge(i, j)
                }
            }
        }

        for (k in 0..linkMaps.size - 1) {
            val pair = linkMaps[k].split("-")

            if (pair.size == 2) {
                for (i in 0..nedges - 1) {
                    if (edges[i].from == nodeNameArray.indexOf(pair[0]) && edges[i].to == nodeNameArray.indexOf(pair[1]) || edges[i].to == nodeNameArray.indexOf(pair[0]) && edges[i].from == nodeNameArray.indexOf(pair[1])) {
                        edges[i].group = true
                    }
                }
            }
        }
        //TODO builderの終わりを追加し、そこに記述する
        isReady = true
        return this
    }

    fun linkStrength(bounce: Double): Properties{
        this.bounce = bounce
        return this
    }

    fun distance(distance: Int): Properties{
        this.distance = distance
        return this
    }

    fun gravity(gravity: Double): Properties{
        this.gravity = gravity
        return this
    }

    fun addNode(lbl: String, width: Int, height: Int) {
        val n = Node()
        n.x = drawAreaWidth * Math.random()
        n.y = (drawAreaHeight - 10) * Math.random() + 10
        n.nodename = lbl
        n.width = width.toDouble()
        n.height = height.toDouble()
        n.dx = 0.0
        n.dy = 0.0
        nodes.add(n)
        nodeindex++
    }

    fun addEdge(from: Int, to: Int) {
        val e = Edge()
        e.from = from
        e.to = to
        e.group = false
        edges.add(e)
        nedges++
    }

    fun relax() {
        for (i in 0..nodeindex - 1) {
            var fx = 0.0
            var fy = 0.0

            for (j in 0..nodeindex - 1) {

                val distX = ((nodes[i].x + nodes[i].width / 2 - (nodes[j].x + nodes[j].width / 2)).toInt()).toDouble()
                val distY = ((nodes[i].y + nodes[i].height / 2 - (nodes[j].y + nodes[j].height / 2)).toInt()).toDouble()
                var rsq = distX * distX + distY * distY
                val rsq_round = rsq.toInt() * 100
                rsq = (rsq_round / 100).toDouble()

                var coulombDistX = COULOMB * distX
                var coulombDistY = COULOMB * distY
                val coulombDistRoundX = coulombDistX.toInt() * 100
                val coulombDistRoundY = coulombDistY.toInt() * 100
                coulombDistX = (coulombDistRoundX / 100).toDouble()
                coulombDistY = (coulombDistRoundY / 100).toDouble()

                if (rsq != 0.0 && Math.sqrt(rsq) < distance) {
                    fx += coulombDistX / rsq
                    fy += coulombDistY / rsq
                }
            }

            //gravity : node - central
            var distXC = 0.0
            var distYC = 0.0
            distXC = displayWidth / 2 - (nodes[i].x + nodes[i].width / 2)
            distYC = displayHeight / 2 - (nodes[i].y + nodes[i].height / 2)

            fx += gravity * distXC
            fy += gravity * distYC


            //node in group : from - to
            for (j in 0..nedges - 1 - 1) {
                var distX = 0.0
                var distY = 0.0
                if (edges[j].group) {
                    if (i == edges[j].from) {
                        distX = nodes[edges[j].to].x - nodes[i].x
                        distY = nodes[edges[j].to].y - nodes[i].y

                    } else if (i == edges[j].to) {
                        distX = nodes[edges[j].from].x - nodes[i].x
                        distY = nodes[edges[j].from].y - nodes[i].y
                    }
                }
                fx += bounce * distX
                fy += bounce * distY
            }

            nodes[i].dx = (nodes[i].dx + fx) * ATTENUATION
            nodes[i].dy = (nodes[i].dy + fy) * ATTENUATION


            nodes[i].x += nodes[i].dx
            nodes[i].y += nodes[i].dy

        }
    }

    private fun initNodes() {
        nodeindex = 0
        nodes = ArrayList<Node>()
        nodeNameArray.clear()
        nodesList.clear()
    }

    private fun initEdges(){
        nedges = 0
        edges = ArrayList<Edge>()
    }
}
