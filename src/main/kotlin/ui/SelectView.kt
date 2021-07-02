import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Item

@Composable
fun SelectView(
    selectValue: String,
    selectList: MutableList<Item>,
    expanded: Boolean,
    onSelectClick: () -> Unit,
    onMenuDismissRequest: () -> Unit,
    onItemClick: (item: Item) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "选择当前设备：",
                style = TextStyle(
                    color = MaterialTheme.colors.onPrimary
                )
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ClickableText(
                    text = AnnotatedString(selectValue),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.onPrimary,
                    ),
                    onClick = {
                        onSelectClick()
                    }
                )
                IconButton(
                    onClick = {
                        onSelectClick()
                    }
                ) {
                    Icon(Icons.Default.ArrowDropDown, "")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        onMenuDismissRequest()
                    },
                ) {
                    for (it in selectList) {
                        DropdownMenuItem(onClick = {
                            onItemClick(it)
                        }) {
                            Text(it.text)
                        }
                    }
                }
            }
        }
    }
}
