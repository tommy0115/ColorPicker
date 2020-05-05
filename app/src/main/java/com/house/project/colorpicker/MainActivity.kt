package com.house.project.colorpicker

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.house.project.colorpicker.lib.PickerChangeListener
import com.house.project.colorpicker.lib.PickerViewManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
}