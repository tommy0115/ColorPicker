package com.house.project.colorpicker.lib

interface PickerChangeListener<T> {
    fun onChangeValue(data: T)
}