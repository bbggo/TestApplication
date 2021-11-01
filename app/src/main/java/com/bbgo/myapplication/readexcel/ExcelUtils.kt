package com.bbgo.myapplication.readexcel

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.util.Log
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.text.SimpleDateFormat
import android.provider.MediaStore

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.*


/**
 * @Description:
 * @Author: wangyuebin
 * @Date: 2021/9/16 2:30 下午
 */
object ExcelUtils {

    private val snList = mutableListOf<String>()

    private const val TAG = "EXCEL"

    fun isContainSN(sn: String): Boolean {
        return snList.contains(sn)
    }

    /**
     * 读取Excel文件
     * @param context
     */
    fun readExcel(context: Context) {
        kotlin.runCatching {
            val inputStream = context.resources.assets.open("SN.xlsx")
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)
            val rowsCount = sheet.physicalNumberOfRows
            val formulaEvaluator: FormulaEvaluator =
                workbook.creationHelper.createFormulaEvaluator()
            for (r in 0 until rowsCount) {
                val row: Row = sheet.getRow(r)
                val cellsCount = row.physicalNumberOfCells
                //每次读取一行的内容
                for (c in 0 until cellsCount) {
                    //将每一格子的内容转换为字符串形式
                    val value = getCellAsString(row, c, formulaEvaluator)
                    snList.add(value)
                    val cellInfo = "r:$r; c:$c; v:$value"
                    Log.d(TAG, cellInfo)
                }
            }
            inputStream.close()
        }.onFailure {
            Log.e(TAG, it.message!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun readExcel(context: Activity, file: File, uri: Uri?) {
        kotlin.runCatching {
            if (uri == null) return

            val contentResolver = context.contentResolver

            var imgPath = ""
            var imageUri: Uri? = null

            val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    println("image uri is ${FileUtils.getPath(context, uri)}")
                    imageUri = uri
                    imgPath = FileUtils.getPath(context, uri)
                }
                cursor.close()
            }

//            val fd = contentResolver.openFileDescriptor(imageUri!!, "r")
//            val inputStream = FileInputStream(fd?.fileDescriptor)
            val inputStream = contentResolver.openInputStream(imageUri!!)


            var name = "${System.currentTimeMillis()}.jpg"
            val values = ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME,  name)
            values.put(MediaStore.Images.Media.MIME_TYPE,  "image/jpeg")

            values.put(MediaStore.Images.Media.RELATIVE_PATH, "imageTest");
            var inseertUri: Uri? = null
            var outputStream: OutputStream? = null
            try {
                inseertUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                outputStream = contentResolver.openOutputStream(inseertUri!!)!!
                outputStream.write(inputStream!!.readBytes())
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                if (inseertUri != null) {
                    contentResolver.delete(inseertUri, null, null);
                }
            } finally {
                outputStream?.close()
            }

            val selectionclause = MediaStore.Images.Media.DISPLAY_NAME + "=?"
            val arguments = arrayOf(name)

            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selectionclause, arguments)


            /*val contentResolver = context.contentResolver
            val fd = contentResolver.openFileDescriptor(uri, "r")
            val inputStream = FileInputStream(fd?.fileDescriptor)
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)
            val rowsCount = sheet.physicalNumberOfRows
            val formulaEvaluator: FormulaEvaluator =
                workbook.creationHelper.createFormulaEvaluator()
            for (r in 0 until rowsCount) {
                val row: Row = sheet.getRow(r)
                val cellsCount = row.physicalNumberOfCells
                //每次读取一行的内容
                for (c in 0 until cellsCount) {
                    //将每一格子的内容转换为字符串形式
                    val value = getCellAsString(row, c, formulaEvaluator)
                    snList.add(value)
                    val cellInfo = "r:$r; c:$c; v:$value"
                    Log.d(TAG, cellInfo)
                }
            }
            inputStream.close()*/
        }.onFailure {
            Log.e(TAG, it.message!!)
        }
    }

    /**
     * 读取excel文件中每一行的内容
     * @param row
     * @param c
     * @param formulaEvaluator
     * @return
     */
    private fun getCellAsString(row: Row, c: Int, formulaEvaluator: FormulaEvaluator): String {
        var value = ""
        kotlin.runCatching {
            val cell = row.getCell(c)
            val cellValue = formulaEvaluator.evaluate(cell)
            when (cellValue.cellType) {
                Cell.CELL_TYPE_BOOLEAN -> value = "" + cellValue.booleanValue
                Cell.CELL_TYPE_NUMERIC -> {
                    val numericValue = cellValue.numberValue
                    value = if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        val date = cellValue.numberValue
                        val formatter = SimpleDateFormat("dd/MM/yy")
                        formatter.format(HSSFDateUtil.getJavaDate(date))
                    } else {
                        "" + numericValue
                    }
                }
                Cell.CELL_TYPE_STRING -> value = "" + cellValue.stringValue
                else -> {
                }
            }
        }.onFailure {
            Log.e(TAG, it.message!!)
        }
        return value
    }
}