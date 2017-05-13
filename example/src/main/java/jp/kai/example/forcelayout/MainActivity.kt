package jp.kai.example.forcelayout

import android.app.Activity
import android.os.Bundle
import android.widget.SeekBar
import jp.kai.forcelayout.Forcelayout
import jp.kai.forcelayout.Links
import jp.kai.forcelayout.Links.LinkPair
import jp.kai.forcelayout.Nodes
import jp.kai.forcelayout.Nodes.NodePair
import org.jetbrains.anko.button
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.verticalLayout
import java.util.Arrays

/**
 * Created by kai on 2016/09/03.
 * Usage for Kotlin
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bane = Forcelayout(applicationContext)

        /** set nodes */
        val nodes: Nodes = Nodes()
        nodes.add(NodePair("neko", R.drawable.a))
        nodes.add(NodePair("nyanko", R.drawable.b))
        nodes.add(NodePair("hoge", R.drawable.c))
        nodes.add(NodePair("neko1", R.drawable.d))
        nodes.add(NodePair("neko2", R.drawable.d))
        nodes.add(NodePair("neko3", R.drawable.d))
        nodes.add(NodePair("neko4", R.drawable.d))
        nodes.add(NodePair("neko5", R.drawable.d))
        nodes.add(NodePair("neko6", R.drawable.d))
        nodes.add(NodePair("neko7", R.drawable.e))
        nodes.add(NodePair("neko8", R.drawable.e))
        nodes.add(NodePair("neko9", R.drawable.f))
        nodes.add(NodePair("neko10", R.drawable.f))
        nodes.add(NodePair("neko11", R.drawable.g))
        nodes.add(NodePair("neko12", R.drawable.g))

        /** set links */
        val links: Links = Links()
        links.add(LinkPair("neko5", "neko8"))
        links.add(LinkPair("neko", "neko4"))
        links.add(LinkPair("neko1", "neko2"))
        links.add(LinkPair("neko2", "neko3"))
        links.add(LinkPair("neko3", "neko1"))
        links.add(LinkPair("neko5", "neko10"))

        bane.with()
                .distance(350) /** distance between each nodes */
                .gravity(0.04) /** gravitation from center of view */
                .friction(0.02) /** value of gravitation between each nodes */
                .nodeSize(130) /** node width */
                .nodes(nodes) /** set nodes */
                .links(links) /** set links */
                .start() /** start animation */

        verticalLayout {
            button {
                text = "change links"
                setOnClickListener {
                    /** set links */
                    val links = Arrays.asList("neko-nyanko", "neko1-neko2", "neko5-neko8", "neko-neko4", "neko2-neko5")

                    bane.with()
                            .friction(0.02)
                            .gravity(0.08)
                            .links(links)
                            .start()
                }
            }

            seekBar {
                max = 100
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        bane.with().gravity((progress.toFloat() / 1000).toDouble())

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }
                })
            }
            addView(bane)
        }
    }
}
