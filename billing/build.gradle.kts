import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    id("android-library-base")
}

android {
    
    val prop = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "security.properties")))
    }


    buildTypes {
        getByName("debug"){
            val licence_key: String = prop.getProperty("licence_key_staging")
            buildConfigField("String", "LICENCE_KEY", licence_key)

        }
        getByName("release"){
            val licence_key: String = prop.getProperty("licence_key")
            buildConfigField("String", "LICENCE_KEY", licence_key)

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
    implementation(Libs.GOOGLE_IN_APP_PURCHASE)
}