plugins {
    id("android-library-base")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {

    buildTypes {
        getByName("debug"){
            buildConfigField("String", "ADS_APP_OPEN_ID", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField("String", "ADS_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "ADS_FULL_SCREEN_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "ADS_NATIVE_ID", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "ADS_REWARD", "\"ca-app-pub-3940256099942544/5224354917\"")

        }
        getByName("release"){
            buildConfigField("String", "ADS_APP_OPEN_ID", "\"ca-app-pub-6432501233751447/5271984268\"")
            buildConfigField("String", "ADS_BANNER_ID", "\"ca-app-pub-6432501233751447/7000795065\"")
            buildConfigField("String", "ADS_FULL_SCREEN_ID", "\"ca-app-pub-6432501233751447/6585065930\"")
            buildConfigField("String", "ADS_NATIVE_ID", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "ADS_REWARD", "\"ca-app-pub-6432501233751447/3958902595\"")

        }
    }
    

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(Libs.ANDROID_CORE)

    implementation(Libs.DAGGER_HILT)
    kapt(Libs.DAGGER_HILT_COMPILER)

    implementation(Libs.COROUTINES_CORE)
    implementation(Libs.COROUTINES_ANDROID)
    implementation(Libs.PLAY_SERVICE_ADS)
}