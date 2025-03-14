plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
    id("maven-publish")
    id("signing")
}

group = "io.projectliberty"
version = System.getenv("RELEASE_VERSION") ?: "0.0.0-SNAPSHOT"

android {
    namespace = "io.projectliberty.siwf"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        aarMetadata {
            minCompileSdk = 24
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "11"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.projectliberty"
            artifactId = "siwf"
            version = System.getenv("RELEASE_VERSION") ?: "0.0.0-SNAPSHOT"
            pom {
                name.set("SIWF")
                description.set("SIWF SDK for Android")
                url.set("https://github.com/ProjectLibertyLabs/siwf-sdk-android/")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ProjectLibertyLabs/siwf-sdk-android.git")
                    developerConnection.set("scm:git:ssh://github.com/ProjectLibertyLabs/siwf-sdk-android.git")
                    url.set("https://github.com/ProjectLibertyLabs/siwf-sdk-android/")
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        System.getenv("GPG_SECRET_KEY"),
        System.getenv("GPG_PASSPHRASE")
    )
    sign(publishing.publications)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
