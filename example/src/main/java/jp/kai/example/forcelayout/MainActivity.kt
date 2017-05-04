package jp.kai.example.forcelayout

import android.app.Activity
import android.os.Bundle
import android.util.Pair
import android.widget.SeekBar
import jp.kai.forcelayout.Forcelayout
import org.jetbrains.anko.button
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.verticalLayout
import java.util.ArrayList
import java.util.Arrays

/**
 * Created by kai on 2016/09/03.
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bane = Forcelayout(applicationContext)

        /** set nodes */
        val nodes = ArrayList <Pair<String,Int>>()

        nodes.add(Pair("neko", R.drawable.a))
        nodes.add(Pair("nyanko", R.drawable.b))
        nodes.add(Pair("hoge", R.drawable.c))
        nodes.add(Pair("neko1", R.drawable.d))
        nodes.add(Pair("neko2", R.drawable.d))
        nodes.add(Pair("neko3", R.drawable.d))
        nodes.add(Pair("neko4", R.drawable.d))
        nodes.add(Pair("neko5", R.drawable.d))
        nodes.add(Pair("neko6", R.drawable.d))
        nodes.add(Pair("neko7", R.drawable.e))
        nodes.add(Pair("neko8", R.drawable.e))
        nodes.add(Pair("neko9", R.drawable.f))
        nodes.add(Pair("neko10", R.drawable.f))
        nodes.add(Pair("neko11", R.drawable.g))
        nodes.add(Pair("neko12", R.drawable.g))

        //set links
        val links = Arrays.asList("neko5-neko8", "neko-neko4", "neko1-neko2", "neko2-neko3", "neko3-neko1", "neko5-neko10", "neko3-neko11", "neko3-neko12", "neko3-nyanko", "hoge-neko7")

        bane.with()
                .linkStrength(0.09)
                .distance(200)
                .gravity(0.04)
                .nodes(nodes)
                .links(links)

        verticalLayout {

            button {
                text = "change links"
                setOnClickListener {
                    //set links
                    val links = Arrays.asList("neko-nyanko", "neko1-neko2", "neko5-neko8", "neko-neko4", "neko2-neko5")

                    bane.with().linkStrength(0.09).gravity(0.04).distance(200).links(links)
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
