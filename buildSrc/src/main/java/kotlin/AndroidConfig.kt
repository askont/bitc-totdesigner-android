object AndroidConfig {
    const val COMPILE_SDK_VERSION = 29
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 29
    const val BUILD_TOOLS_VERSION = "29.0.0"

    const val VERSION_CODE: Int = 1
    const val VERSION_NAME: String = "0.0.3"

    const val APPLICATION_ID = "ru.bitc.totdesigner"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    const val TEST_RUNNER_BUILDER = "de.mannodermaus.junit5.AndroidJUnit5Builder"
    const val SUPPORT_LIBRARY_VECTOR_DRAWABLES = true
    const val MULTI_DEX_ENABLED = true

    const val APK_NAME = "TotDesigner-$VERSION_NAME($VERSION_CODE)"
}