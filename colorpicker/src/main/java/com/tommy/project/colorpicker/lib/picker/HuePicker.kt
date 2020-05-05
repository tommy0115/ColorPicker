package com.tommy.project.colorpicker.lib.picker

import android.graphics.Point
import android.util.Log
import com.tommy.project.colorpicker.lib.bitmap.BitmapGenerator
import com.tommy.project.colorpicker.lib.view.PickerView
import kotlin.properties.Delegates

class HuePicker(bitmapGenerator: BitmapGenerator, initHue : Float) : Picker<Float>(bitmapGenerator) {

    private var hueData: Float by Delegates.observable(initHue, { _, _, newValue ->
        notifyValue(newValue)
    })

    override fun getValue(): Float {
        return hueData
    }

    override fun setValue(data: Float) {
        if (hueData != data) {
            hueData = data
        }
    }

    override fun calcPositionToData(x: Float, y: Float): Float {
        return bitmapGenerator.let {

            var hue = when (it.orientation) {
                PickerView.Orientation.VERTICAL -> {
                    y / (it.height / 360.0f)
                }

                PickerView.Orientation.HORIZONTAL -> {
                    x / (it.width / 360.0f)
                }

                else -> {
                    throw IllegalArgumentException()
                }
            }

            if (hue >= 360) {
                hue = 360f
            } else if (hue <= 0) {
                hue = 0f
            }

            Log.d("thumb", "hue : $hue / ${y}}")
            hue
        }
    }

    override fun calcDataToPosition(data: Float): Point {
        return bitmapGenerator.let {
            val point = Point(0, 0)
            when (it.orientation) {
                PickerView.Orientation.VERTICAL -> {
                    point.y = (it.height.toFloat() / 360f * data).toInt()

                    if (point.y > bitmapGenerator.height) {
                        point.y = bitmapGenerator.height
                    } else if (point.y < 0) {
                        point.y = 0
                    }
                }
                PickerView.Orientation.HORIZONTAL -> {
                    point.x = (it.width.toFloat() / 360f * data).toInt()

                    if (point.x > bitmapGenerator.width) {
                        point.x = bitmapGenerator.width
                    } else if (point.x < 0) {
                        point.x = 0
                    }
                }

                else -> {
                    throw IllegalArgumentException()
                }
            }

            return point
        }
    }
}