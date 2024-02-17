plugins {
    id("android-library-base")
}

android {
    namespace = "com.ezyplanet.core"
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    val appDependencies = Libs.dependencies
    val testDependencies = Libs.testDependencies


    api(appDependencies["kotlin"]!!)
    api(appDependencies["coreKtx"]!!)
    api(appDependencies["kotlinCoroutine"]!!)

    // Arch
    api(appDependencies["ArchLifeCycle"]!!)
    api(appDependencies["ArchReactiveStreams"]!!)

    // Support
    api(appDependencies["support"]!!)
    api(appDependencies["supportAppCompat"]!!)
    api(appDependencies["supportDesign"]!!)
    api(appDependencies["supportCardView"]!!)
    api(appDependencies["constraintLayout"]!!)

    // Room
    api(appDependencies["Room"]!!)
    api(appDependencies["RxRoom"]!!)
    kapt(appDependencies["RoomCompiler"]!!)

    // Glide
    api(appDependencies["glide"]!!)
    kapt(appDependencies["glideCompiler"]!!)

    // Dagger
    implementation(appDependencies["dagger"]!!)
    implementation(appDependencies["daggerAndroid"]!!)
    kapt(appDependencies["daggerCompiler"]!!)
    kapt(appDependencies["daggerAndroidCompiler"]!!)

    // Rx
    api(appDependencies["rxJava"]!!)
    api(appDependencies["rxAndroid"]!!)

    api(appDependencies["gson"]!!)

    // View
    api(appDependencies["spinkit"]!!)
    api(appDependencies["fastNetwork"]!!)
    api(appDependencies["errorView"]!!)

    // api("com.facebook.android:facebook-android-sdk:${appDependencies["facebookVersion"]}")
    api(appDependencies["multipleDex"]!!)

    // Firebase
    api(appDependencies["firebase.fireUiStorage"]!!)

    api(appDependencies["timber"]!!)
    api(Libs.PLAY_APP_UPDATE)

    testImplementation(testDependencies["junit"]!!)
    testImplementation(testDependencies["mockitoKotlin"]!!)
    testImplementation(testDependencies["archTesting"]!!)

    androidTestImplementation(testDependencies["testCore"]!!)
    androidTestImplementation(testDependencies["testRunner"]!!)
    androidTestImplementation(testDependencies["esperesso"]!!)

    androidTestImplementation("androidx.test:rules:1.1.0-beta02")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${appDependencies["kotlin_version"]}")

    implementation("com.tapadoo.android:alerter:4.0.0")
}

repositories {
    maven {
        url = uri("https://jitpack.io")
    }
    mavenCentral()
}