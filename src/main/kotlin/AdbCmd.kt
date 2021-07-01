import androidx.compose.ui.platform.DesktopPlatform
import constants.DefaultConstants
import java.io.File

object AdbCmd {

    var adb = "${DefaultConstants.DEFAULT_ANDROID_HOME}/platform-tools/adb"

    private const val ADB_DEF_DEVICE = "-s %1s"

    private const val ADB_DEVICES = "devices"

    private const val ADB_MODEL = "$ADB_DEF_DEVICE shell getprop ro.product.model"

    private const val ADB_VERSION_RELEASE = "$ADB_DEF_DEVICE shell getprop ro.build.version.release"

    private const val ADB_VERSION_RELEASE_SDK = "$ADB_DEF_DEVICE shell getprop ro.build.version.sdk"

    private const val ADB_INSTALL = "$ADB_DEF_DEVICE install -r %2s"

    private const val ADB_PUSH = "$ADB_DEF_DEVICE push %2s sdcard/%3s"

    private const val ADB_ACTIVITIES_UNIX =
        "$ADB_DEF_DEVICE shell dumpsys activity | grep -E 'mCurrentFocus|mFocusedApp|Run #0'"

    private const val ADB_ACTIVITIES_WIN =
        "$ADB_DEF_DEVICE shell dumpsys activity | findstr 'mCurrentFocus mFocusedApp Run #0'"

    private const val ADB_SCREENSHOT = "$ADB_DEF_DEVICE exec-out screencap -p > %2s"

    private const val ADB_MONKEY_THROTTLE_DEFAULT = 500

    private const val ADB_MONKEY_TIMES_DEFAULT = 100

    private const val ADB_MONKEY = "$ADB_DEF_DEVICE shell monkey -p %2s -v %3d –-throttle %4d"

    fun adbDevicesCmd(): String {
        return "$adb $ADB_DEVICES"
    }

    fun adbModelCmd(deviceName: String): String {
        return String.format("$adb $ADB_MODEL", deviceName)
    }

    fun adbVersionReleaseCmd(deviceName: String): String {
        return String.format("$adb $ADB_VERSION_RELEASE", deviceName)
    }

    fun adbVersionSdkCmd(deviceName: String): String {
        return String.format("$adb $ADB_VERSION_RELEASE_SDK", deviceName)
    }

    fun adbInstall(deviceName: String, apk: String): String {
        return String.format("$adb $ADB_INSTALL", deviceName, apk)
    }

    fun adbPush(deviceName: String, filePath: String, fileName: String): String {
        return String.format("$adb $ADB_PUSH", deviceName, filePath, fileName)
    }

    fun adbActivitiesCmd(deviceName: String): String {
        return when (DesktopPlatform.Current) {
            DesktopPlatform.Linux -> {
                String.format("$adb $ADB_ACTIVITIES_UNIX", deviceName)
            }
            DesktopPlatform.MacOS -> {
                String.format("$adb $ADB_ACTIVITIES_UNIX", deviceName)
            }
            DesktopPlatform.Windows -> {
                String.format("$adb $ADB_ACTIVITIES_WIN", deviceName)
            }
            else -> ""
        }
    }

    fun adbScreenshotCmd(deviceName: String, outputFileName: String): String {
        return String.format("$adb $ADB_SCREENSHOT", deviceName, outputFileName)
    }

    fun adbMonkeyCmd(deviceName: String, packageName: String): String {
        return String.format(
            "$adb $ADB_MONKEY",
            deviceName,
            packageName,
            ADB_MONKEY_TIMES_DEFAULT,
            ADB_MONKEY_THROTTLE_DEFAULT
        )
    }
}