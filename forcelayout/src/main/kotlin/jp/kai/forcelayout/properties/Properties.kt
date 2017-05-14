package jp.kai.forcelayout.properties

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Pair
import jp.kai.forcelayout.ATTENUATION
import jp.kai.forcelayout.COULOMB
import jp.kai.forcelayout.Links
import jp.kai.forcelayout.Nodes
import jp.kai.forcelayout.Nodes.NodePair
import jp.kai.forcelayout.properties.Util.Companion.getCroppedBitmap
import jp.kai.forcelayout.properties.Util.Companion.getDisplayMetrics
import jp.kai.forcelayout.properties.Util.Companion.resizeBitmap
import java.util.ArrayList

/**
 * Created by kai on 2017/05/01.
 * Builder Class
 */

class Properties(private val mContext: Context){
    var isReady: Boolean = false

    /** node's and link's List */
    internal var nodes = ArrayList<Node>()
    internal var edges = ArrayList<Edge>()
    var nodeindex: Int = 0
    var nedges: Int = 0
    private var nodeNameArray: Array<String?> = arrayOfNulls(0)
    var nodesList = ArrayList<Pair<String, Bitmap>>()

    /** draw area */
    private var displayWidth: Float = 0f
    private var displayHeight: Float = 0f
    private var drawAreaWidth: Float = 0f /** draw-area means screen-size */
    private var drawAreaHeight: Float = 0f

    /** spring-like force */
    private var distance: Int = 300
    private var bounce: Double = 0.08
    private var gravity: Double = 0.04

    /** node style */
    private var nodesWidth: Int = 150
    private val roundSize = 5
    private var reduction: Int = 30

    fun prepare(): Properties {
        val mDisplay = getDisplayMetrics(mContext)
        displayWidth = mDisplay.width.toFloat()
        displayHeight = mDisplay.height.toFloat()

        return this
    }

    fun nodeSize(nodesWidth: Int): Properties {
        this.nodesWidth = nodesWidth

        return this
    }

    fun nodes(nodemaps: ArrayList<Pair<String, Int>>): Properties {
        initNodes()

        val resource = mContext.resources
        val iterator :Iterator<Pair<String, Int>> = nodemaps.iterator()

        nodeNameArray = arrayOfNulls(nodemaps.size)

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
            reduction = bitmapWidth / nodesWidth
            if (reduction != 0) {
                bmfOptions.inSampleSize = reduction
            }

            var bitmap = BitmapFactory.decodeResource(resource, pair.second, bmfOptions)
            var imgheight = bmfOptions.outHeight
            var imgwidth = bmfOptions.outWidth

            if (imgwidth != nodesWidth) {
                bitmap = resizeBitmap(bitmap, nodesWidth)

                imgheight = bitmap.height
                imgwidth = bitmap.width
            }

            drawAreaWidth = displayWidth - imgwidth
            drawAreaHeight = displayHeight - imgheight

            addNode(pair.first, imgwidth, imgheight)

            nodeNameArray[nodeindex-1] = pair.first

            nodesList.add(Pair(pair.first, getCroppedBitmap(bitmap, roundSize)))
        }

        for(i in (nodemaps.size - 1)..0){
            nodemaps.removeAt(i)
        }
        nodemaps.clear()

        return this
    }

    fun nodes(nodemaps: Nodes): Properties {
        initNodes()

        val resource = mContext.resources
        val iterator :Iterator<NodePair> = nodemaps.iterator()

        nodeNameArray = arrayOfNulls(nodemaps.size)

        while (iterator.hasNext()){
            /** Node List */
            val pair: NodePair = iterator.next()

            /** get image properties */
            val imageOptions = BitmapFactory.Options()
            imageOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
            imageOptions.inJustDecodeBounds = true
            BitmapFactory.decodeResource(resource, pair.getResource(), imageOptions)
            val bitmapWidth = imageOptions.outWidth
            val bmfOptions = BitmapFactory.Options()

            /** resize image */
            reduction = bitmapWidth / nodesWidth
            if (reduction != 0) {
                bmfOptions.inSampleSize = reduction
            }

            var bitmap = BitmapFactory.decodeResource(resource, pair.getResource(), bmfOptions)
            var imgheight = bmfOptions.outHeight
            var imgwidth = bmfOptions.outWidth

            if (imgwidth != nodesWidth) {
                bitmap = resizeBitmap(bitmap, nodesWidth)

                imgheight = bitmap.height
                imgwidth = bitmap.width
            }

            drawAreaWidth = displayWidth - imgwidth
            drawAreaHeight = displayHeight - imgheight

            addNode(pair.getLabel(), imgwidth, imgheight)

            nodeNameArray[nodeindex-1] = pair.getLabel()

            nodesList.add(Pair(pair.getLabel(), getCroppedBitmap(bitmap, roundSize)))
        }

        for(i in (nodemaps.size - 1)..0){
            nodemaps.removeAt(i)
        }
        nodemaps.clear()

        return this
    }

    fun nodes(nodemaps: Array<String>): Properties {
        nodeNameArray = arrayOfNulls(nodemaps.size)

        for (i in 0..nodemaps.size ){
            addNode(nodemaps[i], nodesWidth, nodesWidth)
            nodeNameArray[i] = nodemaps[i]
        }

        drawAreaWidth = displayWidth - nodesWidth
        drawAreaHeight = displayHeight - nodesWidth

        return this
    }

    fun links(linkMaps: List<String>): Properties {
        initEdges()

        for (i in 0..nodeindex -1) {
            for (j in 0..nodeindex - 1) {
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

        return this
    }

    fun links(linkMaps: Links): Properties {
        initEdges()

        for (i in 0..nodeindex -1) {
            for (j in 0..nodeindex - 1) {
                if (i != j) {
                    addEdge(i, j)
                }
            }
        }

        for (k in 0..linkMaps.size - 1) {
            val pair = linkMaps[k]

            for (i in 0..nedges - 1) {
                if (edges[i].from == nodeNameArray.indexOf(pair.child()) && edges[i].to == nodeNameArray.indexOf(pair.parent()) || edges[i].to == nodeNameArray.indexOf(pair.child()) && edges[i].from == nodeNameArray.indexOf(pair.parent())) {
                    edges[i].group = true
                }
            }
        }

        for(i in (linkMaps.size - 1)..0){
            linkMaps.removeAt(i)
        }
        linkMaps.clear()

        return this
    }

    fun friction(bounce: Double): Properties {
        this.bounce = bounce

        return this
    }

    fun distance(distance: Int): Properties {
        this.distance = distance

        return this
    }

    fun gravity(gravity: Double): Properties {
        this.gravity = gravity

        return this
    }

    fun start(){
        isReady = true
    }

    private fun addNode(lbl: String, width: Int, height: Int) {
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

    private fun addEdge(from: Int, to: Int) {
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

            /** calculate gravity */
            val distXC = displayWidth / 2 - (nodes[i].x + nodes[i].width / 2)
            val distYC = displayHeight / 2 - (nodes[i].y + nodes[i].height / 2)

            fx += gravity * distXC
            fy += gravity * distYC

            /** calculate spring like force between from and to */
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
        nodeNameArray = arrayOfNulls(0)
        nodesList.clear()
    }

    private fun initEdges(){
        nedges = 0
        edges = ArrayList<Edge>()
    }
}
