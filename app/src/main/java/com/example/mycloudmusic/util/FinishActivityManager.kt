package com.example.mycloudmusic.util

import android.app.Activity
import android.os.Process
import java.lang.Exception
import java.util.*
import kotlin.system.exitProcess


/**
 * 利用栈机制来完成activity的管理
 */

class FinishActivityManager private constructor() {
    private var activityList: MutableList<Activity>? = null

    /**
     *
     *
     * 添加Activity到集合中
     */
    fun addActivity(activity: Activity) {
        if (activityList == null) {
            activityList = LinkedList()
        }
        activityList!!.add(activity)
    }

    /**
     * 关闭指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activityList != null && activity != null && activityList!!.contains(activity)) {
            //使用迭代器安全删除
            val it = activityList!!.iterator()
            while (it.hasNext()) {
                val temp = it.next()
                // 清理掉已经释放的activity
                if (temp === activity) {
                    it.remove()
                }
            }
            activity.finish()
        }
    }

    /**
     * 关闭指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        if (activityList != null) {
            // 使用迭代器安全删除
            val it = activityList!!.iterator()
            while (it.hasNext()) {
                val activity = it.next()
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove()
                    continue
                }
                if (activity.javaClass == cls) {
                    it.remove()
                    activity.finish()
                }
            }
        }
    }

    /**
     * 关闭所有的Activity
     */
    fun finishAllActivity() {
        if (activityList != null) {
            val it: Iterator<Activity> = activityList!!.iterator()
            while (it.hasNext()) {
                val activity = it.next()
                activity.finish()
            }
            activityList!!.clear()
        }
    }

    /**
     * 退出应用程序
     */
    fun exitApp() {
        try {
            finishAllActivity()
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //单例的任务栈
    companion object {
        private var sManager: FinishActivityManager? = null
        val manager: FinishActivityManager?
            get() {
                if (sManager == null) {
                    synchronized(FinishActivityManager::class.java) {
                        if (sManager == null) {
                            sManager = FinishActivityManager()
                        }
                    }
                }
                return sManager
            }
    }
}