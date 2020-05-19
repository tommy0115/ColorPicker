package com.tommy.project.colorpicker.lib

import android.graphics.Color
import com.tommy.project.colorpicker.lib.data.SatVal
import com.tommy.project.colorpicker.lib.view.*

class PickerViewManager {

    private val pickerListener: PickerChangeListener<Int>
    private var hue: Float = 0f
    private var satVal: SatVal = SatVal(1f, 1f)
    private var transparent: Float = 255f

    private var pickerList = mutableListOf<PickerView<*>>()

    constructor(
        defaultColor: Int,
        hueView: HuePickerView,
        satValView: SatValPickerView,
        transparentView: TransparentPickerView,
        changeListener: PickerChangeListener<Int>
    ) {
        this.pickerListener = changeListener
        pickerList.add(hueView as PickerView<*>)
        pickerList.add(satValView as PickerView<*>)
        pickerList.add(transparentView as PickerView<*>)
        parsingColor(defaultColor)

        pickerList.forEach { pickerView ->
            pickerView.setOnPickerReadyListener(object : PickerReadyListener {
                override fun onDestroy() {

                }

                override fun onChange() {

                }

                override fun onReady() {
                    when (pickerView) {
                        is HuePickerView -> {
                            pickerView.placeThumb(hue)
                            pickerView.setHueChangeListener(satValView, transparentView)
                        }

                        is SatValPickerView -> {
                            pickerView.changeHue(hue)
                            pickerView.placeThumb(satVal)
                            pickerView.setSatValChangeListener(transparentView)
                        }

                        is TransparentPickerView -> {
                            pickerView.changeColor(getColor())
                            pickerView.placeThumb(transparent)
                            pickerView.setTransparentChangeListener()
                        }
                    }
                }
            })
        }
    }

    fun getColor(): Int {
        return Color.HSVToColor(transparent.toInt(), floatArrayOf(hue, satVal.sat, satVal.value))
    }

    fun setColor(color: Int) {
        parsingColor(color)
        changePlaceView()
    }

    private fun changePlaceView() {
        pickerList.forEach { pickerView ->

            pickerView.takeIf { it.isReady }?:return

            when (pickerView) {
                is HuePickerView -> {
                    pickerView.placeThumb(hue)
                }

                is SatValPickerView -> {
                    pickerView.changeHue(hue)
                    pickerView.placeThumb(satVal)
                }

                is TransparentPickerView -> {
                    pickerView.changeColor(getColor())
                    pickerView.placeThumb(transparent)
                }
            }
        }
    }

    private fun HuePickerView.setHueChangeListener(
        satValPickerView: SatValPickerView,
        transparentPickerView: TransparentPickerView
    ) {
        addChangeValue(object : PickerChangeListener<Float> {
            override fun onChangeValue(data: Float) {
                hue = data

                satValPickerView.takeIf { it.isReady }?.let {
                    it.changeHue(data)
                } ?: return

                transparentPickerView.takeIf { it.isReady }?.let {
                    it.changeColor(getColor())
                } ?: return

                pickerListener.onChangeValue(getColor())
            }
        })
    }

    private fun SatValPickerView.setSatValChangeListener(transparentPickerView: TransparentPickerView) {
        addChangeValue(object : PickerChangeListener<SatVal> {
            override fun onChangeValue(data: SatVal) {
                satVal = data

                transparentPickerView.takeIf { it.isReady }?.let {
                    it.changeColor(getColor())
                } ?: return

                pickerListener.onChangeValue(getColor())
            }
        })
    }

    private fun TransparentPickerView.setTransparentChangeListener() {
        addChangeValue(object :
            PickerChangeListener<Float> {
            override fun onChangeValue(data: Float) {
                transparent = data
                takeIf { isReady } ?: return
                pickerListener.onChangeValue(getColor())
            }
        })
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