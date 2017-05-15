package jp.kai.example.forcelayout

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import jp.kai.forcelayout.Forcelayout
import jp.kai.forcelayout.Links
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.verticalLayout

/**
 * Created by kai on 2017/05/14.
 */

class SubActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forceSub = Forcelayout(this)

        /** set nodes */
        val nodes: Array<String> = arrayOf( "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n","o","p","q","r")

        /** set links */
        val links: Links = Links()
        links.add(Links.LinkPair("a", "b"))
        links.add(Links.LinkPair("b", "c"))
        links.add(Links.LinkPair("a", "c"))
        links.add(Links.LinkPair("d", "e"))
        links.add(Links.LinkPair("d", "g"))
        links.add(Links.LinkPair("d", "l"))
        links.add(Links.LinkPair("l", "k"))
        links.add(Links.LinkPair("l", "m"))
        links.add(Links.LinkPair("l", "n"))
        links.add(Links.LinkPair("h", "i"))
        links.add(Links.LinkPair("o", "p"))
        links.add(Links.LinkPair("p", "q"))
        links.add(Links.LinkPair("q", "r"))
        links.add(Links.LinkPair("r", "o"))

        forceSub.node()
                .size(100)
                .style(Color.argb(100,200,30,50))

        forceSub.link()
                .style(Color.argb(60,50,30,200), 5.0f)


        forceSub.with()
                .distance(350) /** distance between each nodes */
                .gravity(0.04) /** gravitation from center of view */
                .friction(0.02) /** value of gravitation between each nodes */
                .nodes(nodes) /** set nodes */
                .links(links) /** set links */
                .start() /** start animation */

        verticalLayout {
            seekBar {
                max = 100
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        forceSub.with().gravity((progress.toFloat() / 1000).toDouble()).start()

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }
                })
            }
            addView(forceSub)
        }
    }
}
