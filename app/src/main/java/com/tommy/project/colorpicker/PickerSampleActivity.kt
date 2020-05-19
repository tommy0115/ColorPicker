package com.tommy.project.colorpicker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tommy.project.colorpicker.lib.PickerChangeListener
import com.tommy.project.colorpicker.lib.PickerViewManager
import kotlinx.android.synthetic.main.activity_sample_picker.*

class PickerSampleActivity : AppCompatActivity() {

    private lateinit var pickerViewManager: PickerViewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_picker)

        pickerViewManager = PickerViewManager(
            Color.BLUE, huePickerView, satValPickerView, transparentPickerView,
            object : PickerChangeListener<Int> {
                override fun onChangeValue(data: Int) {
                    runOnUiThread {
                        colorCheck.setBackgroundColor(data)
                    }
                }
            })
    }



}