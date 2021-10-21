package net.luculent.libcore.storage.file

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import net.luculent.libcore.storage.file.FileConstants.DOWNLOAD
import net.luculent.libcore.storage.file.FileConstants.LOGS
import net.luculent.libcore.storage.file.FileConstants.PICTURES
import net.luculent.libcore.storage.file.FileConstants.WEBCACHE
import java.io.*

/**
 *
 * @Description:     文件管理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/21 10:40
 */
@SuppressLint("StaticFieldLeak")
object FileManager {
    private lateinit var mContext: Context

    private const val FILE_WRITING_ENCODING = "UTF-8"
    private const val FILE_READING_ENCODING = "UTF-8"

    fun init(context: Context) {
        mContext = context
    }

    /**
     * @return 应用数据存储根目录
     */
    fun getAppDir(): File {
        return mContext.getExternalFilesDir(null)!!
    }

    /**
     * @return 应用图片存储目录
     */
    fun getAppPicturesDir(): File {
        return mContext.getExternalFilesDir(PICTURES)!!
    }

    /**
     * @return 应用日志存储目录
     */
    fun getAppLogsDir(): File {
        return mContext.getExternalFilesDir(LOGS)!!
    }

    /**
     * @return 应用下载存储目录
     */
    fun getAppDownloadDir(): File {
        return mContext.getExternalFilesDir(DOWNLOAD)!!
    }

    /**
     * @return Web缓存目录
     */
    fun getWebCacheDir(): File {
        return mContext.getExternalFilesDir(WEBCACHE)!!
    }

    /**
     * @return 创建一个新的文件目录
     */
    fun getAppNewDir(dir: String): File {
        return mContext.getExternalFilesDir(dir)!!
    }

    /**
     * @return 返回uri
     */
    fun getFileUri(file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                mContext,
                "${mContext.packageName}.fileSelect.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }

    // ***************************************** 文件工具 *******************************************

    @Throws(Exception::class)
    fun readFile(_sFileName: String, _sEncoding: String): String {
        var _sEncoding = _sEncoding
        var buffContent: StringBuffer? = null
        var sLine: String?
        var fis: FileInputStream? = null
        var buffReader: BufferedReader? = null
        if (_sEncoding == null || "" == _sEncoding) {
            _sEncoding = FILE_READING_ENCODING
        }
        return try {
            fis = FileInputStream(_sFileName)
            buffReader = BufferedReader(
                InputStreamReader(
                    fis,
                    _sEncoding
                )
            )
            var zFirstLine = "UTF-8".equals(_sEncoding, ignoreCase = true)
            while (buffReader.readLine().also { sLine = it } != null) {
                if (buffContent != null) {
                    buffContent.append("\n")
                } else {
                    buffContent = StringBuffer()
                }
                if (zFirstLine) {
                    sLine = removeBomHeaderIfExists(sLine)
                    zFirstLine = false
                }
                buffContent.append(sLine)
            } // end while
            buffContent?.toString() ?: ""
        } catch (ex: FileNotFoundException) {
            throw Exception("要读取的文件没有找到!", ex)
        } catch (ex: IOException) {
            throw Exception("读取文件时错误!", ex)
        } finally {
            // 增加异常时资源的释放
            try {
                buffReader?.close()
                fis?.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @Throws(Exception::class)
    fun writeFile(path: String, content: String, encoding: String, isOverride: Boolean): File? {
        var encoding = encoding
        if (TextUtils.isEmpty(encoding)) {
            encoding = FILE_WRITING_ENCODING
        }
        val `is`: InputStream = ByteArrayInputStream(content.toByteArray(charset(encoding)))
        return writeFile(`is`, path, isOverride)
    }

    @Throws(Exception::class)
    fun writeFile(`is`: InputStream?, path: String, isOverride: Boolean): File? {
        var path = path
        val sPath: String = extractFilePath(path)
        if (!pathExists(sPath)) {
            makeDir(sPath, true)
        }
        if (!isOverride && fileExists(path)) {
            path = if (path.contains(".")) {
                val suffix = path.substring(path.lastIndexOf("."))
                val pre = path.substring(0, path.lastIndexOf("."))
                pre + "_" + System.currentTimeMillis() + suffix
            } else {
                path + "_" + System.currentTimeMillis()
            }
        }
        var os: FileOutputStream? = null
        var file: File? = null
        return try {
            file = File(path)
            os = FileOutputStream(file)
            var byteCount = 0
            val bytes = ByteArray(1024)
            while (`is`!!.read(bytes).also { byteCount = it } != -1) {
                os.write(bytes, 0, byteCount)
            }
            os.flush()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("写文件错误", e)
        } finally {
            try {
                os?.close()
                `is`?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 移除字符串中的BOM前缀
     *
     * @param _sLine 需要处理的字符串
     * @return 移除BOM后的字符串.
     */
    private fun removeBomHeaderIfExists(_sLine: String?): String? {
        if (_sLine == null) {
            return null
        }
        var line: String = _sLine
        if (line.length > 0) {
            var ch = line[0]
            // 使用while是因为用一些工具看到过某些文件前几个字节都是0xfffe.
            // 0xfeff,0xfffe是字节序的不同处理.JVM中,一般是0xfeff
            while (ch.toInt() == 0xfeff || ch.toInt() == 0xfffe) {
                line = line.substring(1)
                if (line.length == 0) {
                    break
                }
                ch = line[0]
            }
        }
        return line
    }

    /**
     * 从文件的完整路径名（路径+文件名）中提取 路径（包括：Drive+Directroy )
     *
     * @param _sFilePathName
     * @return
     */
    fun extractFilePath(_sFilePathName: String): String {
        var nPos = _sFilePathName.lastIndexOf('/')
        if (nPos < 0) {
            nPos = _sFilePathName.lastIndexOf('\\')
        }
        return if (nPos >= 0) _sFilePathName.substring(0, nPos + 1) else ""
    }

    /**
     * 检查指定文件的路径是否存在
     *
     * @param _sPathFileName 文件名称(含路径）
     * @return 若存在，则返回true；否则，返回false
     */
    fun pathExists(_sPathFileName: String): Boolean {
        val sPath: String = extractFilePath(_sPathFileName)
        return fileExists(sPath)
    }

    fun fileExists(_sPathFileName: String?): Boolean {
        val file = File(_sPathFileName)
        return file.exists()
    }

    /**
     * 创建目录
     *
     * @param _sDir             目录名称
     * @param _bCreateParentDir 如果父目录不存在，是否创建父目录
     * @return
     */
    fun makeDir(_sDir: String?, _bCreateParentDir: Boolean): Boolean {
        var zResult = false
        val file = File(_sDir)
        zResult = if (_bCreateParentDir) file.mkdirs() // 如果父目录不存在，则创建所有必需的父目录
        else file.mkdir() // 如果父目录不存在，不做处理
        if (!zResult) zResult = file.exists()
        return zResult
    }
}