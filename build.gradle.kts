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
        classpath(Libs.plugins_android_junit5)
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
    id("com.github.ben-manes.versions") version "0.27.0"
}

task("clean") {
    delete(rootProject.buildDir)
}