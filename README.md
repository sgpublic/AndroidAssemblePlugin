# AndroidAssemblePlugin

此 Gradle 插件可以帮助您归档您的 Android Application 或 Android Library 项目打包的文件，而您只需要引用此插件而无需额外进行任何操作。

## 版本

| 插件版本 | 目前最新版                                                   | 支持的 AGP 版本 | 所需最低 Gradle 版本 |
| -------- | ------------------------------------------------------------ | --------------- | -------------------- |
| 1.x      | ![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.sgpublic.android-assemble?versionPrefix=1.) | 4.2.x           | 7.6                  |
| 2.x      | ![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.sgpublic.android-assemble?versionPrefix=2.) | 7.x ~ 8.x       | 7.6                  |

## 快速开始

```kotlin
plugins {
   id("io.github.sgpublic.android-assemble") version "$version"
}
```

引入插件后，插件会为您的项目额外添加 Gradle Task，具体而言，项目中已有 `assemble${productFlavor}${buildType}` 用于打包指定 ProductFlavor、BuildType 的 apk 或 aar，则插件会为您额外添加 `assemble${productFlavor}${buildType}AndLocate`，默认配置下此任务会执行以下步骤：

1. 执行 `assembleMasterRelease` 完成打包；

2. 将打包后的文件移动至目录 `${rootProject}/assemble/${productFlavor}/` 并进行重命名。

   默认命名规则如下：

   - release：`${rootProject.name}-${project.name} V${versionName}(${versionCode})`
   - debug：`${rootProject.name}-${project.name}_${versionName}_${versionCode}`

3. 打开文件浏览器并高亮显示打包后的文件（暂时仅支持 Windows 和 macOS）。

### 针对 AAR

由于 AGP v4 对 `versionName` 和 `versionName` 的定义不会体现在 BuildConfig 中，因此推荐您使用 [libVersionCode/libVersionName](#libversioncodelibversionname) 定义。

## 自定义

插件允许您自定义一些配置，具体如下。

### 命名规则

由于归档后的打包文件会按 ProductFlavor 名称分类，则命名规则仅支持按 BuildType 设置。配置方法如下：

```kotlin
android {
    ...
    buildTypes {
        named("release") {
            renameRule {
                "${rootProject.name} V${versionName}(${versionCode})"
            }
        }
    }
}
```

其中 `renameRule` 上下文为 `RenameParam`：

```kotlin
data class RenameParam(
    val flavorType: String,
    val buildType: String,
    val versionName: String = "",
    val versionCode: Int = 1,
)
```

### 归档目录规则

可自定义打包文件归档目录，配置如下：

```kotlin
android {
    ...
}

assembleOption {
    outputDir = "./examble/path"
}
```

插件会在您设置的目录后追加 ProductFlavor 名称。

## 额外功能

README.md 中的注释可能随版本更新会与实际不同，具体请参阅源码。

### 辅助管理版本名称

[VersionGen.kt](./android-assemble/src/main/kotlin/io/github/sgpublic/androidassemble/util/VersionGen.kt)

```kotlin
package io.github.sgpublic.androidassemble.util

/** 辅助管理版本名称 */
object VersionGen {
    /**
     * git commit id，若当前不为 git 仓库，则返回 TIME_MD5，可用于版本名后缀
     * @see TIME_MD5
     */
    val GIT_HEAD: String

    /**
     * 按照 yyMMdd 的格式，根据当前日期返回一个整数，可用于版本号
     */
    val DATED_VERSION: Int

    /**
     * 按照 yyMMdd 的格式，根据最近一条 git commit 日期返回一个整数，若当前不为 git 仓库，则返回 DATED_VERSION，可用于版本号
     * @see DATED_VERSION
     */
    val COMMIT_VERSION: Int

    /**
     * 返回当前日期的 MD5，截取其中十位，与 git commit id 位数相等，可用于版本名后缀
     */
    val TIME_MD5: String
}
```

### buildConfigFiled

[_VariantDimension.kt](./android-assemble/src/main/kotlin/io/github/sgpublic/androidassemble/util/_VariantDimension.kt)

```kotlin
/**
 * 添加 buildConfigFiled，仅支持基础数据类型和 String
 */
fun VariantDimension.buildConfigField(name: String, value: Any)
```

### libVersionCode/libVersionName

[_LibDefaultConfg.kt](./android-assemble/src/main/kotlin/io/github/sgpublic/androidassemble/util/_LibDefaultConfg.kt)

```kotlin
package io.github.sgpublic.androidassemble.util

/** 为 com.android.library 添加版本号，并写入 BuildConfig.VERSION_CODE */
var DefaultConfig.libVersionCode: Int?

/** 为 com.android.library 添加版本名，并写入 BuildConfig.VERSION_NAME */
var DefaultConfig.libVersionName: String?
```
