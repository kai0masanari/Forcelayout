package jp.kai.example.forcelayout

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import jp.kai.forcelayout.Forcelayout
import android.util.Pair
import java.util.ArrayList
import java.util.Arrays

/**
 * Created by kai on 2016/09/03.
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val _bane = Forcelayout(applicationContext)

        //set nodes
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


        val button1 = Button(this)
        button1.setOnClickListener {
            //set links
            val links = Arrays.asList("neko-nyanko", "neko1-neko2", "neko5-neko8", "neko-neko4", "neko2-neko5")

            _bane.with(applicationContext).linkStrength(0.09).gravity(0.04).distance(200).links(links)
        }
        button1.text = "change links"


        val seek1 = SeekBar(this)
        seek1.max = 100
        seek1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                _bane.with(applicationContext).gravity((progress.toFloat() / 1000).toDouble())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })


        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(button1)
        layout.addView(seek1)
        layout.addView(_bane)
        setContentView(layout)


        //set links
        val links = Arrays.asList("neko5-neko8", "neko-neko4", "neko1-neko2", "neko2-neko3", "neko3-neko1", "neko5-neko10", "neko3-neko11", "neko3-neko12", "neko3-nyanko", "hoge-neko7")

        _bane.with(this)
                .linkStrength(0.09)
                .distance(200)
                .gravity(0.04)
                .nodes(nodes)
                .links(links)


    }


    override fun onStart() {
        super.onStart()
    }

}
