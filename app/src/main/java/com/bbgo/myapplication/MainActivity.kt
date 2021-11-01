package com.bbgo.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.permission.XXPermissionActivity
import org.bouncycastle.util.encoders.Hex

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.tv_test).setOnClickListener {
            startActivity(Intent(this, XXPermissionActivity::class.java))
            /*val componentName = ComponentName(
                "com.bbgo.wanandroid",
                "com.bbgo.wanandroid.SplashActivity"
            )
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString("FIRST_APP_KEY", "你好 ，MainActivity")
            intent.putExtras(bundle)
            intent.component = componentName
            startActivity(intent)*/
        }

        Log.d(TAG, "===============")

        val data = ByteUtil.hexStringToBytes(AESCBCUtil.keyString)

        val iv = ByteUtil.hexStringToBytes(AESCBCUtil.sha1(String(AESCBCUtil.keyByteArray)).substring(0, 32))


        val result = AESCBCUtil.encrypt(ByteUtil.hexStringToBytes("aa3b716100000c000000000000000000"), data, iv)
        Log.d(TAG, Hex.toHexString(result))


        val content = ByteUtil.hexStringToBytes(Hex.toHexString(result))
        val decoder = AESCBCUtil.decrypt(content, data, iv)
        Log.d(TAG, decoder?.let { Hex.toHexString(it) })


//        val data = AESUtil.hexToByte(AESUtil.keyString)
//        val result = AESUtil.encrypt("c43b063bf07aa424".toByteArray(), data, AESUtil.hexToByte(AESUtil.sha1(String(AESUtil.keyByteArray)).substring(0, 32)))
//        Log.d(TAG, Hex.toHexString(result))

//        d81d48136f54446c8d0b0504eb9aff87
//        val uuid = UUID.randomUUID().toString().replace("-", "")
//        val newResult = AESUtil.encrypt("c43b063bf07aa424".toByteArray(), uuid.toByteArray(), AESUtil.hexToByte(AESUtil.sha1(uuid).substring(0, 32)))
//        Log.d(TAG, Hex.toHexString(newResult))


    }

}