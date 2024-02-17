object Versions {
    const val KOTLIN = "1.8.0"
    const val COROUTINES = "1.5.0"
    const val RETROFIT = "2.9.0"
    const val GSON = "2.8.6"
    const val ROOM = "2.6.1"
    const val DAGGER_HILT = "2.44"
    const val LIFECYCLE = "2.7.0"
    const val DATASTORE = "1.0.0"
    const val NAVIGATION = "2.7.6"
    const val GLIDE = "4.9.0"
    const val CIRCLE_IMAGEVIEW = "3.0.0"
    const val CRASH_ANALYTICS = "18.6.0"

    //firebase
    const val firebaseBomVersion = "32.7.1"
    const val firebaseCoreVersion = "16.0.0"
    const val firebaseIidVersion = "17.0.4"
    const val firebaseMessagingVersion = "17.3.4"
    const val firebaseJobdispatcherVersion = "0.8.5"
    const val fireBasseDataBaseVersion = "19.2.0"
    const val fireBasseStorageVersion = "19.1.0"
    const val firebseFirebaseStorage = "21.3.0"

    //play-service
    const val play_serviceVersion = "16.0.0"
    const val play_service_authVersion = "16.0.1"
    const val ADS = "22.6.0"
    const val GOOGLE_IAP = "1.6.0"
    const val APP_UPDATE = "2.1.0"

    const val androidMinSdkVersion = 21
    const val androidTargetSdkVersion = 34
    const val androidCompileSdkVersion = 34

    //
    val lifecycle_version = "2.0.0-beta01"
    val supportVersion = "1.0.0-beta01"
    val constraintVersion = "1.1.2"
    val daggerVersion = "2.16"
    val okHttp = "3.13.1"
    val retrofit = "2.5.0"
    val rxJava = "2.1.9"
    val rxAndroid = "2.0.1"
    val stetho = "1.4.2"
    val roundedImageView = "2.3.0"
    val glide = "4.8.0"
    val MPAndroidChart = "v3.0.3"
    val QrGenerator = "2.4.0"
    val androidKTXVersion = "1.0.0-beta01"
    val materialVersion = "1.0.0"
    val timberVersion = "1.0.1"
    val multipleDexVersion = "2.0.0"
    val mapUtilVersion = "0.5+"
    val kotlinCoroutineVersion = "1.0.0"
    val fastAndroidNetWorkVersion = "1.0.2"
    val facebookVersion = "4.36.1"
    val stethoVersion = "1.5.0"
    val accountKitVersion = "4.37.0"
    val circleImageViewVersion = "2.1.0"
    val roomVersion = "2.6.1"
    val youtubeVersion = "12.1.0"

    // Firebase
//    val firebaseCoreVersion = "16.0.0"
//    val firebaseIidVersion = "17.0.4"
//    val firebaseMessagingVersion = "17.3.4"
//    val firebaseJobdispatcherVersion = "0.8.5"
//    val fireBasseDataBaseVersion = "19.2.0"
//    val fireBasseStorageVersion = "19.1.0"
//    val firebseFirebaseStorage = "21.3.0"

    // Play Services
//    val play_serviceVersion = "16.0.0"
//    val play_service_authVersion = "16.0.1"

    // Test
    val junitVersion = "1.0.0-beta02"
    val testRunnerVersion = "1.1.0-beta02"
    val esperessoVersion = "3.1.0-beta02"
    val mockitoKotlinVersion = "1.5.0"
    val coreVersion = "1.0.0-beta02"
    val gsonVersion = "2.8.1"
    val spinkitVersion = "1.1.0"
    val errorViewVersion = "4.0.0"
    val timeAgoVersion = "4.0.1"
    val playCoreVersion = "1.6.0"

}

object Libs {
    const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:8.2.0"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val HILT_GRADLE_PLUGIN =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.DAGGER_HILT}"
    const val SAFE_ARGS_PLUGIN = "androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:4.3.5"
    const val FIREBASE_CRASHLYTICS_GRADLE_PLUGIN =
        "com.google.firebase:firebase-crashlytics-gradle:2.9.9"

    const val ANDROID_CORE = "androidx.core:core-ktx:1.3.2"
    const val ANDROID_APPCOMPAT = "androidx.appcompat:appcompat:1.3.0-rc01"
    const val ANDROID_MATERIAL = "com.google.android.material:material:1.3.0"
    const val ANDROID_FRAGMENT = "androidx.fragment:fragment-ktx:1.6.2"


    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_GSON_CONVERTER = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:4.9.1"

    const val LIFECYCLE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:${Versions.LIFECYCLE}"

    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"

    const val DAGGER_HILT = "com.google.dagger:hilt-android:${Versions.DAGGER_HILT}"
    const val DAGGER_HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.DAGGER_HILT}"

    const val DATASTORE = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"

    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"

    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
    const val CIRCLE_IMAGEVIEW = "de.hdodenhof:circleimageview:${Versions.CIRCLE_IMAGEVIEW}"

    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.firebaseBomVersion}"
    const val FIREBASE_MESSAGING = "com.google.firebase:firebase-messaging"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics"

    const val PLAY_SERVICE_ADS = "com.google.android.gms:play-services-ads:${Versions.ADS}"
    const val GOOGLE_IN_APP_PURCHASE = "com.github.akshaaatt:Google-IAP:${Versions.GOOGLE_IAP}"
    const val PLAY_APP_UPDATE = "com.google.android.play:app-update-ktx:${Versions.APP_UPDATE}"

    val dependencies = mapOf(
        // Kotlin
        "kotlin" to "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}",
        "coreKtx" to "androidx.core:core-ktx:${Versions.androidKTXVersion}",
        "kotlinCoroutine" to "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutineVersion}",

        // ARCH
        "ArchLifeCycle" to "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle_version}",
        "ArchReactiveStreams" to "androidx.lifecycle:lifecycle-reactivestreams:${Versions.lifecycle_version}",
        "ArchJava8" to "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle_version}",

        // ROOM
        "Room" to "androidx.room:room-runtime:${Versions.roomVersion}",
        "RoomCompiler" to "androidx.room:room-compiler:${Versions.roomVersion}",
        "RxRoom" to "androidx.room:room-rxjava2:${Versions.roomVersion}",

        // Android Support
        "support" to "androidx.legacy:legacy-support-v4:${Versions.supportVersion}",
        "supportAppCompat" to "androidx.appcompat:appcompat:${Versions.supportVersion}",
        "supportDesign" to "com.google.android.material:material:${Versions.materialVersion}",
        "constraintLayout" to "androidx.constraintlayout:constraintlayout:${Versions.constraintVersion}",
        "supportCardView" to "androidx.cardview:cardview:${Versions.supportVersion}",
        "supportAnnotations" to "androidx.annotation:annotation:${Versions.supportVersion}",

        // Dagger
        "dagger" to "com.google.dagger:dagger:${Versions.daggerVersion}",
        "daggerCompiler" to "com.google.dagger:dagger-compiler:${Versions.daggerVersion}",
        "daggerAndroid" to "com.google.dagger:dagger-android-support:${Versions.daggerVersion}",
        "daggerAndroidCompiler" to "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}",

        // Retrofit
        "retrofit" to "com.squareup.retrofit2:retrofit:${Versions.retrofit}",
        "retrofitGson" to "com.squareup.retrofit2:converter-gson:${Versions.retrofit}",
        "retrofitRx" to "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}",

        // Fast Android Networking
        "fastNetwork" to "com.amitshekhar.android:rx2-android-networking:${Versions.fastAndroidNetWorkVersion}",

        // OkHttp
        "okHttpInterceptor" to "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}",

        // Stetho
        "stetho" to "com.facebook.stetho:stetho:${Versions.stetho}",
        "stetho_OkHttp" to "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}",

        // RXJava
        "rxJava" to "io.reactivex.rxjava2:rxjava:${Versions.rxJava}",

        // RXAndroid
        "rxAndroid" to "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}",

        // RoundedImageView
        "roundedImageView" to "com.makeramen:roundedimageview:${Versions.roundedImageView}",

        // Glide
        "glide" to "com.github.bumptech.glide:glide:${Versions.glide}",
        "glideCompiler" to "com.github.bumptech.glide:compiler:${Versions.glide}",

        // Persian Date Picker
        "datePicker" to ":datepicker}",

        // MP Android Chart
        "MPAndroidChart" to "com.github.PhilJay:MPAndroidChart:${Versions.MPAndroidChart}",

        // Qr Generator
        "QrGenerator" to "com.github.kenglxn.QRGen:android:${Versions.QrGenerator}",

        // Gson
        "gson" to "com.google.code.gson:gson:${Versions.gsonVersion}",

        // View
        "spinkit" to "com.github.ybq:Android-SpinKit:${Versions.spinkitVersion}",
        "circleImageView" to "de.hdodenhof:circleimageview:${Versions.circleImageViewVersion}",
        "errorView" to "tr.xip.errorview:library:${Versions.errorViewVersion}",

        // Log
        "timber" to "com.github.tony19:timber-loggly:${Versions.timberVersion}",
        "multipleDex" to "androidx.multidex:multidex:${Versions.multipleDexVersion}",

        // Firebase
        "firebase.core" to "com.google.firebase:firebase-core:${Versions.firebaseCoreVersion}",
        "firebase.iid" to "com.google.firebase:firebase-iid:${Versions.firebaseIidVersion}",
        "firebase.messaging" to "com.google.firebase:firebase-messaging:${Versions.firebaseMessagingVersion}",
        "firebase.jobdispatcher" to "com.firebase:firebase-jobdispatcher:${Versions.firebaseJobdispatcherVersion}",
        "firebase.database" to "com.google.firebase:firebase-database:${Versions.fireBasseDataBaseVersion}",
        "firebase.storage" to "com.google.firebase:firebase-storage:${Versions.fireBasseStorageVersion}",
        "firebase.fireStorage" to "com.google.firebase:firebase-firestore:${Versions.firebseFirebaseStorage}",
        "firebase.fireUiStorage" to "com.firebaseui:firebase-ui-storage:6.2.0",


        // Map
        "mapUtil" to "com.google.maps.android:android-maps-utils:${Versions.mapUtilVersion}",

        // Play Services
        "play_services.analytics" to "com.google.android.gms:play-services-analytics:${Versions.play_serviceVersion}",
        "play_services.gcm" to "com.google.android.gms:play-services-gcm:${Versions.play_serviceVersion}",
        "play_services.maps" to "com.google.android.gms:play-services-maps:${Versions.play_serviceVersion}",
        "play_services.location" to "com.google.android.gms:play-services-location:${Versions.play_serviceVersion}",
        "play_services.places" to "com.google.android.gms:play-services-places:${Versions.play_serviceVersion}",
        "play_services.auth" to "com.google.android.gms:play-services-auth:${Versions.play_service_authVersion}",
        "play_services.ads" to "com.google.android.gms:play-services-ads:${Versions.play_serviceVersion}",

        // Facebook
        "facebook.stetho" to "com.facebook.stetho:stetho:${Versions.stethoVersion}",
        "facebook.stetho_okhttp3" to "com.facebook.stetho:stetho-okhttp3:${Versions.stethoVersion}",
        "facebook.facebookSdk" to "com.facebook.android:facebook-android-sdk:${Versions.facebookVersion}",
        "facebook.accountKit" to "com.facebook.android:account-kit-sdk:${Versions.accountKitVersion}",


        // Others
        "timeAgo" to "com.github.marlonlom:timeago:${Versions.timeAgoVersion}",
        "playCore" to "com.google.android.play:core:${Versions.playCoreVersion}",
        "youtube" to "com.pierfrancescosoffritti.androidyoutubeplayer:core:${Versions.youtubeVersion}"
    )

    val testDependencies = mapOf(
        // JUnit
        "junit" to "androidx.test.ext:junit:${Versions.junitVersion}",

        // Support Test Runner
        "testRunner" to "androidx.test:runner:${Versions.testRunnerVersion}",

        // Espresso
        "esperesso" to "androidx.test.espresso:espresso-core:${Versions.esperessoVersion}",

        // Mockito
        "mockitoKotlin" to "com.nhaarman:mockito-kotlin-kt1.1:${Versions.mockitoKotlinVersion}",

        // Test Core
        "testCore" to "androidx.test:core:${Versions.coreVersion}",

        // Arch Testing
        "archTesting" to "androidx.arch.core:core-testing:${Versions.lifecycle_version}"
    )

}



