package jp.kai.example.forcelayout

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import jp.kai.forcelayout.Forcelayout
import jp.kai.forcelayout.Links
import jp.kai.forcelayout.Links.LinkPair
import jp.kai.forcelayout.Nodes
import jp.kai.forcelayout.Nodes.NodePair
import jp.kai.forcelayout.properties.GraphStyle
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

        val force = Forcelayout(applicationContext)

        /** set nodes */
//        val nodes: Nodes = Nodes()
//        nodes.add(NodePair("cat_a1", R.drawable.a))
//        nodes.add(NodePair("cat_a2", R.drawable.a))
//        nodes.add(NodePair("cat_a3", R.drawable.a))
//        nodes.add(NodePair("cat_b1", R.drawable.b))
//        nodes.add(NodePair("cat_c1", R.drawable.c))
//        nodes.add(NodePair("cat_d1", R.drawable.d))
//        nodes.add(NodePair("cat_d2", R.drawable.d))
//        nodes.add(NodePair("cat_e1", R.drawable.e))
//        nodes.add(NodePair("cat_f1", R.drawable.f))
        /** set nodes */
        val nodes: Array<String> = arrayOf("hoge","hogehoge","huga","hugahuga")


        /** set links */
        val links: Links = Links()
//        links.add(LinkPair("cat_a1", "cat_a2"))
//        links.add(LinkPair("cat_a2", "cat_a3"))
//        links.add(LinkPair("cat_a1", "cat_a3"))
//        links.add(LinkPair("cat_d1", "cat_d2"))
        links.add(LinkPair("hoge", "hogehoge"))
        links.add(LinkPair("huga", "hugahuga"))

        force.node()
                .size(300)
                .style(Color.BLUE)


        force.with()
                .distance(350) /** distance between each nodes */
                .gravity(0.04) /** gravitation from center of view */
                .friction(0.02) /** value of gravitation between each nodes */
//                .size(130) /** node width */
                .nodes(nodes) /** set nodes */
                .links(links) /** set links */
                .start() /** start animation */

        verticalLayout {
            button {
                text = "change links"
                setOnClickListener {
                    /** set links */
                    val links = Arrays.asList("cat_a1-cat_a2", "cat_a2-cat_a3", "cat_a3-cat_d1", "cat_d1-cat_d2", "cat_d2-cat_e1")

                    force.with()
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
                        force.with().gravity((progress.toFloat() / 1000).toDouble())

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }
                })
            }
            addView(force)
        }
    }
}
