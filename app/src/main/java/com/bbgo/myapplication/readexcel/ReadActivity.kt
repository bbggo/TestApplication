package com.bbgo.myapplication.readexcel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.io.File

class ReadActivity : AppCompatActivity() {

    private val REQUEST_SELECT_FILE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_test).text = "test"

        findViewById<TextView>(R.id.tv_test).setOnClickListener {
            XXPermissions.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            Intent(Intent.ACTION_GET_CONTENT).apply {
                                type = "*/*"
                                addCategory(Intent.CATEGORY_OPENABLE)
                                startActivityForResult(this, REQUEST_SELECT_FILE)
                            }
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        super.onDenied(permissions, never)
                    }

                })
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val string = FileUtils.getPath(this, uri)
            Thread{
                ExcelUtils.readExcel(this, File(string), uri)
            }.start()
            println(string)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}