package com.house.project.colorpicker.lib.picker

import android.graphics.Point
import com.house.project.colorpicker.lib.bitmap.BitmapGenerator
import com.house.project.colorpicker.lib.data.SatVal
import kotlin.properties.Delegates

class SatValPicker(bitmapGenerator: BitmapGenerator) : Picker<SatVal>(bitmapGenerator) {

    private var satVal: SatVal by Delegates.observable(SatVal(-1f, -1f), { _, _, newValue ->
        notifyValue(newValue)
    })

    override fun getValue(): SatVal {
        return satVal
    }

    override fun setValue(data: SatVal) {
        if (satVal != data) {
            satVal = data
        }
    }

    override fun calcPositionToData(x: Float, y: Float): SatVal {
        return bitmapGenerator.let {
            var value = (it.height - y) / it.height.toFloat()
            var sat = x / it.width.toFloat()
            SatVal(sat, value)
        }
    }

    override fun calcDataToPosition(data: SatVal): Point {
        var point = Point(0, 0)
        bitmapGenerator?.let {
            point.x = (it.width.toFloat() * data.sat).toInt()
            point.y = it.height - (it.height.toFloat() * data.value).toInt()

            if (point.x > it.width){
                point.x = it.width
            } else if(point.x < 0){
                point.x = 0
            }

            if (point.y > it.height){
                point.y = it.height
            } else if(point.y < 0){
                point.y = 0
            }

        }

        return point
    }

    /*private var satVal : SatVal by Delegates.observable(
        SatVal(0f, 0f), { _, _, newValue ->
        notifyValue(newValue)
    })

    override fun getValue(x: Float, y: Float): SatVal {
        return bitmapGenerator.obtainBitmap().let {
            var value = (it.height - y) / it.height.toFloat()
            var sat = x / it.width.toFloat()

            val satVal =
                SatVal(sat, value)

            if (this.satVal != satVal) {
                this.satVal = satVal
            }

            satVal
        }
    }

    override fun setValue(data: SatVal): Point {
        var point = Point(0, 0)
        bitmapGenerator.obtainBitmap()?.let { bitmap ->

            point.x = (bitmap.width.toFloat() * data.sat).toInt()
            point.y = (bitmap.height.toFloat() * data.value).toInt()

            if (satVal != data) {
                satVal = data
            }
        }

        return point
    }*/
}