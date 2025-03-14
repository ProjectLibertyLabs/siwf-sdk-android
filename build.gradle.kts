// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("org.jreleaser") version "1.17.0"
}


jreleaser {
    signing {
        active.set(org.jreleaser.model.Active.ALWAYS)
        armored.set(true)
    }

    deploy {
        maven {
            pomchecker {
                version = "1.14.0"
                failOnWarning = true
                failOnError = true
            }
            mavenCentral {
                create("sonatype") {
                    active = ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    snapshotSupported = true
                    stagingRepository("build/staging-deploy")
                    verifyPom = true
                    applyMavenCentralRules = true
                }
            }
        }
    }
}