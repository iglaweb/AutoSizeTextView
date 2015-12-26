# AutoSizeTextView

[![JitPack][jitpack-svg]][jitpack-link]
[![Build Status][build-status-svg]][build-status-link]
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

An Android TextView that automatically fits its font and line count based on its available size and content

Based on solutions listed here http://stackoverflow.com/questions/5033012/auto-scale-textview-text-to-fit-within-bounds


## Overview

- Android 4.0.3+ support
- Widget allows to set minimum text size
- Text size is also adjusted automatically after rotation
- Ellipsis text in case of text length exceeds predefined value


# Gradle Dependency


#### Repository

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

#### Dependency

Add this to your module's `build.gradle` file:

```gradle
dependencies {
	compile 'com.github.iglaweb:AutoSizeTextView:v1.0'
	}
}
```







Use the built in Widget in code or XML:

```xml
    <ru.igla.widget.AutoSizeTextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="Long ancestry"
        android:textColor="@android:color/black"
        android:background="@android:color/white"
        android:textSize="500sp"
        android:maxLines="500"
        android:gravity="center"
        android:ellipsize="@null"
        android:autoText="false"
        android:singleLine="false"
        android:includeFontPadding="false"
        android:textAlignment="center"
        android:typeface="normal"
        android:layout_gravity="center"
        android:textStyle="normal"
        />
```

Issues
------

If you find any problems or would like to suggest a feature, please
feel free to file an issue on github at
https://github.com/iglaweb/AutoSizeTextView/issues

## License

    Copyright 2015 Igor Lashkov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [build-status-svg]: https://travis-ci.org/iglaweb/AutoSizeTextView.svg?branch=dev
 [build-status-link]: https://travis-ci.org/iglaweb/AutoSizeTextView
 [license-svg]: https://img.shields.io/badge/license-APACHE-lightgrey.svg
 [license-link]: https://github.com/iglaweb/AutoSizeTextView/blob/dev/LICENSE
 [jitpack-svg]: https://img.shields.io/github/release/iglaweb/autosizetextview.svg?label=jitpack
 [jitpack-link]: https://jitpack.io/#iglaweb/autosizetextview