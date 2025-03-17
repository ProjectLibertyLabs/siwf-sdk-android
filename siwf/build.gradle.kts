plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jreleaser)
    id("maven-publish")
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
            afterEvaluate {
                from(components["release"])
            }

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
                developers {
                    developer {
                        id.set("claire.clark")
                        name.set("Claire Clark")
                    }
                    developer {
                        id.set("wil.wade")
                        name.set("Wil Wade")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ProjectLibertyLabs/siwf-sdk-android.git")
                    developerConnection.set("scm:git:ssh://github.com/ProjectLibertyLabs/siwf-sdk-android.git")
                    url.set("https://github.com/ProjectLibertyLabs/siwf-sdk-android/")
                }
            }
        }
    }

    repositories {
        maven {
            name = "PreDeploy"
            url = uri(layout.buildDirectory.dir("pre-deploy"))
        }
    }
}

jreleaser {
    gitRootSearch.set(true)

    release {
        github {
            //Requires env variable: JRELEASER_GITHUB_TOKEN
            update {
                enabled.set(true)
                sections.set(setOf(
                    org.jreleaser.model.UpdateSection.TITLE,
                    org.jreleaser.model.UpdateSection.BODY,
                    org.jreleaser.model.UpdateSection.ASSETS
                ))
            }

            prerelease {
                enabled.set(true)
                pattern.set(".*-(snapshot|SNAPSHOT)\$")
            }
        }
    }

    signing {
        active.set(org.jreleaser.model.Active.ALWAYS)
        mode = org.jreleaser.model.Signing.Mode.FILE
        publicKey = "./signing-public-key.asc"
        secretKey = "./signing-secret-key.gpg"
        armored.set(true)
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    //Requires env variables
                    // JRELEASER_MAVENCENTRAL_SONATYPE_USERNAME
                    // JRELEASER_MAVENCENTRAL_SONATYPE_TOKEN
                    active.set(org.jreleaser.model.Active.ALWAYS)
                    url = "https://central.sonatype.com/api/v1/publisher"
                    snapshotSupported = true
                    stagingRepository("build/pre-deploy")
                    sign = true
                    verifyPom = false
                    applyMavenCentralRules = true
                }
            }
        }
    }
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