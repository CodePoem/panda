package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultLayout(result: String) {
    Row(
        modifier = Modifier.height(300.dp)
    ) {
        TextField(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxSize().padding(8.dp),
            label = { Text("Result") },
            value = result,
            readOnly = true,
            onValueChange = {
            }
        )
    }
}