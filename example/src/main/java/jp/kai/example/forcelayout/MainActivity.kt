package jp.kai.example.forcelayout

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import jp.kai.forcelayout.Forcelayout
import org.jetbrains.anko.button
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.verticalLayout
import java.util.Arrays
import android.util.Pair

/**
 * Created by kai on 2016/09/03.
 * Usage for Kotlin
 */

class MainActivity : Activity() {
    private var isFlip: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        val force = Forcelayout(this)

        example1(force)

        verticalLayout {
            button {
                text = "change examples"

                setOnClickListener {
                    if(isFlip){
                        example1(force)
                        isFlip = false
                    }else {
                        example2(force)
                        isFlip = true
                    }
                }
            }

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
                        force.with().gravity((progress.toFloat() / 1000).toDouble()).start()

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

    private fun example1(force: Forcelayout){
        /** set nodes */
        val nodes: ArrayList<Pair<String, Int>> = ArrayList<Pair<String, Int>>()

        nodes.add(Pair("cat_a1", R.drawable.a))
        nodes.add(Pair("cat_a2", R.drawable.a))
        nodes.add(Pair("cat_a3", R.drawable.a))
        nodes.add(Pair("cat_b1", R.drawable.b))
        nodes.add(Pair("cat_c1", R.drawable.c))
        nodes.add(Pair("cat_d1", R.drawable.d))
        nodes.add(Pair("cat_d2", R.drawable.d))
        nodes.add(Pair("cat_e1", R.drawable.e))
        nodes.add(Pair("cat_f1", R.drawable.f))

        /** set links */
        val links: List<String> = Arrays.asList("cat_d1-cat_d2","cat_a1-cat_a3","cat_a2-cat_a3","cat_a1-cat_a2")

        force.with()
                .distance(350) /** distance between each nodes */
                .gravity(0.04) /** gravitation from center of view */
                .friction(0.02) /** value of gravitation between each nodes */
                .size(130) /** node width */
                .nodes(nodes) /** set nodes */
                .links(links) /** set links */
                .start() /** start animation */
    }

    private fun example2(force: Forcelayout){
        /** set nodes */
        val nodes: Array<String> = arrayOf( "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n","o","p","q","r")

        /** set links */
        val links: List<String> = Arrays.asList("a-b","c-d","d-g","r-o")

        force.node()
                .size(100)
                .style(Color.argb(100,200,30,50))

        force.link()
                .style(Color.argb(60,50,30,200), 5.0f)

        force.with()
                .distance(350) /** distance between each nodes */
                .gravity(0.04) /** gravitation from center of view */
                .friction(0.02) /** value of gravitation between each nodes */
                .nodes(nodes) /** set nodes */
                .links(links) /** set links */
                .start() /** start animation */
    }
}