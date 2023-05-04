# AndroidAssemblePlugin

The plugin in this repository are designed to help you assist in finding the packaged APK or AAR files, while minimizing the modifications required to your project.

Here are the plugin included in this repository and their descriptions:

## `android-assemble`

This plugin is used to assist in packaging APK and AAR files for Android projects.

### Setup

```kotlin
plugins {
    id("io.github.sgpublic.android-assemble") version "0.1.0"
}
```

Then add the following code to your `build.gradle.kts`:

**_TODO: This code will be built into the plugin in future versions, and you will be able to remove it at that time._**

```kotlin
android.applicationVariants.all {
    for (output in outputs) {
        if (output !is BaseVariantOutputImpl) {
            continue
        }
        val name = output.name.split("-")
            .joinToString("") { it.capitalize() }
        val taskName = "assemble${name}AndLocate"
        tasks.register(taskName) {
            dependsOn("assemble${name}")
            doLast {
                ApkUtil.assembleApkAndLocate(output.name, output.outputFile, "./build/assemble")
            }
        }
    }
}
```

Now you can then run the Gradle task `assembleXxxAndLocate` to package the APK or AAR. Once the packaging is complete, the plugin will move the packaged APK or AAR to the `build` folder in the root directory of your project and automatically open `explorer.exe` for you.