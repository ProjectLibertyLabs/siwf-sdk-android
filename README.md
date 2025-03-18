# **Sign-In With Frequency (SIWF) SDK For Android**

🚀 **[Sign-In With Frequency (SIWF)](https://github.com/ProjectLibertyLabs/siwf)** is an authentication SDK designed for seamless integration with mobile apps.

This repository contains the local SIWF SDK and Demo App, for seamless spinup and as a reference for how to use the SIWF SDK in your app.

## 📌 **Index**
1. 🚀 [Getting Started - SIWF SDK Demo App](#getting-started---siwf-sdk-demo-app)
2. 📝 [Getting Started - SIWF SDK For Your App](#getting-started---siwf-sdk-for-your-app)
3. 🛠 [Usage For Your App](#usage-for-your-app)
4. 🤝 [Contributing](#contributing)
5. 📦 [Release](#📦-release)

## 🚀 **Getting Started - SIWF SDK Demo App**

Follow these steps to set up and run the Kotlin app:  

### 1️⃣ Install Android Studio  
If you haven't already, download and install [Android Studio](https://developer.android.com/studio) to set up your development environment.  

### 2️⃣ Clone the Repository  
Run the following command in your terminal to clone the repository:  
```sh
git clone git@github.com:ProjectLibertyLabs/siwf-sdk-android.git
cd siwf-sdk-android
```

### 3️⃣ Open the Project in Android Studio  
Open the cloned project and wait for the Gradle sync to complete.

### 4️⃣ Run the App  
- Connect an Android device or start an emulator  
- Click **Run ▶** in Android Studio

Your SIWF SDK Demo App should now be running! 🚀

## 📝 **Getting Started - SIWF SDK For Your App**

### ⚙️ Requirements

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

### 📥 Installation


To install SIWF for Android with [Gradle](https://gradle.org/), simply add the following line to your `build.gradle` file and update the version line to the latest version.
- [GitHub Releases](https://github.com/ProjectLibertyLabs/siwf-sdk-android/releases)
- [Maven](https://central.sonatype.com/artifact/io.projectliberty/siwf)

```gradle
dependencies {
    implementation 'io.projectliberty:siwf:1.0.0'
}
```

## 🛠 **Usage For Your App**
When you decide to use the SIWF SDK in your own app, follow the steps below for easy integration:

### **1️⃣ Define an `authRequest`**
- Refer to the Demo App for examples of encoded and non-encoded requests.
- To create your own, use [Frequency's Signed Request Generator](https://projectlibertylabs.github.io/siwf/v2/docs/Generate.html).

### **2️⃣ Display the SIWF Sign-In Button**
Use `Siwf.CreateSignInButton` to create a SIWF Button in your UI:

```kotlin
    import io.projectliberty.siwf.Siwf
    import io.projectliberty.models.SiwfButtonMode

    Siwf.CreateSignInButton(
        mode = SiwfButtonMode.PRIMARY,
        authRequest = authRequest
    )
```

### **3️⃣ Handle Authorization Callbacks**
Make sure your `AndroidManifest.xml` includes intent filters:

Intent filters define how your app responds to certain system-wide events or external requests, such as opening a URL or handling authentication callbacks. They allow your app to register for specific actions and handle incoming data accordingly. 

- **HTTP Callback Example**: This intent filter enables your app to receive authentication responses via HTTP links. It requires a verified app link so that the system knows which app should handle the request.
- **Custom Schema Support Example**: This intent filter lets your app handle custom URL schemes (e.g., `siwfdemoapp://login`), allowing seamless deep linking from external sources.

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

Then, use the `BroadcastReceiver` in your main activity:

The `BroadcastReceiver` listens for authentication results and extracts the authorization code after a sign-in attempt, enabling the authentication flow in your app.

```kotlin
    setContent {
        var authorizationCode by remember { mutableStateOf<String?>(null) }
    
        val authReceiver = remember {
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val receivedCode = intent?.getStringExtra(AuthConstants.AUTH_INTENT_KEY)
                    authorizationCode = receivedCode
                    // Process the authorizationCode by sending it it your backend servers
                    // See https://projectlibertylabs.github.io/siwf/v2/docs/Actions/Response.html
                }
            }
        }
    
        val authFilter = IntentFilter(AuthConstants.AUTH_RESULT_ACTION)
    
        ContextCompat.registerReceiver(
            this,
            authReceiver,
            authFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    
        // Render UI content
    }
```
### **4️⃣ Process Authorization Code**

On your backend services process the authorization code and start your session.

Resources:
- [SIWF Documentation on Processing a Result](https://projectlibertylabs.github.io/siwf/v2/docs/Actions/Response.html)
- [Frequency Gateway SSO Tutorial](https://projectlibertylabs.github.io/gateway/GettingStarted/SSO.html)

## 🤝 **Contributing**
To contribute:
- Fork the repo and create a feature branch.
- Make changes and test.
- Submit a pull request with details.

## 📦 **Release**

Use GitHub to create a release.
That will trigger CI to do the release and update with [jreleaser](https://jreleaser.org/guide/latest/tools/jreleaser-gradle.html).

### Example Release Steps

1. Set the environment variable: `RELEASE_VERSION` to `x.y.z` or `x.y.z-SNAPSHOT`
2. Show config: `RELEASE_VERSION="1.0.0" ./gradlew siwf:jreleaserConfig --dryrun --full`
3. Staging build `RELEASE_VERSION="1.0.0" ./gradlew siwf:publishReleasePublicationToPreDeployRepository`
4. Dry run `RELEASE_VERSION="1.0.0" ./gradlew siwf:jreleaserFullRelease --dryrun`
5. Full `RELEASE_VERSION="1.0.0" ./gradlew siwf:jreleaserFullRelease --dryrun`

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
3. Update GitHub Actions Secret `GPG_SECRET_KEY_BASE64` with the Secret Key `gpg --armor --export-secret-key <KEY ID> | base64 -w 0`
4. Update the GPG Passphrase GitHub Actions Secret: `JRELEASER_GPG_PASSPHRASE`
