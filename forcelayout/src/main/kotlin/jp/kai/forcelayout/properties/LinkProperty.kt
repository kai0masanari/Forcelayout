package jp.kai.forcelayout.properties

import java.lang.Integer.parseInt

/**
 * Created by kai on 2017/05/14.
 */

class LinkProperty {
    fun prepare(): LinkProperty {
        return this
    }

    fun syle(color:String ,width: Float): LinkProperty {
        GraphStyle.linkColor = parseInt(color, 16)
        GraphStyle.linkWidth = width

        return this
    }

    fun syle(color:Int ,width: Float): LinkProperty {
        GraphStyle.linkColor = color
        GraphStyle.linkWidth = width

        return this
    }
}