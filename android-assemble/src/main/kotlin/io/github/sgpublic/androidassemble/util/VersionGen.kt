package io.github.sgpublic.androidassemble.util

import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/** 辅助管理版本名称 */
object VersionGen {
    /**
     * git commit id，若当前不为 git 仓库，则返回 TIME_MD5，可用于版本名后缀
     * @see TIME_MD5
     */
    val GIT_HEAD: String get() {
        val lines = Runtime.getRuntime()
                .exec("git rev-parse --short HEAD")
                .inputStream.reader()
                .readLines()
        if (lines.isEmpty()) {
            return TIME_MD5
        }
        return lines[0]
    }

    /**
     * 按照 yyMMdd 的格式，根据当前日期返回一个整数，可用于版本号
     */
    val DATED_VERSION: Int @Suppress("SimpleDateFormat") get() {
        return Integer.parseInt(SimpleDateFormat("yyMMdd").format(Date()))
    }

    /**
     * 按照 yyMMdd 的格式，根据最近一条 git commit 日期返回一个整数，若当前不为 git 仓库，则返回 DATED_VERSION，可用于版本号
     * @see DATED_VERSION
     */
    val COMMIT_VERSION: Int get() {
        val lines = Runtime.getRuntime()
                .exec("git log -n 1 --pretty=format:%cd --date=format:%y%m%d")
                .inputStream.reader()
                .readLines()
        if (lines.isEmpty()) {
            return DATED_VERSION
        }
        return Integer.parseInt(lines[0])
    }

    /**
     * 返回当前日期的 MD5，截取其中十位，与 git commit id 位数相等，可用于版本名后缀
     */
    val TIME_MD5: String get() {
        val digest = MessageDigest.getInstance("MD5")
                .digest(System.currentTimeMillis().toString().toByteArray())
        val pre = BigInteger(1, digest)
        return pre.toString(16).padStart(32, '0').substring(8, 18)
    }
}