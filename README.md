# ColorPicker

## Dependency
```groovy

allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
....

dependencies {
  implementation "com.github.tommy0115:ColorPicker:1.0.1"
}
```
## How To
### declare xml 
```xml
       <com.tommy.project.colorpicker.lib.view.SatValPickerView
            android:id="@+id/satValPickerView"
            android:layout_width="wrap_content"
            android:layout_height="450dp"
            app:pickerThumbDrawableSize="35dp"
            app:pickerStokeColor="#946A6A6A"
            app:pickerStokeWidth="1dp"/>

        <com.tommy.project.colorpicker.lib.view.HuePickerView
            android:id="@+id/huePickerView"
            android:layout_width="300dp"
            android:layout_height="35dp"
            app:pickerThumbDrawableSize="35dp"
            app:pickerOrientation="HORIZONTAL"
            app:pickerStokeRadius="25dp"
            app:pickerStokeColor="#946A6A6A"
            app:pickerStokeWidth="1dp"/>

        <com.tommy.project.colorpicker.lib.view.TransparentPickerView
            android:layout_marginTop="10dp"
            android:id="@+id/transparentPickerView"
            android:layout_width="300dp"
            android:layout_height="35dp"
            app:pickerThumbDrawableSize="35dp"
            app:pickerOrientation="HORIZONTAL"
            app:pickerStokeRadius="25dp"
            app:pickerStokeColor="#946A6A6A"
            app:pickerStokeWidth="1dp"/>
```

### declare code
```kotlin
private lateinit var pickerViewManager : PickerViewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pickerViewManager = PickerViewManager(
            Color.BLUE, huePickerView, satValPickerView, transparentPickerView,
            object : PickerChangeListener<Int>{
                override fun onChangeValue(data: Int) {
                    runOnUiThread {
                        colorCheck.setBackgroundColor(data)
                    }
                }
            })
    }

```

