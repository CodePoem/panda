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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import execRun
import java.awt.FileDialog
import java.io.File
import java.io.FilenameFilter

@Composable
fun ActivityLayout(
    deviceName: String,
    window: AppWindow,
    onActivitiesResult: (String) -> Unit,
    onScreenShotResult: (String) -> Unit,
    onCmdError: (Throwable) -> Unit,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                val result = AdbCmd.adbActivitiesCmd(deviceName).execRun(
                    onCmdError = onCmdError
                )
                onActivitiesResult(result)
            }) {
            Text("运行界面 Activities")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                val fd = FileDialog(window.window).apply {
                    mode = FileDialog.SAVE
                    filenameFilter = FilenameFilter { dir, name ->
                        val file = File(dir, name)
                        file.isDirectory
                    }
                    isVisible = true
                }
                val dir = fd.directory
                val fileName = fd.file
                if (dir != null && fileName != null) {
                    val result = AdbCmd.adbScreenshotCmd(deviceName, "$dir$fileName").execRun(
                        onCmdError = onCmdError
                    )
                    onScreenShotResult(result)
                }
            }) {
            Text("截图")
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}