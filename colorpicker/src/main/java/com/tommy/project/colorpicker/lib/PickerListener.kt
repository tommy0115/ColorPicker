package com.tommy.project.colorpicker.lib

interface PickerChangeListener<T> {
    fun onChangeValue(data: T)
}