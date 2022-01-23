package com.example.mycloudmusic.base

import android.os.Bundle
import androidx.fragment.app.Fragment


/**
 * Used 基础碎片类
 */
open class BaseFragment : Fragment() {
    //传递过来的参数Bundle，供子类使用
    private var args: Bundle? = null


    /**
     * 初始创建Fragment对象时调用
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = arguments
    }

    companion object {
        /**
         * 创建fragment的静态方法，方便传递参数
         * @param args 传递的参数
         * @return
         */
        fun <T : Fragment> getInstance(clazz: Class<*>, args: Bundle?): T {
            lateinit var mFragment: T
            try {
                mFragment = clazz.newInstance() as T
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            mFragment.arguments = args
            return mFragment
        }
    }
}
