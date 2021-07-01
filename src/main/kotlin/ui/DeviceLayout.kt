package ui

import AdbCmd
import SelectView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import execRun
import model.Item

@Composable
fun DeviceLayout(
    onDevicesResult: (String) -> Unit,
    selectValue: String,
    selectList: MutableList<Item>,
    expanded: Boolean,
    onSelectClick: () -> Unit,
    onMenuDismissRequest: () -> Unit,
    onItemClick: (item: Item) -> Unit,
    onCmdError: (Throwable) -> Unit,
) {
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                val result = AdbCmd.adbDevicesCmd().execRun(
                    onCmdError = onCmdError
                )
                onDevicesResult(result)
            }) {
            Text("运行设备列表")
        }

        Spacer(modifier = Modifier.width(8.dp))

        SelectView(
            selectValue,
            selectList,
            expanded,
            onSelectClick,
            onMenuDismissRequest,
            onItemClick
        )

        Spacer(modifier = Modifier.width(8.dp))
    }
}