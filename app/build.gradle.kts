import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {

    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

    defaultConfig {
        applicationId = AndroidConfig.APPLICATION_ID
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)
        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME
        multiDexEnabled = AndroidConfig.MULTI_DEX_ENABLED
        vectorDrawables.useSupportLibrary = AndroidConfig.SUPPORT_LIBRARY_VECTOR_DRAWABLES
        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
        testInstrumentationRunnerArgument("runnerBuilder", AndroidConfig.TEST_RUNNER_BUILDER)
        setProperty("archivesBaseName", AndroidConfig.APK_NAME)

        androidExtensions {
            isExperimental = true
        }

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("String", "ENDPOINT", Config.SERVER_ENDPOINT)
        }
        getByName("debug") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("String", "ENDPOINT", Config.SERVER_ENDPOINT)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/LICENSE*")
    }


    kotlinOptions {
        val options = this as? KotlinJvmOptions
        options?.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //AndroidX
    implementation(Libs.androidx_app_compat)
    implementation(Libs.androidx_constraintlayout)
    implementation(Libs.androidx_core)
    implementation(Libs.androidx_material)
    implementation(Libs.androidx_recyclerview)
    implementation(Libs.androidx_cardview)
    implementation(Libs.androidx_fragment)

    //Koin
    implementation(Libs.koin_scope)
    implementation(Libs.koin_viewmodel)

    // cicerone
    implementation(Libs.cicerone)

    //lifecycle
    implementation(Libs.lifecycle_extensions)
    implementation(Libs.lifecycle_livedata)
    implementation(Libs.lifecycle_viewmodel)

    //Glide
    implementation(Libs.glide_runtime)
    kapt(Libs.glide_compiler)

    //adapterDelegat
    implementation(Libs.adapter_delegates)
    implementation(Libs.adapter_delegates_dsl)

    //room
    implementation(Libs.room_runtime)
    implementation(Libs.room_ktx)
    implementation(Libs.room_testing)
    kapt(Libs.room_compiler)
    //Networking
    implementation(Libs.tikxml_annotation)
    implementation(Libs.tikxml_core)
    kapt(Libs.tikxml_kapt)
    implementation(Libs.okhttp_logging_interceptor)
    implementation(Libs.retrofit)
    implementation(Libs.tikxml_converter)

    // coroutines
    implementation(Libs.coroutines_android)
    implementation(Libs.coroutines)
    // logger
    implementation(Libs.stetho)

    //Timber
    implementation(Libs.timber)

    // test
    testImplementation(Libs.kotlinx_coroutines_test)
    testImplementation(Libs.junit4)
    testImplementation(Libs.assertj)
    testImplementation(Libs.junit_ext)
    testImplementation(Libs.mockito_core)
    testImplementation(Libs.mockito_kotlin)
    testImplementation(Libs.koin_test)
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinCompile::class).all {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xuse-experimental=kotlinx.coroutines.ObsoleteCoroutinesApi"
        )
    }
}