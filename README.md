# **Sign-In With Frequency (SIWF) SDK For Android**

🚀 **[Sign-In With Frequency (SIWF)](https://github.com/ProjectLibertyLabs/siwf)** is an authentication SDK designed for seamless integration with mobile apps.

## **Installation**

### **Step 1: Clone the Repository**
```bash
git clone https://github.com/ProjectLibertyLabs/liwl-sdk-android.git
cd liwl-sdk-android
```

### **Step 2: Add Dependencies**
Add any missing dependencies to your `build.gradle.kts`:

[//]: # (TODO: Figure out if you need to do this or not.)

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

### **2️⃣ Handle Authentication Callbacks**
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
    var receivedAuthCode by remember { mutableStateOf<String?>(null) }
    val filter = IntentFilter("io.projectliberty.helpers.AUTH_RESULT")
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            receivedAuthCode = intent?.getStringExtra("authorizationCode")
            Log.d("HostApp", "✅ Received Auth Code: $receivedAuthCode")
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