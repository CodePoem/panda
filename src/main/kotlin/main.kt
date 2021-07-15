import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import constants.DefaultConstants
import model.Device
import model.Item
import ui.*

fun main() = Window(
    title = "Panda",
) {
    val darkTheme = isSystemInDarkTheme()
    val androidHome = remember { mutableStateOf(DefaultConstants.DEFAULT_ANDROID_HOME) }
    val cmdResult = remember { mutableStateOf(DefaultConstants.DEFAULT_EMPTY) }
    val devices = remember { mutableStateOf(mutableListOf<Device>()) }
    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf(Item("", DefaultConstants.DEFAULT_UNSELECT_DEVICE)) }
    val selectList = remember { mutableStateOf(mutableListOf<Item>()) }
    val model = remember { mutableStateOf("") }
    val versionRelease = remember { mutableStateOf("") }
    val versionSdk = remember { mutableStateOf("") }
    val packageName = remember { mutableStateOf(DefaultConstants.DEFAULT_PACKAGE_NAME) }
    val activityFullName = remember { mutableStateOf(DefaultConstants.DEFAULT_ACTIVITY_FULL_NAME) }
    val window = LocalAppWindow.current
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        MaterialTheme(
            colors = if (darkTheme) DarkColors else LightColors,
            shapes = Shapes,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)
            ) {
                ResultLayout(cmdResult.value)

                Column(
                    modifier = Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    BaseFuncLayout(
                        onClearResult = {
                            cmdResult.value = it
                        }
                    )

                    ConfigLayout(
                        androidHome = androidHome.value,
                        onAndroidHomeInputChange = {
                            androidHome.value = it
                            AdbCmd.adb = "$it/platform-tools/adb"
                        }
                    )

                    TitleLayout(
                        phoneTitle = getPhoneTitle(
                            selectedItem,
                            model,
                            versionRelease,
                            versionSdk
                        ),
                    )

                    DeviceLayout(
                        onDevicesResult = {
                            cmdResult.value = it
                            findDevices(it, devices)
                            generateSelectProps(devices, selectList)
                        },
                        selectValue = selectedItem.value.text,
                        selectList = selectList.value,
                        expanded = expanded.value,
                        onSelectClick = {
                            expanded.value = true
                        },
                        onMenuDismissRequest = {
                            expanded.value = false
                        },
                        onItemClick = {
                            selectedItem.value = it
                            expanded.value = false
                            model.value = AdbCmd.adbModelCmd(selectedItem.value.id).execRun().trim('\n')
                            versionRelease.value =
                                AdbCmd.adbVersionReleaseCmd(selectedItem.value.id).execRun().trim('\n')
                            versionSdk.value = AdbCmd.adbVersionSdkCmd(selectedItem.value.id).execRun().trim('\n')
                        },
                        onCmdError = {
                            cmdResult.value = it.message ?: ""
                        }
                    )

                    ApkLayout(
                        deviceName = selectedItem.value.id,
                        window,
                        onInstallResult = {
                            cmdResult.value = it
                        },
                        onCmdError = {
                            cmdResult.value = it.message ?: ""
                        }
                    )

                    ActivityLayout(
                        deviceName = selectedItem.value.id,
                        window = window,
                        onActivitiesResult = {
                            cmdResult.value = it
                        },
                        onScreenShotResult = {
                            cmdResult.value = it
                        },
                        onCmdError = {
                            cmdResult.value = it.message ?: ""
                        }
                    )

                    MonkeyLayout(
                        deviceName = selectedItem.value.id,
                        packageName = packageName.value,
                        activityFullName = activityFullName.value,
                        onPackageNameInputChange = {
                            packageName.value = it
                        },
                        onStartAppResult = {
                            cmdResult.value = it
                        },
                        onMonkeyResult = {
                            cmdResult.value = it
                        },
                        onCmdError = {
                            cmdResult.value = it.message ?: ""
                        }
                    )
                }
            }
        }
    }

}

private val Green200 = Color(0xffa5d6a7)
private val Green200Dark = Color(0xff75a478)
private val LightGreen200 = Color(0xffc5e1a5)
private val LightGreen200Dark = Color(0xff94af76)

private val Pink200 = Color(0xfff48fb1)
private val Pink200Dark = Color(0xfffbf5f82)

private val Purple200 = Color(0xfffce93d8)
private val Purple200Dark  = Color(0xfff9c64a6)


private val LightColors = lightColors(
    primary = Green200,
    secondary = LightGreen200,

    onPrimary = Pink200,
    onSurface = Purple200,
)

private val DarkColors = darkColors(
    primary = Green200Dark,
    secondary = LightGreen200Dark,

    onPrimary = Pink200Dark,
    onSurface = Purple200Dark,
)

private val Shapes = Shapes(
    small = RoundedCornerShape(percent = 50),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

private fun getPhoneTitle(
    selectedItem: MutableState<Item>,
    model: MutableState<String>,
    versionRelease: MutableState<String>,
    versionSdk: MutableState<String>
): String {
    if (selectedItem.value.id.isBlank()) {
        return DefaultConstants.DEFAULT_UNSELECT_DEVICE
    }
    return "${model.value} [ ${versionRelease.value} (${versionSdk.value}) ]"
}

private fun findDevices(
    result: String,
    devices: MutableState<MutableList<Device>>
) {
    devices.value.clear()
    val devicesString = result.split('\n').toMutableList()
    devicesString.forEach {
        if (it.contains('\t')) {
            val deviceFields = it.split('\t')
            if (deviceFields.size == 2) {
                devices.value.add(Device(deviceFields[0], deviceFields[1]))
            }
        }
    }
}

private fun generateSelectProps(
    devices: MutableState<MutableList<Device>>,
    selectList: MutableState<MutableList<Item>>
) {
    selectList.value.clear()
    devices.value.forEach {
        selectList.value.add(
            Item(it.name, "${it.name}[${it.state}]")
        )
    }
}
