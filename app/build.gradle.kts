import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-kapt")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    signingConfigs {
        create("mockup") {
            storeFile = file("../knowledge.jks")
            storePassword = "knowledge@prod"
            keyAlias = "knowledge"
            keyPassword = "knowledge@prod"
        }
        create("release") {
            storeFile = file("../knowledge.jks")
            storePassword = "knowledge@prod"
            keyAlias = "knowledge"
            keyPassword = "knowledge@prod"
        }
    }


    defaultConfig {
        applicationId = "com.applifehack.knowledge"
        minSdk = Versions.androidMinSdkVersion
        targetSdk = Versions.androidTargetSdkVersion
        compileSdk = Versions.androidCompileSdkVersion
        versionCode = 1
        multiDexEnabled = true
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lintOptions {
        //abortOnError = false
        disable("InvalidPackage", "MissingTranslation")
    }

    packaging {
        resources {
            excludes.add("META-INF/rxjava.properties")
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/license.txt")
            excludes.add("META-INF/NOTICE")
            excludes.add("META-INF/NOTICE.txt")
            excludes.add("META-INF/notice.txt")
            excludes.add("META-INF/ASL2.0")
            excludes.add("META-INF/core_release.kotlin_module")
            excludes.add("mozilla/public-suffix-list.txt")
        }
    }


    flavorDimensions("default")

    productFlavors {
        create("mockup") {
            applicationId = "com.applifehack.knowledge.admin.staging"
            buildConfigField("String", "API_YOUTUBE", "\"AIzaSyCzwn_65bFMiqB1pP-yI4DvfOrR_DJN7vo\"")
            buildConfigField("String", "URL_DYNAMIC_LINK", "\"https://applifehack.page.link\"")
            buildConfigField(
                "String",
                "DYNAMIC_PACKAGE_NAME",
                "\"com.applifehack.knowledge.staging\""
            )
            buildConfigField("String", "ENVIRONMENT", "\"mockup\"")
            resValue("string", "app_name", "Admin-Staging")
        }

        create("admin") {
            applicationId = "com.applifehack.knowledge.admin"
            buildConfigField("String", "API_YOUTUBE", "\"AIzaSyCzwn_65bFMiqB1pP-yI4DvfOrR_DJN7vo\"")
            buildConfigField("String", "URL_DYNAMIC_LINK", "\"https://applifehack.page.link\"")
            buildConfigField(
                "String",
                "DYNAMIC_PACKAGE_NAME",
                "\"com.applifehack.knowledge.admin\""
            )
            buildConfigField("String", "ENVIRONMENT", "\"admin\"")
            resValue("string", "app_name", "Admin")
        }

        create("production") {
            applicationId = "com.applifehack.knowledge"
            buildConfigField("String", "API_YOUTUBE", "\"AIzaSyCzwn_65bFMiqB1pP-yI4DvfOrR_DJN7vo\"")
            buildConfigField("String", "URL_DYNAMIC_LINK", "\"https://applifehack.page.link\"")
            buildConfigField("String", "DYNAMIC_PACKAGE_NAME", "\"com.applifehack.knowledge\"")
            buildConfigField("String", "ENVIRONMENT", "\"production\"")
            resValue("string", "app_name", "Knowledge")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }

    namespace = "com.applifehack.knowledge"
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(project(":core"))
    val appDependencies = Libs.dependencies
    
        implementation(appDependencies["play_services.analytics"]!!)
        implementation(appDependencies["play_services.gcm"]!!)
        implementation(appDependencies["play_services.auth"]!!)
        implementation(appDependencies["firebase.core"]!!)
        implementation(appDependencies["firebase.iid"]!!)
        implementation(appDependencies["firebase.messaging"]!!)
        implementation(appDependencies["firebase.jobdispatcher"]!!)
        implementation(appDependencies["firebase.database"]!!)
        implementation(appDependencies["firebase.fireStorage"]!!)
        implementation(appDependencies["facebook.facebookSdk"]!!)
        implementation(appDependencies["facebook.accountKit"]!!)
        implementation(appDependencies["facebook.stetho"]!!)
        implementation(appDependencies["facebook.stetho_okhttp3"]!!)
        implementation(appDependencies["circleImageView"]!!)
        implementation("com.makeramen:roundedimageview:2.2.1")
        implementation("com.github.rubensousa:gravitysnaphelper:2.2.2")
        implementation(appDependencies["dagger"]!!)
        implementation(appDependencies["daggerAndroid"]!!)
        implementation("androidx.appcompat:appcompat:1.1.0")
        implementation("androidx.constraintlayout:constraintlayout:1.1.3")
        implementation("com.google.android.material:material:1.0.0")
        kapt(appDependencies["daggerCompiler"]!!)
        kapt(appDependencies["daggerAndroidCompiler"]!!)
        implementation("com.androidhuman.rxfirebase2:firebase-firestore-kotlin:17.0.4.0")
        implementation("com.androidhuman.rxfirebase2:firebase-firestore:17.0.4.0")
        implementation("com.google.firebase:firebase-firestore:17.0.4")
        implementation("com.androidhuman.rxfirebase2:firebase-core:16.0.1.0")
        implementation("com.androidhuman.rxfirebase2:firebase-database:16.0.1.1")
        implementation("com.prof18.rssparser:rssparser:6.0.5")
        implementation("com.android.support:multidex:1.0.1")
        implementation("androidx.browser:browser:1.3.0-alpha04")
        implementation(appDependencies["Room"]!!)
        implementation(appDependencies["RxRoom"]!!)
        kapt(appDependencies["RoomCompiler"]!!)
        implementation(files("libs/YouTubeAndroidPlayerApi.jar"))
        implementation(appDependencies["timeAgo"]!!)
        implementation("com.google.apis:google-api-services-youtube:v3-rev183-1.22.0") {
            exclude(group = "org.apache.httpcomponents")
            exclude(group = "com.google.guava")
        }
        implementation("jp.wasabeef:blurry:3.0.0")
        implementation(appDependencies["play_services.ads"]!!)
        implementation("com.trello.rxlifecycle3:rxlifecycle-android-lifecycle:3.1.0")
        implementation("com.byoutline.observablecachedfield:observablecachedfield:1.7.0")
        implementation("org.sufficientlysecure:html-textview:3.9")
        implementation("com.google.firebase:firebase-dynamic-links-ktx:19.1.0")
        implementation("org.jsoup:jsoup:1.13.1")
        implementation("it.skrape:skrapeit:1.2.2")
        implementation("com.prof18.youtubeparser:youtubeparser:4.0.1")
        implementation("com.google.firebase:firebase-crashlytics:18.6.0")
        implementation(appDependencies["youtube"]!!)
    
}

repositories {
    maven {
        setUrl("https://jitpack.io")
    }
    mavenCentral()
    jcenter()
}