package com.tommy.project.colorpicker.lib.picker

import android.graphics.Point
import com.tommy.project.colorpicker.lib.bitmap.BitmapGenerator
import com.tommy.project.colorpicker.lib.view.PickerView
import kotlin.properties.Delegates

class TransparentPicker(bitmapGenerator: BitmapGenerator) : Picker<Float>(bitmapGenerator) {

    private var alphaData: Float by Delegates.observable(-1f, { _, _, newValue ->
        notifyValue(newValue)
    })

    override fun getValue(): Float {
        return alphaData
    }

    override fun setValue(data: Float) {
        if (alphaData != data) {
            alphaData = data
        }
    }

    override fun calcPositionToData(x: Float, y: Float): Float {
        return bitmapGenerator.let {

            var alpha = when (it.orientation) {
                PickerView.Orientation.VERTICAL -> {
                    y / (it.height / 255.0f)
                }

                PickerView.Orientation.HORIZONTAL -> {
                    x / (it.width / 255.0f)
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }

            if (alpha > 255) {
                alpha = 255f
            } else if (alpha <= 0) {
                alpha = 0f
            }


            alpha
        }
    }

    override fun calcDataToPosition(data: Float): Point {
        return bitmapGenerator.let {
            val point = Point(0, 0)

            when (it.orientation) {
                PickerView.Orientation.VERTICAL -> {
                    point.y = (it.height.toFloat() / 255 * data).toInt()

                    if (point.y > bitmapGenerator.height) {
                        point.y = bitmapGenerator.height
                    } else if (point.y < 0) {
                        point.y = 0
                    }
                }
                PickerView.Orientation.HORIZONTAL -> {
                    point.x = (it.width.toFloat() / 255 * data).toInt()

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

    /*private var transparent : Float by Delegates.observable(
        0f, { _, _, newValue ->
        notifyValue(newValue)
    })

    override fun getValue(x: Float, y: Float): Float {
        return bitmapGenerator.obtainBitmap().let {
            var value = x / (it.width / 255.0f)

            if (this.transparent != value) {
                this.transparent = value
            }

            transparent
        }
    }

    override fun setValue(data: Float): Point {
        var point = Point(0, 0)
        bitmapGenerator.obtainBitmap()?.let { bitmap ->

            point.x = (bitmap.width.toFloat() * data).toInt()
            //point.y = (bitmap.height.toFloat() * data.value).toInt()

            if (transparent != data) {
                transparent = data
            }
        }

        return point
    }*/
}