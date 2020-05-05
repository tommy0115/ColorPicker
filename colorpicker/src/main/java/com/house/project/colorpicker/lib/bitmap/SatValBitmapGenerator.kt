package com.house.project.colorpicker.lib.bitmap

import android.content.Context
import android.graphics.*

class SatValBitmapGenerator(context: Context, var hue: Float = 0f) :
    BitmapGenerator(context) {

    override fun createBitmap(): Bitmap {
        val linearGradient  =  LinearGradient(0f, 0f, 0f, height.toFloat(), Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
        val sat = LinearGradient(0f, 0f, width.toFloat(), 0f, Color.WHITE, Color.HSVToColor(floatArrayOf(hue, 1f, 1f)), Shader.TileMode.CLAMP);
        val merged = ComposeShader(linearGradient, sat, PorterDuff.Mode.MULTIPLY)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), getRectPaint(canvas).apply {
            this.shader = merged
        })

        return bitmap
    }

    /** 첫번째 버전 */
    /*fun createBitmapFirstVersion() : Bitmap{
        val skipCount = 10
        val colors = IntArray(width * height)
        var pix = 0
        var y = 0
        while (y < height) {
            run {
                var x = 0
                while (x < width) {

                    if (pix >= width * height)
                        break

                    val sat = x / width.toFloat()
                    val `val` = (height - y) / height.toFloat()

                    val hsv = floatArrayOf(hue, sat, `val`)

                    val color = Color.HSVToColor(hsv)
                    for (m in 0 until skipCount) {
                        if (pix >= width * height)
                            break

                        if (x + m < width) {
                            colors[pix] = color
                            pix++
                        }
                    }
                    x += skipCount
                }
            }

            for (n in 0 until skipCount) {
                if (pix >= width * height)
                    break

                for (x in 0 until width) {
                    colors[pix] = colors[pix - width]
                    pix++
                }
            }
            y += skipCount
        }

        val bitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)

        val satValBitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.RGB_565).copy(Bitmap.Config.ARGB_8888, true)
        canvas.drawBitmap(satValBitmap, 0f, 0f, getRectPaint(canvas))

        satValBitmap.recycle()

        return bitmap
    }*/


}