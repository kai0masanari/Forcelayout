package jp.kai.example.forcelayout

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar

import java.util.Arrays
import java.util.HashMap

import jp.kai.forcelayout.Forcelayout

/**
 * Created by kai on 2016/09/03.
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val _bane = Forcelayout(applicationContext)

        //set nodes
        val nodes = Pair()

        nodes.put("neko", R.drawable.a)
        nodes.put("nyanko", R.drawable.b)
        nodes.put("hoge", R.drawable.c)
        nodes.put("neko1", R.drawable.d)
        nodes.put("neko2", R.drawable.d)
        nodes.put("neko3", R.drawable.d)
        nodes.put("neko4", R.drawable.d)
        nodes.put("neko5", R.drawable.d)
        nodes.put("neko6", R.drawable.d)
        nodes.put("neko7", R.drawable.e)
        nodes.put("neko8", R.drawable.e)
        nodes.put("neko9", R.drawable.f)
        nodes.put("neko10", R.drawable.f)
        nodes.put("neko11", R.drawable.g)
        nodes.put("neko12", R.drawable.g)


        val button1 = Button(this)
        button1.setOnClickListener {
            //set links
            val links = Arrays.asList("neko-nyanko", "neko1-neko2", "neko5-neko8", "neko-neko4", "neko2-neko5")

            _bane.with(applicationContext).linkStrength(0.09).gravity(0.04).distance(200).links(links)
        }
        button1.text = "change links and hide labels"

        val button2 = Button(this)
        button2.setOnClickListener {
            //set links
            val links = Arrays.asList("neko-nyanko", "neko1-neko2", "neko5-neko8", "neko-neko4", "neko2-neko5")

            _bane.nodes().nodesize(150).nodes(nodes).drawLable(true)
            _bane.link().links(links).style(10, Color.RED).drawStroke(true)

            _bane.with(applicationContext).linkStrength(0.1).gravity(0.3).distance(500)
        }
        button2.text = "change Node size"

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
        layout.addView(button2)
        layout.addView(seek1)
        layout.addView(_bane)
        setContentView(layout)


        //set links
        val links = Arrays.asList("neko5-neko8", "neko-neko4", "neko1-neko2", "neko2-neko3", "neko3-neko1", "neko5-neko10", "neko3-neko11", "neko3-neko12", "neko3-nyanko", "hoge-neko7")

        _bane.with(this)
                .nodesize(200)
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
