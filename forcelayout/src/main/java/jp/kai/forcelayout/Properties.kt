package jp.kai.forcelayout

import android.content.Context
import android.view.Display
import android.view.WindowManager

/**
 * Created by kai on 2017/05/01.
 * Builder Class
 */

//FIX-ME relax()を別のクラスに切り分けたい
class Properties(mContext: Context){
    private val mContext: Context = mContext

    private var display_width: Float = 0f
    private var display_height: Float = 0f

    private var distance: Int = 300
    private var gravity: Double = 0.04

    fun prepare(): Properties{
        val mDisplay = getDisplayMetrics(mContext)
        display_width = mDisplay.getWidth().toFloat()
        display_height = mDisplay.getHeight().toFloat()

        return this
    }

    private fun nodes(): Properties{
        //TODO ここにノード処理を
        return this
    }

    private fun links(): Properties{
        //TODO ここにはリンク処理
        return this
    }

    private fun linkStrength(): Properties{
        return this
    }

    private fun distance(): Properties{
       return this
    }

    private fun gravity(): Properties{
        return this
    }

    //TODO スタイル等の情報はまた別のBuilderを作るべきかもしれない
    private fun nodeSize(): Properties{
        return this
    }

    private fun getDisplayMetrics(context: Context): Display {
        val winMan = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val disp = winMan.defaultDisplay
        return disp
    }

}
