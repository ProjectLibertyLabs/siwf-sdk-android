# **Sign-In With Frequency (SIWF) SDK For Android**

🚀 **[Sign-In With Frequency (SIWF)](https://github.com/ProjectLibertyLabs/siwf)** is an authentication SDK designed for seamless integration with mobile apps.

## **Installation**

### Requirements

Android API version 24 or later and Java 11+.

Here’s what you need in `build.gradle` to target Java 11 byte code for Android and Kotlin plugins respectively.

```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = '11'
    }
}
```

### Installation


To install SIWF for Android with [Gradle](https://gradle.org/), simply add the following line to your `build.gradle` file and update the version line to the [latest version](https://github.com/ProjectLibertyLabs/siwf-sdk-android/releases):

```gradle
dependencies {
    implementation 'io.projectliberty:siwf:1.0.0'
}
```

## **Usage**
When you decide to use the SIWF SDK in your own app, follow the steps before for easy integration:

### **1️⃣ Display the SIWF Sign-In Button**
Use `Siwf.CreateSignInButton` in your UI:

```kotlin
    import io.projectliberty.siwf.Siwf
    import io.projectliberty.models.SiwfButtonMode

    Siwf.CreateSignInButton(
        mode = SiwfButtonMode.PRIMARY,
        authRequest = authRequest
    )
```

### **2️⃣ Handle Authorization Callbacks**
Make sure your `AndroidManifest.xml` includes intent filters:

```kotlin
    <activity
        android:name="io.projectliberty.helpers.AuthCallbackActivity"
        android:exported="true"
        android:launchMode="singleTask">

        <!-- HTTP Callback Example. Requires a Verified App Link: https://developer.android.com/training/app-links/verify-android-applinks -->
        <intent-filter android:autoVerify="true">
          <action android:name="android.intent.action.VIEW" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <data
          android:scheme="http"
          android:host="localhost"
          android:port="3000"
          android:path="/login/callback" />
        </intent-filter>

        <!-- Custom Schema Support Example -->
        <intent-filter android:autoVerify="true">
          <action android:name="android.intent.action.VIEW" />
          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <data
          android:scheme="siwfdemoapp"
          android:host="login" />
        </intent-filter>
    </activity>
```

Then, use the `AuthReceiver` in your main activity:
```kotlin
    var receivedAuthorizationCode by remember { mutableStateOf<String?>(null) }
    val filter = IntentFilter("io.projectliberty.helpers.AUTH_RESULT")
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            receivedAuthorizationCode = intent?.getStringExtra("authorizationCode")
            Log.d("HostApp", "✅ Received Auth Code: $receivedAuthorizationCode")
        }
    }

    ContextCompat.registerReceiver(
        this,
        receiver,
        filter,
        ContextCompat.RECEIVER_NOT_EXPORTED
    )
```

## **Contributing**
To contribute:
- Fork the repo and create a feature branch.
- Make changes and test.
- Submit a pull request with details.

## Release

Use GitHub to create a release.
That will trigger CI to do the release and update with [jreleaser](https://jreleaser.org/guide/latest/tools/jreleaser-gradle.html).

### Example Release Steps

1. Set the environment variable: `RELEASE_VERSION` to `x.y.z` or `x.y.z-SNAPSHOT`
2. Show config: `RELEASE_VERSION="1.0.0" ./gradlew siwf:jreleaserConfig --dryrun --full`
3. Staging build `RELEASE_VERSION="1.0.0" ./gradlew siwf:publishReleasePublicationToPreDeployRepository`
4. Dry run `RELEASE_VERSION="1.0.0" ./gradlew siwf:jreleaserFullRelease --dryrun`

### Release Secrets
Can be set in `/siwf/.env`
```
# Release Username for Maven Central / SonaType
JRELEASER_MAVENCENTRAL_SONATYPE_USERNAME=<replace>
# Release Token for Maven Central / SonaType
JRELEASER_MAVENCENTRAL_SONATYPE_TOKEN=<replace>

# Release GitHub Token with Permissions
JRELEASER_GITHUB_TOKEN=<replace>

# Release Signing GPG Passphrase
JRELEASER_GPG_PASSPHRASE=<replace>
# Release Signing GPG Public Key (base64 encoded)
```

### GPG Signing Key Rotation

1. Generate new key
2. Export key and commit `gpg --armor --export <KEY ID> > ./siwf/signing-public-key.asc`
3. Update GitHub env vars with the Secret Key `gpg --armor --export-secret-key <KEY ID>`