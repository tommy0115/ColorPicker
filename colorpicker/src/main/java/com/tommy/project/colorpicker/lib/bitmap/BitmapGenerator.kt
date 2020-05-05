package com.tommy.project.colorpicker.lib.bitmap

import android.content.Context
import android.graphics.*
import com.tommy.project.colorpicker.lib.view.PickerView
import java.lang.Exception

abstract class BitmapGenerator {
    private var bitmap: Bitmap? = null
    protected val context: Context

    var width: Int = 0
    var height: Int = 0
    var radius: Float = 0f
    var orientation: PickerView.Orientation = PickerView.Orientation.VERTICAL

    constructor(context: Context) {
        this.context = context
    }

    abstract fun createBitmap(): Bitmap

    fun createBitmap(
        width: Int,
        height: Int,
        radius: Float = 0f,
        orientation: PickerView.Orientation
    ): Bitmap {
        this.width = width
        this.height = height
        this.radius = radius
        this.orientation = orientation

        return createBitmap()
    }

    fun obtainBitmap(): Bitmap {
        if (bitmap == null || bitmap!!.isRecycled) {
            bitmap = createBitmap(width, height, radius, orientation)
        }

        if (bitmap == null) {
            throw Exception("Bitmap is null")
        }

        return bitmap!!
    }

    fun recycle() {
        bitmap?.let {
            if (!it.isRecycled) {
                it.recycle()
            }
        }

        bitmap = null
    }

    protected fun getRectPaint(canvas: Canvas, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) : Paint{
        return if (radius <= 0){
            getRectStrokePaint(canvas, mode)
        } else {
            getRoundRectStrokePaint(canvas, mode)
        }
    }

    private fun getRoundRectStrokePaint(canvas: Canvas, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN): Paint {
        val paint = Paint()
        paint.apply {
            isAntiAlias = true
            color = Color.BLUE
        }

        canvas.drawRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            radius,
            radius,
            paint
        )

        paint.xfermode = PorterDuffXfermode(mode)
        return paint
    }

    private fun getRectStrokePaint(canvas: Canvas, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN): Paint {
        val paint = Paint()
        paint.apply {
            isAntiAlias = true
            color = Color.BLUE
        }

        canvas.drawRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()), paint
        )

        paint.xfermode = PorterDuffXfermode(mode)
        return paint
    }

    /*protected fun getRoundedRectPath(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        rx: Float,
        ry: Float,
        conformToOriginalPost: Boolean
    ): Path {
        var rx = rx
        var ry = ry
        val path = Path()
        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry

        path.moveTo(right, top + ry)
        path.arcTo(right - 2 * rx, top, right, top + 2 * ry, 0f, -90f, false) //top-right-corner
        path.rLineTo(-widthMinusCorners, 0f)
        path.arcTo(left, top, left + 2 * rx, top + 2 * ry, 270f, -90f, false)//top-left corner.
        path.rLineTo(0f, heightMinusCorners)
        if (conformToOriginalPost) {
            path.rLineTo(0f, ry)
            path.rLineTo(width, 0f)
            path.rLineTo(0f, -ry)
        } else {
            path.arcTo(
                left,
                bottom - 2 * ry,
                left + 2 * rx,
                bottom,
                180f,
                -90f,
                false
            ) //bottom-left corner
            path.rLineTo(widthMinusCorners, 0f)
            path.arcTo(
                right - 2 * rx,
                bottom - 2 * ry,
                right,
                bottom,
                90f,
                -90f,
                false
            ) //bottom-right corner
        }

        path.rLineTo(0f, -heightMinusCorners)

        path.close()//Given close, last lineto can be removed.
        return path
    }*/
}