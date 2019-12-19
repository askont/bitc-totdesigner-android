// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven("http://repository.jetbrains.com/all")
        maven("https://maven.fabric.io/public")
        maven("https://www.jitpack.io")
    }
    dependencies {
        classpath(Libs.android_gradle_plugin)
        classpath(Libs.kotlin_gradle_plugin)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven("http://repository.jetbrains.com/all")
    }
}

plugins {
    id(Libs.gradle_versions_plugin) version Versions.gradle_versions_plugin
}

task("clean") {
    delete(rootProject.buildDir)
}