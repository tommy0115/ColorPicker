package com.tommy.project.colorpicker.lib

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import com.tommy.project.colorpicker.lib.data.SatVal
import com.tommy.project.colorpicker.lib.view.HuePickerView
import com.tommy.project.colorpicker.lib.view.SatValPickerView
import com.tommy.project.colorpicker.lib.view.TransparentPickerView

class PickerViewManager {

    private val huePickerView: HuePickerView
    private val satValPickerView: SatValPickerView
    private val transparentPickerView: TransparentPickerView
    private val pickerListener: PickerChangeListener<Int>

    private var hue: Float = 0f
    private var satVal: SatVal = SatVal(1f, 1f)
    private var transparent: Float = 255f

    constructor(
        defaultColor: Int,
        huePickerView: HuePickerView,
        satValPickerView: SatValPickerView,
        transparentPickerView: TransparentPickerView,
        pickerListener: PickerChangeListener<Int>
    ) {
        this.huePickerView = huePickerView
        this.satValPickerView = satValPickerView
        this.transparentPickerView = transparentPickerView
        this.pickerListener = pickerListener

        parsingColor(defaultColor)

        huePickerView.viewTreeObserver.addOnGlobalLayoutListener {
            Handler(Looper.getMainLooper()).post {
                huePickerView.placeThumb(hue)
            }
        }

        satValPickerView.viewTreeObserver.addOnGlobalLayoutListener {
            Handler(Looper.getMainLooper()).post {
                satValPickerView.changeHue(hue)
                satValPickerView.placeThumb(satVal)
            }
        }

        transparentPickerView.viewTreeObserver.addOnGlobalLayoutListener {
            Handler(Looper.getMainLooper()).post{
                transparentPickerView.changeColor(getColor())
                transparentPickerView.placeThumb(transparent)
            }
        }

        setDependency()
    }

    private fun setDependency() {
        huePickerView.addChangeValue(object : PickerChangeListener<Float> {
            override fun onChangeValue(data: Float) {
                hue = data
                satValPickerView.changeHue(data)
                transparentPickerView.changeColor(getColor())
                pickerListener.onChangeValue(getColor())
            }
        })

        satValPickerView.addChangeValue(object : PickerChangeListener<SatVal> {
            override fun onChangeValue(data: SatVal) {
                satVal = data
                transparentPickerView.changeColor(getColor())
                pickerListener.onChangeValue(getColor())
            }
        })

        transparentPickerView.addChangeValue(object : PickerChangeListener<Float> {
            override fun onChangeValue(data: Float) {
                transparent = data
                pickerListener.onChangeValue(getColor())
            }
        })
    }

    fun getColor(): Int {
        return Color.HSVToColor(transparent.toInt(), floatArrayOf(hue, satVal.sat, satVal.value))
    }

    fun setColor(color: Int) {
        parsingColor(color)
        changeView()
    }

    private fun changeView() {
        Handler(Looper.getMainLooper()).post {
            huePickerView.placeThumb(hue)

            satValPickerView.changeHue(hue)
            satValPickerView.placeThumb(satVal)

            transparentPickerView.changeColor(getColor())
            transparentPickerView.placeThumb(transparent)
        }
    }

    private fun parsingColor(defaultColor: Int) {

        var hsv = FloatArray(3)
        Color.RGBToHSV(
            Color.red(defaultColor),
            Color.green(defaultColor),
            Color.blue(defaultColor),
            hsv
        )

        hue = hsv[0]
        satVal = SatVal(hsv[1], hsv[2])
        transparent = Color.alpha(defaultColor).toFloat()
    }

}