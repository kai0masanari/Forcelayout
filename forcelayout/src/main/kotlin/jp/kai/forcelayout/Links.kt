package jp.kai.forcelayout

import java.util.ArrayList

/**
 * Created by kai on 2017/05/12.
 */

class Links : ArrayList<Links.LinkPair>() {

    class LinkPair(private val parent: String, private val child: String) {

        fun parent(): String {
            return parent
        }

        fun child(): String {
            return child
        }
    }
}