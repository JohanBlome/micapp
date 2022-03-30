# micapp

micapp is a tool to characterizate microphones in Android.


# 1. Prerequisites

For running encapp:
* adb connection to the device being tested.


# 2. Operation: Install App

Install the app:
```
$ ./gradlew installDebug
...
BUILD SUCCESSFUL in 2s
27 actionable tasks: 1 executed, 26 up-to-date
```

Check the app has been installed:
```
$ adb shell pm list packages |grep micapp
package:com.facebook.micapp
```

Uninstall the app:
```
$ adb shell cmd package uninstall com.facebook.micapp
Success
$ adb shell pm list packages |grep micapp
$
```

Build the app:
```
$ ./gradlew build
...
BUILD SUCCESSFUL in 2s
61 actionable tasks: 1 executed, 60 up-to-date
```


# 3. Operation: Get List of Available Mics and Properties

After installing the app, run:

```
./scripts/micapp.py info
micapp

audio_device_info_array {
  size: 7
  audio_device_info {
    address: "bottom"
    channel_count: 2
    channel_mask: 12
    encoding: 2
    encoding_str: "pcm_16bit"
    encoding: 4
    encoding_str: "pcm_float"
    id: 8
    product_name: "..."
    sample_rate: 48000
    type: 15
    type_str: "builtin mic"
    hash_code: 39
    is_sink: false
    is_source: true
  }
  ...
}
microphone_info_array {
  size: 8
  microphone_info {
    address: "bottom"
    channel_mappings {
    }
  ...
}
audio_effect_descriptors {
  descriptor {
    connectMode: "Insert"
    implementor: "The Android Open Source Project"
    name: "Dynamics Processing"
  ...
}
audio_effects {
  aec_available: false
  agc_available: false
  ns_available: false
  ...
}

__________________

Data also available in micapp_info_device_name.txt
```

Note that the output contains 4 different Sections.
