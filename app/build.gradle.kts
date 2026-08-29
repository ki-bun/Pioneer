val hasSigning = project.hasProperty("PIONEER_STORE_PASSWORD") && project.hasProperty("PIONEER_KEY_ALIAS") && project.hasProperty("PIONEER_KEY_PASSWORD")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.ki_bun.pioneer"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    dependenciesInfo {
		includeInApk = false
		includeInBundle = false
	}

	signingConfigs {
		if (hasSigning) {
			create("release") {
				storeFile = file("pioneer.jks")
				storePassword = project.findProperty("PIONEER_STORE_PASSWORD") as String
				keyAlias = project.findProperty("PIONEER_KEY_ALIAS") as String
				keyPassword = project.findProperty("PIONEER_KEY_PASSWORD") as String
			}
		}
	}

    buildTypes {
        release {
            isMinifyEnabled = false
			if (hasSigning) {
            signingConfig = signingConfigs.getByName("release")
			}
		}

    }

    defaultConfig {
        applicationId = "com.ki_bun.pioneer"
        minSdk = 24
        targetSdk = 36
        versionCode = 14
        versionName = "2.7.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

ksp {
    arg("room.schemaLocation","$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
    val nav_version = "2.9.8"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    implementation("com.opencsv:opencsv:5.12.0")
    implementation("io.coil-kt.coil3:coil-compose:3.4.0")
    implementation("androidx.core:core-splashscreen:1.2.0")
}
