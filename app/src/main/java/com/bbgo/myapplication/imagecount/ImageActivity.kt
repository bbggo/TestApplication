package com.bbgo.myapplication.imagecount

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bbgo.myapplication.R

class ImageActivity : AppCompatActivity() {

    private val TAG = "ImageActivity"

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        getDisplayInfo()

    }

    private fun getDisplayInfo() {
        val metrics = resources.displayMetrics
        Log.d(TAG, "density = ${metrics.density}")
        Log.d(TAG, "screenWidth = ${metrics.widthPixels}")
        Log.d(TAG, "screenHeight = ${metrics.heightPixels}")
        Log.d(TAG, "scaledDensity = ${metrics.scaledDensity}")
        Log.d(TAG, "desityDpi = ${metrics.densityDpi}")

        loadResImage(findViewById<ImageView>(R.id.image_view))
    }

    /**
     * 一张图片在未使用图片加载框架，并且加载的是res里面的图片，
     * 则它占用的内存是：新长*新宽*一个像素所占字节
     * 新长=旧长*（设备的dpi / 目录对应的dpi)
     *
     * 加载其他地方的图片，比如网络或者磁盘，则它所占内存是：图片原始宽*原始高*一个像素所占字节
     *
     * https://www.cnblogs.com/dasusu/p/9789389.html
     */
    private fun loadResImage(imageView: ImageView) {
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.image_count, options)
        imageView.setImageBitmap(bitmap)
        Log.d(TAG, "bitmap: ByteCount = ${bitmap.byteCount}  bitmap：AllocationByteCount = ${bitmap.allocationByteCount}")
        Log.d(TAG, "width = ${bitmap.width}  height = ${bitmap.height}")
        Log.d(TAG, "inDensity = ${options.inDensity}  inTargetDensity = ${options.inTargetDensity}")
        Log.d(TAG, "imageview.width = ${imageView.width}  imageview.height = ${imageView.height}")
    }

}