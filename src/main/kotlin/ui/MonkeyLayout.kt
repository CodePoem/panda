package ui

import AdbCmd
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import execRun

@Composable
fun MonkeyLayout(
    deviceName: String,
    packageName: String,
    onPackageNameInputChange: (String) -> Unit,
    onMonkeyResult: (String) -> Unit,
    onCmdError: (Throwable) -> Unit,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            packageName,
            label = {
                Text("包名（applicationId）")
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
            ),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = MaterialTheme.colors.onPrimary
            ),
            onValueChange = onPackageNameInputChange
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                val result = AdbCmd.adbMonkeyCmd(deviceName, packageName).execRun(
                    onCmdError = onCmdError
                )
                onMonkeyResult(result)
            }) {
            Text("Monkey")
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}