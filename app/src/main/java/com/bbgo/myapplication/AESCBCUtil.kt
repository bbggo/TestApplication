package com.bbgo.myapplication

import java.lang.StringBuilder
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security


/**
 * AES CBC PKCS7 模式加密解密
 */
object AESCBCUtil {

    private const val CIPHER_MODE = "AES/CBC/PKCS7Padding"

    val keyByteArray = byteArrayOf(
        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
        0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,
        0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14,
        0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b,
        0x1c, 0x1d, 0x1e, 0x1f
    )

    val keyString = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"

    @Throws(Exception::class)
    fun sha1(inStr: String): String {
        val sha = try {
            MessageDigest.getInstance("SHA")
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
            return ""
        }
        val byteArray = inStr.toByteArray(charset("UTF-8"))
        val md5Bytes: ByteArray = sha.digest(byteArray)
        val hexValue = StringBuffer()
        for (i in md5Bytes.indices) {
            val `val` = md5Bytes[i].toInt() and 0xff
            if (`val` < 16) {
                hexValue.append("0")
            }
            hexValue.append(Integer.toHexString(`val`))
        }
        return hexValue.toString()
    }

    /**
     *
     * @param srcData 要加密的数组（String 需要base64 编码）
     * @param key 公钥，32位byte数组
     * @param iv 私钥，16位byte数组
     * @return 加密后的byte数组
     * @throws Exception 找不到加密算法等
     */
    @Throws(Exception::class)
    fun encrypt(srcData: ByteArray?, key: ByteArray?, iv: ByteArray?): ByteArray? {
//        Cipher.getMaxAllowedKeyLength(CIPHER_MODE)
        val keySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance(CIPHER_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
        return cipher.doFinal(srcData)
    }

    /**
     *
     * @param encData 要解密的数组
     * @param key 公钥
     * @param iv 私钥
     * @return 解密后的byte数组
     * @throws Exception 找不到解密算法等
     */
    @Throws(Exception::class)
    fun decrypt(encData: ByteArray?, key: ByteArray?, iv: ByteArray?): ByteArray? {
//        Cipher.getMaxAllowedKeyLength(CIPHER_MODE)
        val keySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance(CIPHER_MODE)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        return cipher.doFinal(encData)
    }

    fun hexToByte(hexParam: String): ByteArray {
        /**
         * 先去掉16进制字符串的空格
         */
        var hex = hexParam
        hex = hex.replace(" ", "")
        /**
         * 字节数组长度为16进制字符串长度的一半
         */
        val byteLength = hex.length / 2
        val bytes = ByteArray(byteLength)
        var m = 0
        var n = 0
        for (i in 0 until byteLength) {
            m = i * 2 + 1
            n = m + 1
            val intHex = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n))
            bytes[i] = java.lang.Byte.valueOf(intHex.toByte())
        }
        return bytes
    }
}