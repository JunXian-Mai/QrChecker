package com.markensic.sdk.utils

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.OpenableColumns
import com.markensic.sdk.global.App
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream
import java.net.URI

object FileUtils {
  val sDefaultPath =
    App.sApplication.getExternalFilesDir(null)!!.absolutePath + File.separator

  fun createFile(path: String): File {
    return File(path).also {
      if (!it.exists()) {
        val dirPath = path.substring(0, path.lastIndexOf("/"))
        createFileDir(dirPath)

        if (path != dirPath) {
          it.createNewFile()
        }
      }
    }
  }

  fun iterateFileInDir(path: String, handler: (f: File) -> Unit) {
    File(path).also {
      if (it.exists() && it.isDirectory) {
        it.walk().iterator().forEach { file ->
          handler(file)
        }
      }
    }
  }

  fun createFileDir(path: String): Boolean {
    return File(path).mkdirs()
  }

  fun deleteFile(path: String): Boolean {
    return File(path).let {
      if (it.exists()) {
        it.deleteRecursively()
      } else {
        true
      }
    }
  }

  fun appendToFile(path: String, text: String) {
    createFile(path).also {
      it.appendText(text)
    }
  }

  fun writeToFile(path: String, text: String) {
    createFile(path).also {
      it.writeText(String())
    }
  }

  fun writeToFile(path: String, array: ByteArray) {
    createFile(path).also {
      it.writeBytes(array)
    }
  }

  fun uriCastFile(uri: Uri): File? {
    return if (uri.scheme == ContentResolver.SCHEME_FILE) {
      uri.path?.let {
        File(it)
      }
    } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val contentResolver = App.sApplication.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
          if (it.moveToFirst()) {
            val uriInputStream = contentResolver.openInputStream(uri)
            if (uriInputStream != null) {
              it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                .let { fileName ->
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val file = File("${App.sApplication.externalCacheDir!!.absolutePath}/$fileName")
                    val fos = FileOutputStream(file)
                    uriInputStream.use {
                      fos.use {
                        FileUtils.copy(uriInputStream, fos)
                      }
                    }
                    file
                  } else {
                    val file = File(App.sApplication.filesDir, fileName)
                    uriInputStream.use {
                      FileOutputStream(file).sink().buffer().use { sink ->
                        sink.write(uriInputStream.readBytes())
                      }
                    }
                    file
                  }
                }
            } else {
              null
            }
          } else {
            null
          }
        }
      } else {
        File(URI(uri.toString()))
      }
    } else {
      throw RuntimeException("uri scheme error: ${uri.scheme}")
    }
  }
}