package ui


import AdbCmd
import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import execRun
import java.io.FilenameFilter

@Composable
fun ApkLayout(
    deviceName: String,
    window: AppWindow,
    onInstallResult: (String) -> Unit,
    onCmdError: (Throwable) -> Unit,
) {
    Row(
        modifier = Modifier.padding(8.dp),
    ) {
        Button(onClick = {
            val fd = java.awt.FileDialog(window.window).apply {
                filenameFilter = FilenameFilter { dir, name ->
                    name.endsWith(".apk")
                }
                isVisible = true
            }
            val dir = fd.directory
            val filename = fd.file
            if (filename != null) {
                val result = AdbCmd.adbInstall(deviceName, "$dir$filename").execRun(
                    onCmdError = onCmdError
                )
                onInstallResult(result)
            }
        }) {
            Text("安装 apk")
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = {
            val fd = java.awt.FileDialog(window.window).apply {
                isVisible = true
            }
            val dir = fd.directory
            val filename = fd.file
            if (filename != null) {
                val result = AdbCmd.adbPush(deviceName, "$dir$filename", filename).execRun(
                    onCmdError = onCmdError
                )
                onInstallResult(result)
            }
        }) {
            Text("推送文件到手机")
        }
    }
}