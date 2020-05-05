package com.house.project.colorpicker.lib.bitmap

import android.content.Context
import android.graphics.*
import com.house.project.colorpicker.lib.getDpToPixel

class TransparentBitmapGenerator(context: Context, var backgroundColor : Int = 0) : BitmapGenerator(context){

    private val grayPaint = Paint().apply {
        this.style = Paint.Style.FILL
        this.color = Color.LTGRAY
        this.isAntiAlias = true
    }

    private val whitePaint = Paint().apply {
        this.style = Paint.Style.FILL
        this.color = Color.WHITE
        this.isAntiAlias = true
    }

    override fun createBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)

        val background = createTransparentBackground()

        var left = 0f
        var top = 0f

        val bgTileWidth: Int = background.width
        val bgTileHeight: Int = background.height

        while (left < width) {
            while (top < height) {
                canvas.drawBitmap(background, left, top, null)
                top += bgTileHeight
            }
            left += bgTileWidth
            top = 0f
        }

        background.recycle()

        canvas.drawRect( 0f, 0f, width.toFloat(), height.toFloat(), Paint().apply {
            this.shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(),
                Color.argb(0, 0, 0, 0),
                Color.argb(255, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor)), Shader.TileMode.CLAMP)

            this.isDither = true
            this.isAntiAlias = true
        })

        val strokeBase = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true)
        val strokeBaseCanvas = Canvas(strokeBase)

        strokeBaseCanvas.drawBitmap(bitmap, 0f, 0f, getRectPaint(strokeBaseCanvas))
        bitmap.recycle()

        return strokeBase
    }

    fun createTransparentBackground() : Bitmap{

        val size = getDpToPixel(12f, context)
        val halfSize = size / 2
        val transparentBitmap = Bitmap.createBitmap(size.toInt(), size.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(transparentBitmap)

        canvas.drawRect(0f, 0f, halfSize, halfSize, grayPaint)
        canvas.drawRect(halfSize, halfSize, size , size , grayPaint)

        canvas.drawRect( halfSize, 0f, size , halfSize , whitePaint)
        canvas.drawRect(0f, halfSize, halfSize , size , whitePaint)

        return transparentBitmap

    }
}