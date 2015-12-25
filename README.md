# AutoSizeTextView
An Android TextView that automatically fits its font and line count based on its available size and content

Based on solutions listed here http://stackoverflow.com/questions/5033012/auto-scale-textview-text-to-fit-within-bounds



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
