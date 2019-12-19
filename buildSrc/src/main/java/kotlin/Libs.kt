
object Libs {
    //Plugins
    const val android_gradle_plugin =
        "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    const val kotlin_gradle_plugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_gradle_plugin}"
    const val gradle_versions_plugin = "com.github.ben-manes.versions"

    //AndroidX
    const val androidx_app_compat = "androidx.appcompat:appcompat:${Versions.androidx_appcompat}"
    const val androidx_recyclerview =
        "androidx.recyclerview:recyclerview:${Versions.androidx_recyclerview}"
    const val androidx_cardview = "androidx.cardview:cardview:${Versions.androidx_cardview}"
    const val androidx_material =
        "com.google.android.material:material:${Versions.androidx_material}"
    const val androidx_core = "androidx.core:core-ktx:${Versions.ktx}"
    const val androidx_constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraint_layout}"
    const val androidx_fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val androidx_multidex = "androidx.multidex:multidex:${Versions.multidex}"
    const val flexbox_layout = "com.google.android:flexbox:${Versions.flexbox_layout}"
    const val pull_refresh_layout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.refresh_pull_layout}"
    const val transition_transition =
        "androidx.transition:transition:${Versions.transition_transition}"

    //Kotlin
    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlin_coroutines}"
    const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin_gradle_plugin}"

    //Koin
    const val koin_scope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koin_viewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

    //Lifecycle
    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycle_viewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    //Conductor
    const val conductor_controller = "com.bluelinelabs:conductor:${Versions.conductor_controller}"
    const val conductor_archlifecircle =
        "com.bluelinelabs:conductor-archlifecycle:${Versions.conductor_controller}"


    //Room
    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    //Networking
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val tikxml_annotation = "com.tickaroo.tikxml:annotation:${Versions.tikxml}"
    const val tikxml_core = "com.tickaroo.tikxml:core:${Versions.tikxml}"
    const val tikxml_converter = "com.tickaroo.tikxml:retrofit-converter:${Versions.tikxml}"
    const val tikxml_kapt = "com.tickaroo.tikxml:processor:${Versions.tikxml}"


    const val okhttp_logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp_logging_interceptor}"
    const val stetho = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"

    //Glide
    const val glide_runtime = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    //Cicerone
    const val cicerone = "ru.terrakok.cicerone:cicerone:${Versions.cicerone}"

    //AdapterDelegates
    const val adapter_delegates =
        "com.hannesdorfmann:adapterdelegates4:${Versions.adapter_delegates}"
    const val adapter_delegates_dsl =
        "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:${Versions.adapter_delegates}"

    //PermissionsDispatcher
    const val permissionsdispatcher =
        "org.permissionsdispatcher:permissionsdispatcher:${Versions.permissionsdispatcher}"
    const val permissionsdispatcherprocessor =
        "org.permissionsdispatcher:permissionsdispatcher-processor:${Versions.permissionsdispatcher}"

    //Timber
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    //Test
    const val junit = "junit:junit:${Versions.junit}"
    const val junit_ext = "androidx.test.ext:junit:${Versions.test_ext_junit}"
    const val runner = "androidx.test:runner:${Versions.test_runner}"
    const val rules = "androidx.test:rules:${Versions.test_rules}"
    const val mockito_kotlin =
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockito_kotlin}"
    const val assertj = "org.assertj:assertj-core:${Versions.assertj_core}"
    const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.arch_core_test}"
    const val kotlin_reflect =
        "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin_gradle_plugin}"
    // android test
    const val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.test_espresso}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.test_espresso}"
    const val espresso_intents = "androidx.test.espresso:espresso-intents:${Versions.test_espresso}"
    const val mockito_core = "org.mockito:mockito-core:${Versions.mockito_core}"
    const val kotlinx_coroutines_test =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx_coroutines_test}"
}