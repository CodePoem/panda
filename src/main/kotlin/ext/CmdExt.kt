import androidx.compose.ui.platform.DesktopPlatform
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

typealias CmdError = (error: Throwable) -> Unit

fun String.execRun(onCmdError: CmdError? = null): String {
    return when (DesktopPlatform.Current) {
        DesktopPlatform.Linux -> {
            this.runOnLinux(onCmdError)
        }
        DesktopPlatform.MacOS -> {
            this.runOnMac(onCmdError)
        }
        DesktopPlatform.Windows -> {
            this.runOnWindows(onCmdError)
        }
        else -> ""
    }
}

fun String.runOnLinux(onCmdError: CmdError? = null): String {
    val cmd = this
    val runtime = Runtime.getRuntime()
    val realCmd = arrayOf("/bin/bash","-c", cmd)
    try {
        val p = runtime.exec(realCmd)
        return p.inputStreamToStringWithReader()
    } catch (e: IOException) {
        e.printStackTrace()
        onCmdError?.invoke(e)
    } catch (e: Exception) {
        onCmdError?.invoke(e)
    }
    return ""
}

fun String.runOnMac(onCmdError: CmdError? = null): String {
    val cmd = this
    val runtime = Runtime.getRuntime()
    val realCmd = arrayOf("/bin/bash","-c", cmd)
    try {
        val p = runtime.exec(realCmd)
        return p.inputStreamToStringWithReader()
    } catch (e: IOException) {
        e.printStackTrace()
        onCmdError?.invoke(e)
    } catch (e: Exception) {
        onCmdError?.invoke(e)
    }
    return ""
}

fun String.runOnWindows(onCmdError: CmdError? = null): String {
    val cmd = this
    val runtime = Runtime.getRuntime()
    val realCmd = "cmd /c $cmd"
    try {
        val p = runtime.exec(realCmd)
        return p.inputStreamToStringWithReader()
    } catch (e: IOException) {
        e.printStackTrace()
        onCmdError?.invoke(e)
    } catch (e: Exception) {
        onCmdError?.invoke(e)
    }
    return ""
}

fun Process.inputStreamToStringWithReader(): String {
    val result: StringBuilder = StringBuilder()
    try {
        this.waitFor()
    } catch (e: InterruptedException) {
        println(e.message)
    }
    val exitCode = this.exitValue()
    if (exitCode == 0) {
        val readInput = BufferedReader(InputStreamReader(this.inputStream))
        while (readInput.ready()) {
            result.append(readInput.readLine())
            result.append("\n")
        }
    } else {
        val readError = BufferedReader(InputStreamReader(this.errorStream))
        while (readError.ready()) {
            result.append(readError.readLine())
            result.append("\n")
        }
    }
    return result.toString()
}