package com.house.project.colorpicker.lib.picker

import android.graphics.Point
import com.house.project.colorpicker.lib.PickerChangeListener
import com.house.project.colorpicker.lib.bitmap.BitmapGenerator

abstract class Picker<T>(var bitmapGenerator: BitmapGenerator) {

    private val changeValueCallback = mutableListOf<PickerChangeListener<T>>()

    abstract fun getValue(): T
    abstract fun setValue(data : T)
    abstract fun calcPositionToData(x: Float, y: Float) : T
    abstract fun calcDataToPosition(data: T) : Point

    fun addChangeValueCallback(listener: PickerChangeListener<T>) {
        changeValueCallback.add(listener)
    }

    fun changeBitmapGenerator(bitmapGenerator: BitmapGenerator){
        this.bitmapGenerator = bitmapGenerator
    }

    protected fun notifyValue(value : T){
        changeValueCallback.forEach {
            it.onChangeValue(value)
        }
    }

    fun clearChangeValueCallback(){
        changeValueCallback.clear()
    }
}