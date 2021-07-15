package ui

import AdbCmd
import androidx.compose.foundation.layout.*
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
    activityFullName: String,
    onPackageNameInputChange: (String) -> Unit,
    onStartAppResult: (String) -> Unit,
    onMonkeyResult: (String) -> Unit,
    onCmdError: (Throwable) -> Unit,
) {
    Column {
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

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                activityFullName,
                label = {
                    Text("Activity全限定名（activityFullName）")
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
                    val result = AdbCmd.adbStartAppCmd(deviceName, packageName, activityFullName).execRun(
                        onCmdError = onCmdError
                    )
                    onStartAppResult(result)
                }) {
                Text("StartApp")
            }

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}