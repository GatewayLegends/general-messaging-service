[![](https://jitpack.io/v/sotware-supremacy/General-Messaging-Service.svg)](https://jitpack.io/#sotware-supremacy/General-Messaging-Service)

# General Messaging Service (GMS)
> **for Google & Huawei**


## Installation
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```

Step 2. Add the dependency

```gradle
dependencies {
	        implementation 'com.github.sotware-supremacy:General-Messaging-Service:<VERSION>'
}
```

## Usage
> **put this in AndroidManifest.xml**
```xml
<application>
    ...
    
    <!-- For Google Services-->
           
    <service
        android:name="com.gateway.gms.data.GoogleService"
        android:exported="false">
        <intent-filter>
            <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
    </service>


    <!-- For Huawei Services-->
    
    <service
        android:name="com.gateway.gms.data.HuaweiService"
        android:exported="false">
        <intent-filter>
            <action android:name="com.huawei.push.action.MESSAGING_EVENT" />
        </intent-filter>
    </service>
</application>
```

> **Don't miss to put authentication json file in app package.**

```
app/
    google-services.json
    [OR]
    huawei-services.json
```
    
