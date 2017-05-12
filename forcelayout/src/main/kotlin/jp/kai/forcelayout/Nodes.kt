package jp.kai.forcelayout

import java.util.ArrayList

/**
 * Created by kai on 2017/05/12.
 */

class Nodes: ArrayList<Nodes.NodePair>() {
    class NodePair(private val label: String, private val resource :Int){

        fun getLabel(): String{
            return label
        }

        fun getResource(): Int{
            return resource
        }
    }
}
