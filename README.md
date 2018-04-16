# Android-KnoxActivator
Samsung's Knox Standard activation helper library for Android

Download
===============================

Maven
```
<dependency>
  <groupId>com.akexorcist</groupId>
  <artifactId>knoxactivator</artifactId>
  <version>1.0.1</version>
</dependency>
```

Gradle
```groovy
implementation 'com.akexorcist:knoxactivator:1.0.1'
```

Public Method
===============================
```java
// Setup
void register(ActivationCallback callback)
void unregister()
void onActivityResult(int requestCode, final int resultCode, Intent data)

// Activate & Deactivate
void activateLicense(Context context, String licenseKey)
void activateDeviceAdmin(Activity activity)
void deactivateDeviceAdmin(Context context)

// Status Checking
boolean isDeviceAdminActivated(Context context)
boolean isKnoxSdkSupported(Context context)
boolean isMdmApiSupported(Context context, EnterpriseDeviceManager.EnterpriseSdkVersion requiredVersion)
```

Usage
===============================

### Setup
Required methods in Activity class for Knox Standard activation
```java
public class YourActivity extends AppCompatActivity {

    //...

    @Override
    public void onStart() {
        super.onStart();
        KnoxActivationManager.getInstance().register(activationCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        KnoxActivationManager.getInstance().unregister();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KnoxActivationManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
```

### Activate & Deactivate method
```java
// Device Administrator
KnoxActivationManager.getInstance().activateDeviceAdmin(getActivity());
KnoxActivationManager.getInstance().deactivateDeviceAdmin(getContext());

// Knox Standard SDK
String licenseKey = "YOUR_ELM_KEY";
KnoxActivationManager.getInstance().activateLicense(getContext(), licenseKey);
```
How to get the ELM key?
https://seap.samsung.com/license-keys/create/knox-android

### Status checking method
Utility methods 
```java
KnoxActivationManager manager = KnoxActivationManager.getInstance();

// Is Device Administrator activated?
boolean isDeviceAdminActivated = manager.isDeviceAdminActivated(getContext());

// Is Knox SDK Supported?
boolean isKnoxSdkSupported = manager.isKnoxSdkSupported(getContext());

// Is current MDM API version supported?
EnterpriseDeviceManager.EnterpriseSdkVersion sdkVersion = EnterpriseDeviceManager.EnterpriseSdkVersion.ENTERPRISE_SDK_VERSION_5_7_1;
boolean isMdmApiSupported = manager.isMdmApiSupported(getContext(), sdkVersion);
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
        public void onDeviceAdminDeactivated() {
            // ...
        }

        @Override
        public void onLicenseActivated() {
            // ...
        }

        @Override
        public void onLicenseActivateFailed(int errorType, String errorMessage) {
            // ...
        }
    };
```

Licence
===========================
Copyright 2017 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
