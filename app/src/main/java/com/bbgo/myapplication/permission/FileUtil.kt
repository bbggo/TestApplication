package com.bbgo.myapplication.permission

import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import com.bbgo.myapplication.RootApplication


class FileUtil {

    companion object {

        /**
         * 外置存储卡的路径
         * /storage/emulated/0
         * 创建文件是不允许的，会crash
         */
        fun getExternalStorePath(): String? {
            return if (isExistExternalStore()) {
                Environment.getExternalStorageDirectory().absolutePath
            } else null
        }

        /**
         * 内置存储卡的文件路径
         * /data/user/0/com.bbgo.myapplication/files
         * 路径会映射到/data/data/com.bbgo.myapplication/files
         */
        fun getInternalStorePath(): String {
            return RootApplication.getContext().filesDir.absolutePath
        }

        /**
         * 内置存储卡的缓存路径
         * /data/user/0/com.bbgo.myapplication/cache
         * 路径会映射到/data/data/com.bbgo.myapplication/cache
         */
        fun getInternalStoreCachePath(): String {
            return RootApplication.getContext().cacheDir.absolutePath
        }

        /**
         * 外置存储卡文件的路径
         * /storage/emulated/0/Android/data/com.bbgo.myapplication/files
         * 可直接操作
         */
        fun getExternalStoreFilePath(): String? {
            return if (isExistExternalStore()) {
                return RootApplication.getContext().getExternalFilesDir(null)?.absolutePath
            } else null
        }

        /**
         * 外置存储卡缓存的路径
         * /storage/emulated/0/Android/data/com.bbgo.myapplication/cache
         * 可直接操作
         */
        fun getExternalStoreCachePath(): String? {
            return if (isExistExternalStore()) {
                return RootApplication.getContext().externalCacheDir?.absolutePath
            } else null
        }

        /**
         * 是否有外存卡
         */
        private fun isExistExternalStore(): Boolean {
            return Environment.getExternalStorageState() == MEDIA_MOUNTED
        }
    }
}