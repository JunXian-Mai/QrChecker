package com.markensic.core.utils

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.OpenableColumns
import com.markensic.core.global.CoreApp
import com.markensic.core.global.log.CoreLog
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream
import java.net.URI

object FileUtils {
  val sDefaultPath =
    CoreApp.sApplication.getExternalFilesDir(null)!!.absolutePath + File.separator

  private fun create(path: String): File {
    return File(path).also {
      if ('/' == path.last()) {
        if (!it.exists() || !it.isDirectory) {
          if (!makeDirectory(path)) {
            CoreLog.e("$path create directory error")
          }
        }
      } else {
        if (!it.exists() || it.isDirectory) {
          val dirPath = path.substring(0, path.lastIndexOf("/") + 1)
          makeDirectory(dirPath)
          if (!it.createNewFile()) {
            CoreLog.e("$path create file error")
          }
        }
      }
    }
  }

  fun createFile(path: String): File? {
    val file = create(path)
    return if (file.exists() && !file.isDirectory) {
      file
    } else {
      if (file.isDirectory) {
        CoreLog.e("$path createFile error, this is directory")
      }
      null
    }
  }

  @Throws(IllegalAccessError::class)
  fun makeDirectory(path: String): Boolean {
    return if ('/' == path.last()) {
      File(path).mkdirs()
    } else {
      throw IllegalAccessError("$path is not a directory")
    }
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

  fun iterateFileInDir(path: String, handler: (f: File) -> Unit) {
    File(path).also {
      if (it.exists() && it.isDirectory) {
        it.walk().iterator().forEach { file ->
          handler(file)
        }
      }
    }
  }

  fun appendToFile(path: String, text: String) {
    createFile(path)?.also {
      it.appendText(text)
    }
  }

  fun writeToFile(path: String, text: String) {
    createFile(path)?.also {
      it.writeText(text)
    }
  }

  fun writeToFile(path: String, array: ByteArray) {
    createFile(path)?.also {
      it.writeBytes(array)
    }
  }

  @Throws(RuntimeException::class)
  fun uriCastFile(uri: Uri): File? {
    return if (uri.scheme == ContentResolver.SCHEME_FILE) {
      uri.path?.let {
        File(it)
      }
    } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val contentResolver = CoreApp.sApplication.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
          if (it.moveToFirst()) {
            val uriInputStream = contentResolver.openInputStream(uri)
            if (uriInputStream != null) {
              it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                .let { fileName ->
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val file = File("${CoreApp.sApplication.externalCacheDir!!.absolutePath}/$fileName")
                    val fos = FileOutputStream(file)
                    uriInputStream.use {
                      fos.use {
                        FileUtils.copy(uriInputStream, fos)
                      }
                    }
                    file
                  } else {
                    val file = File(CoreApp.sApplication.filesDir, fileName)
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
