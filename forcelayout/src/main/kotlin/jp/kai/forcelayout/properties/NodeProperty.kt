package jp.kai.forcelayout.properties

import java.lang.Integer.parseInt

/**
 * Created by kai on 2017/05/14.
 */

class NodeProperty {
    fun prepare(): NodeProperty {
        return this
    }

    fun size(width: Int): NodeProperty{
        GraphStyle.nodesWidth = width

        return this
    }

    fun style(color: String): NodeProperty {
        GraphStyle.nodeColor = parseInt(color, 16)

        return this
    }

    fun style(color: Int): NodeProperty {
        GraphStyle.nodeColor = color

        return this
    }
}