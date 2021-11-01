package com.bbgo.myapplication.permission

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bbgo.myapplication.R
import pub.devrel.easypermissions.EasyPermissions

import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.PermissionRequest
import com.bbgo.myapplication.MainActivity

import com.hjq.permissions.XXPermissions

import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import org.bouncycastle.util.encoders.Hex
import java.io.*
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.util.*


class XXPermissionActivity : AppCompatActivity() {

    private val TAG = "PermissionActivity"

    companion object {
        const val PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_test).text = "xxpermission"

        Log.d(TAG, "ExternalStorePath = ${FileUtil.getExternalStorePath()}")
        Log.d(TAG, "ExternalStoreCachePath = ${FileUtil.getExternalStoreCachePath()}")
        Log.d(TAG, "ExternalStoreFilePath = ${FileUtil.getExternalStoreFilePath()}")
        Log.d(TAG, "InternalStorePath = ${FileUtil.getInternalStorePath()}")
        Log.d(TAG, "InternalStoreCachePath = ${FileUtil.getInternalStoreCachePath()}")
        Log.d(TAG, "getExternalStorageState = ${Environment.getExternalStorageState()}")


        /*val file = File(FileUtil.getInternalStorePath() + File.separator + "333.txt")
        file.createNewFile()

        val file2 = File(FileUtil.getExternalStoreFilePath() + File.separator + "333.txt")
        if (file2.exists()) {
            Log.d(TAG, "文件存在")
        } else {
            file2.createNewFile()
        }*/

//        insertKeyValue("key3")
//        queryKeyChainKeyValue("key3")

//        deleteKeyChainKeyValue("key3")

//        getImages()

//        requestPermission()

        generateRandom(8)
    }

    private fun generateRandom(length: Int) {
        val random = Random()

        for (i in 0..80) {
//            val intValue = random.nextInt(Int.MAX_VALUE)
//            Log.d("fff", "intValue=$intValue")
//            Log.d("fff", "nextInt=${Integer.toHexString(intValue)}")
            val build = StringBuilder()
            for (index in 0..(length * 2)) {
                build.append(Integer.toHexString(random.nextInt(Int.MAX_VALUE)))
            }
            val value = build.substring(0, length * 2).toString()
            Log.d("fff", "value=$value")
//            Log.d("fff", "hex=$hex")
        }

    }

    private fun getImages() {
        kotlin.runCatching {

            val path = FileUtil.getExternalStorePath() + File.separator +
                    Environment.DIRECTORY_DOWNLOADS + File.separator +
                    "data" + File.separator + "encypt.txt"
            val file = File(path)
            if (file.exists()) {
                val fileUri = FileProvider.getUriForFile(
                    this,
                    "${this.packageName}.fileprovider",
                    File(path)
                )

                val inputStream = contentResolver.openInputStream(fileUri)
                /*val bis = BufferedInputStream(inputStream)
                val reader = bis.bufferedReader(Charset.defaultCharset())
                val list = reader.readLines()*/
                val result = inputStream?.readBytes()?.let { String(it, Charset.defaultCharset()) }
                inputStream?.close()

                val uuid = UUID.randomUUID().toString().replace("-", "").uppercase(Locale.getDefault())
                val outputStream = contentResolver.openOutputStream(fileUri)
                val r = "$result\rkey2:$uuid"
                outputStream?.write(r.toByteArray())
                outputStream?.close()
                return@runCatching
            }
            val rootUri = MediaStore.Files.getContentUri("external")
            val contentValues = ContentValues()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/data") // 可以不要这一行，如果不要，默认是在Download文件夹目录下创建hello.txt
            }
            contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "encypt.txt")
            val insertUri = contentResolver.insert(rootUri, contentValues)!!

            val outputStream = contentResolver.openOutputStream(insertUri)
            val bos = BufferedOutputStream(outputStream)
            val uuid = UUID.randomUUID().toString().replace("-", "").uppercase(Locale.getDefault())
            val result = "key:$uuid"
            bos.write(result.toByteArray())
            bos.close()
        }.onFailure {
            Log.d(TAG, "error = $it")
        }
    }

    fun deleteKeyChainKeyValue(key: String) {
        kotlin.runCatching {
            val fileUri = getFileUri() ?: return@runCatching
            val inputStream = contentResolver.openInputStream(fileUri)
            val bis = BufferedInputStream(inputStream)
            val reader = bis.bufferedReader(Charset.defaultCharset())
            val list = reader.readLines()
            val mutableList = mutableListOf<String>()
            mutableList.addAll(list)
            val iterator = mutableList.iterator()
            val inputContent = StringBuilder()
            while (iterator.hasNext()) {
                val result = iterator.next()
                if (result.startsWith(key)) {
                    iterator.remove()
                    continue
                }
                inputContent.append(result).append("\r")
            }
            val outputStream = contentResolver.openOutputStream(fileUri)
            outputStream?.write(inputContent.toString().toByteArray())
            outputStream?.close()
        }.onFailure {
            Log.d("EncrptModule", "error = $it")
        }
    }

    fun queryKeyChainKeyValue(key: String) {
        kotlin.runCatching {
            val fileUri = getFileUri() ?: return@runCatching
            val inputStream = contentResolver.openInputStream(fileUri)
            val bis = BufferedInputStream(inputStream)
            val reader = bis.bufferedReader(Charset.defaultCharset())
            val list = reader.readLines()
            var result = ""
            for(i in list.indices) {
                result = list[i]
                if (result.startsWith(key)) {
                    break
                }
            }
            reader.close()
            bis.close()
            inputStream?.close()
        }.onFailure {
            Log.d("EncrptModule", "error = $it")
        }
    }

    private fun insertKeyValue(key: String, value:String = "") {
        kotlin.runCatching {
            val fileUri = getFileUri() ?: kotlin.run {
                val rootUri = MediaStore.Files.getContentUri("external")
                val contentValues = ContentValues()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/data") // 可以不要这一行，如果不要，默认是在Download文件夹目录下创建hello.txt
                }
                contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "encypt.txt")
                val insertUri = contentResolver.insert(rootUri, contentValues)!!

                val outputStream = contentResolver.openOutputStream(insertUri)
                val uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.getDefault())
                val result = "$key:$uuid"
                outputStream?.write(result.toByteArray())
                outputStream?.close()
            }
            val inputStream = contentResolver.openInputStream(fileUri as Uri)
            val fileContent = inputStream?.readBytes()?.let { String(it, Charset.defaultCharset()) }
            inputStream?.close()

            val uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.getDefault())
            val outputStream = contentResolver.openOutputStream(fileUri)
            val result = if (value.isEmpty()) {
                "$fileContent\r$key:$uuid"
            } else {
                "$fileContent\r$key:$value"
            }
            outputStream?.write(result.toByteArray())
            outputStream?.close()
        }.onFailure {
            Log.d("EncrptModule", "error = $it")
        }
    }

    private fun getFileUri(): Uri? {
        val path = FileUtil.getExternalStorePath()?.let {
            it + File.separator +
                    Environment.DIRECTORY_DOWNLOADS + File.separator +
                    "data" + File.separator + "encypt.txt"
        } ?: return null

        val file = File(path)
        if (!file.exists()) {
            return null
        }
        return FileProvider.getUriForFile(
            this,
            "${this.packageName}.fileprovider",
            File(path)
        )
    }

    private fun requestPermission() {
        XXPermissions.with(this) // 申请安装包权限
            //.permission(Permission.REQUEST_INSTALL_PACKAGES)
            // 申请悬浮窗权限
            //.permission(Permission.SYSTEM_ALERT_WINDOW)
            // 申请通知栏权限
            //.permission(Permission.NOTIFICATION_SERVICE)
            // 申请系统设置权限
            //.permission(Permission.WRITE_SETTINGS)
            // 申请单个权限
            .permission(Permission.WRITE_EXTERNAL_STORAGE)
            .permission(Permission.CAMERA)
            .permission(Permission.ACCESS_FINE_LOCATION) // 申请多个权限
            .permission(Permission.ACCESS_COARSE_LOCATION)
//            .permission(Permission.ACCESS_BACKGROUND_LOCATION) // 不能和其他权限一起申请
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {
                    if (all) {
                        Log.d(TAG, "获取录音和日历权限成功")
                        getImages()
                    } else {
                        Log.d(TAG, "获取部分权限成功，但部分权限未正常授予")
                    }
                }

                override fun onDenied(permissions: List<String>, never: Boolean) {
                    if (never) {
                        Log.d(TAG, "被永久拒绝授权，请手动授予录音和日历权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@XXPermissionActivity, permissions)
                    } else {
                        Log.d(TAG, "获取录音和日历权限失败")
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, Permission.ACCESS_FINE_LOCATION) &&
                XXPermissions.isGranted(this, Permission.ACCESS_COARSE_LOCATION) &&
                XXPermissions.isGranted(this, Permission.ACCESS_BACKGROUND_LOCATION)) {
                Log.d(TAG, "用户已经在权限设置页授予了录音和日历权限")
            } else {
                Log.d(TAG, "用户没有在权限设置页授予权限")
            }
        }
    }

}