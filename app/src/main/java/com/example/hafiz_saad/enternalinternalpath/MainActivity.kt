package com.example.hafiz_saad.enternalinternalpath

import android.annotation.SuppressLint
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import java.nio.file.Files.size
import android.os.StatFs
import android.support.annotation.RequiresApi
import android.util.Log
import java.io.File


class MainActivity : AppCompatActivity() {
    var external:TextView? = null
    var internal:TextView? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        external = findViewById(R.id.external)
        internal = findViewById(R.id.internal)

//        external?.text  = Environment.getDataDirectory().length().toString()
        //point to mount sd card path
//        var m = Integer.parseInt(Environment.getExternalStorageState().length.toString()) / 1024.0
//        var g = Integer.parseInt(Environment.getExternalStorageState().length.toString()) / 1048576.0
//        external?.text = Environment.getExternalStorageState().length.toString()
////        if(Environment.getExternalStorageState() == null)
//        internal?.text  = Environment.getExternalStorageDirectory().length().toString()
//        val stat = StatFs(Environment.getExternalStorageDirectory().path)
//        val bytesAvailable: Long
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
//        } else {
//            bytesAvailable = stat.blockSize.toLong() * stat.availableBlocks.toLong()
//        }
//        val megAvailable = bytesAvailable / (1024 * 1024)
//        Log.e("MainActivity", "Available MB : $megAvailable")

        if(android.os.Build.VERSION.SDK_INT < 18){
            val freeBytesInternal = File(Environment.getDataDirectory().toString()).freeSpace
            val freeBytesExternal = File(Environment.getExternalStorageDirectory().toString()).freeSpace
            val totalInternal = File(Environment.getDataDirectory().toString()).totalSpace
            val totalExternal = File(Environment.getExternalStorageDirectory().toString()).totalSpace
            internal?.text = "Internal Memory Size Available " +formatSize(freeBytesInternal) + " Internal Total Size " + formatSize(totalInternal)
            external?.text = "External Memory Size Available " +formatSize(freeBytesExternal) + " External Total Size " + formatSize(totalExternal)
        }
        else{
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            internal?.text = "Internal Memory Size Available " +getAvailableInternalMemorySize() + " Internal Total Size " + getTotalInternalMemorySize()
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            external?.text = "External Memory Size Available " +getAvailableExternalMemorySize() + " External Total Size " + getTotalExternalMemorySize()
        }
    }

    fun externalMemoryAvailable(): Boolean {
        return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getAvailableInternalMemorySize(): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return formatSize(availableBlocks * blockSize)
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getTotalInternalMemorySize(): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return formatSize(totalBlocks * blockSize)
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getAvailableExternalMemorySize(): String {
        if (externalMemoryAvailable()) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            return formatSize(availableBlocks * blockSize)
        } else {
            return DeprecationLevel.ERROR.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getTotalExternalMemorySize(): String {
        if (externalMemoryAvailable()) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            return formatSize(totalBlocks * blockSize)
        } else {
            return DeprecationLevel.ERROR.toString()
        }
    }

    fun formatSize(size: Long): String {
        var size = size
        var suffix: String? = null

        if (size >= 1024) {
            suffix = "KB"
            size /= 1024
            if (size >= 1024) {
                suffix = "MB"
                size /= 1024
                if (size >= 1024) {
                    suffix = "GB"
                    size /= 1024
                }
            }
        }

        val resultBuffer = StringBuilder(java.lang.Long.toString(size))

        var commaOffset = resultBuffer.length - 3
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',')
            commaOffset -= 3
        }

        if (suffix != null) resultBuffer.append(suffix)
        return resultBuffer.toString()
    }
}
