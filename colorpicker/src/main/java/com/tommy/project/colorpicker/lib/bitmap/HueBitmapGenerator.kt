package com.tommy.project.colorpicker.lib.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.tommy.project.colorpicker.lib.view.PickerView


class HueBitmapGenerator(context: Context) :
    BitmapGenerator(context) {

    override fun createBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            .copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)

        val paint = getRectPaint(canvas)

        val array = arrayListOf<Int>()
        when (orientation) {
            PickerView.Orientation.VERTICAL -> {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        var hue = y * 360f / height
                        val hsv = floatArrayOf(hue, 1f, 1f)
                        array.add(Color.HSVToColor(hsv))
                    }
                }
            }
            else -> {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        var hue = x * 360f / width
                        val hsv = floatArrayOf(hue, 1f, 1f)
                        array.add(Color.HSVToColor(hsv))
                    }
                }
            }
        }

        val hueBitmap =
            Bitmap.createBitmap(array.toIntArray(), width, height, Bitmap.Config.ARGB_8888)

        canvas.drawBitmap(hueBitmap, 0f, 0f, paint)

        hueBitmap.recycle()

        return bitmap

    }
}