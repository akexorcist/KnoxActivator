# Android-KnoxActivator
Samsung's Knox Standard activation helper library for Android
[What's Samsung Knox SDK?](https://seap.samsung.com/license-keys/create/knox-android)

What's new in v2.0.1
===============================
Update to Knox SDK v25 with backward-compatible library 


Download
===============================

Maven
```
<dependency>
  <groupId>com.akexorcist</groupId>
  <artifactId>knoxactivator</artifactId>
  <version>2.0.1</version>
</dependency>
```

Gradle
```groovy
implementation 'com.akexorcist:knoxactivator:2.0.1'
```

Public Method
===============================
```java
// Setup
void register(Context context, ActivationCallback callback)
void unregister(Context context)
void onActivityResult(int requestCode, final int resultCode, Intent data)

// Activate / Deactivate
void activateDeviceAdmin(Activity activity, String description)
boolean deactivateDeviceAdmin(Context context)
void activateKnoxLicense(Context context, String licenseKey)
void deactivateKnoxLicense(Context context, String licenseKey)
void activateBackwardLicense(Context context, String licenseKey)

// Status Checking
boolean isDeviceAdminActivated(Context context)
boolean isKnoxSdkSupported()
boolean isMdmApiSupported(int requiredVersion)
boolean isLegacySdk()
```

Usage
===============================

### Setup
Required methods in Activity class for Knox Standard activation
```java
public class YourActivity extends AppCompatActivity {

    // ...

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // ...
        KnoxActivationManager.getInstance().register(this, activationCallback);
    }

    @Override
    public void onDestroy() {
        // ...
        KnoxActivationManager.getInstance().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ...
        KnoxActivationManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
```

### Activate & Deactivate method
```java
// Device Administration
String deviceAdminRequestDescription = "Enable Device Administration is required";
KnoxActivationManager.getInstance().activateDeviceAdmin(getActivity(), deviceAdminRequestDescription);
KnoxActivationManager.getInstance().deactivateDeviceAdmin(getContext());

// Knox SDK
String knoxLicenseKey = "YOUR_KNOX_LICENSE_KEY";
KnoxActivationManager.getInstance().activateKnoxLicense(getContext(), knoxLicenseKey);

// Backward-compatible for SDK v2.7 or lower
String backwardLicenseKey = "YOUR_BACKWARD_LICENSE_KEY";
KnoxActivationManager.getInstance().activateBackwardLicense(getContext(), backwardLicenseKey);
```
How to get the license key?
https://seap.samsung.com/license-keys/create/knox-android

If your app ...

Only work with Knox SDK v2.8 or higher 
- Use only Knox License Key

All Knox SDK version supporting
- Use Knox license key and Backward-compatible license key

*Note - Knox SDK version depends on the device.* 

### Status checking method
Utility methods 
```java
KnoxActivationManager manager = KnoxActivationManager.getInstance();

// Is Device Administration activated?
boolean isDeviceAdminActivated = manager.isDeviceAdminActivated(getContext());

// Is Knox SDK Supported?
boolean isKnoxSdkSupported = manager.isKnoxSdkSupported();

// Is current MDM API version supported?
int sdkVersion = EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_7;
boolean isMdmApiSupported = manager.isMdmApiSupported(sdkVersion);

// Is device run on legacy SDK version (deviceVersion <= v2.7)
boolean isLegacySdk = manager.isLegacySdk()
```

### ActivationCallback
Callback when activate/deactivate method called
```java
ActivationCallback activationCallback = new ActivationCallback() {
        @Override
        public void onDeviceAdminActivated() {
            // ...
        }

        @Override
        public void onDeviceAdminActivationCancelled() {
            // ...
        }

        @Override
        public void onKnoxLicenseActivated() {
            // ...
        }

        @Override
        public void onBackwardLicenseActivated() {
            // ...
        }

        @Override
        public void onLicenseActivateFailed(int errorType, String errorMessage) {
            // ...
        }
    };
```

Migrate from v1 to v2
===========================
• Change `register()` to `register(Context context)`  
• Move register method from `onStart()` to `onCreate(Bundle savedInstanceState)`
• Change `unregister()` to `unregister(Context context)`
• Move unregister method from `onStop()` to `onDestroy()`
• Change `activeDeviceAdmin(Activity activity)` to `activateDeviceAdmin(Activity activity, String description)`. The description message will display on Device Administration request screen.
• For SDK v2.8 or higher only : Change `activateLicense(Context context, String licenseKey)` to `activateKnoxLicense(Context context, String licenseKey)` with Knox SDK license key
• For all SDK version support : You need to activate the both Knox license and backward-compatible license. Activate the Knox license with `activateKnoxLicense(Context context, String licenseKey)` by Knox SDK license key, then active the backward-compatible license key with `activateBackwardLicense(Context context, String licenseKey)` by backward-compatible license kry.
• No Knox policy required in XML resource anymore. You have to declare as Knox SDK permission with `<uses-permission ... />` in your AndroidManifest.xml. [More detail](https://seap.samsung.com/license-keys/manifest-knox-permissions) 


ProGuard
===========================
```
# Samsung Knox SDK
-dontwarn com.samsung.**
-keep class com.samsung.** { *; }
-keep interface com.samsung.** { *; }
-keep enum com.samsung.** { *; }
-keepclassmembers class com.samsung.** { *; }
```

Licence
===========================
Copyright 2018 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
