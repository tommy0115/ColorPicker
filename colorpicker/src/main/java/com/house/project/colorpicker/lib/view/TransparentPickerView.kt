package com.house.project.colorpicker.lib.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.house.project.colorpicker.lib.PickerChangeListener
import com.house.project.colorpicker.lib.R
import com.house.project.colorpicker.lib.bitmap.BitmapGenerator
import com.house.project.colorpicker.lib.bitmap.TransparentBitmapGenerator
import com.house.project.colorpicker.lib.picker.Picker
import com.house.project.colorpicker.lib.picker.TransparentPicker


class TransparentPickerView : PickerView<Float> {

    private var colorImageView: ImageView? = null
    private var transparentBitmapGenerator: TransparentBitmapGenerator? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val heightMode = MeasureSpec.getMode(heightMeasureSpec);
        var convertHeightSpec = heightMeasureSpec
        if (pickerOrientation == PickerView.Orientation.HORIZONTAL) {
            when (heightMode) {
                /*MeasureSpec.UNSPECIFIED -> {    // mode
                    heightSize = heightMeasureSpec;
                }*/
                MeasureSpec.AT_MOST -> { // wrap_content
                    val marginSize = pickerThumbSize * 1.5f
                    var heightSize = (marginSize.toInt());
                    convertHeightSpec =
                        MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST)
                }
                /*MeasureSpec.EXACTLY -> { // fill_parent, match_parent
                    heightSize = MeasureSpec.getSize(heightMeasureSpec);
                }*/
            }
        }

        val widthMode = MeasureSpec.getMode(widthMeasureSpec);
        var convertWidthSpec = widthMeasureSpec
        if (pickerOrientation == PickerView.Orientation.VERTICAL) {
            when (widthMode) {
                /*MeasureSpec.UNSPECIFIED -> {    // mode
                    heightSize = heightMeasureSpec;
                }*/
                MeasureSpec.AT_MOST -> { // wrap_content
                    val marginSize = pickerThumbSize * 1.5f
                    var widthSize = (marginSize.toInt());
                    convertWidthSpec =
                        MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST)
                }
                /*MeasureSpec.EXACTLY -> { // fill_parent, match_parent
                    heightSize = MeasureSpec.getSize(heightMeasureSpec);
                }*/
            }

        }

        super.onMeasure(convertWidthSpec, convertHeightSpec)
    }

    override fun createThumbView(): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.thumb_layout, null) as FrameLayout
        colorImageView = view.findViewById(R.id.colorImageView)
        return view
    }

    override fun createBitmapGenerator(): BitmapGenerator {
        return TransparentBitmapGenerator(context, 0).also {
            transparentBitmapGenerator = it
        }
    }

    override fun createPicker(bitmapGenerator: BitmapGenerator): Picker<Float> {
        val transparentPicker = TransparentPicker(bitmapGenerator)
        transparentPicker.addChangeValueCallback(object : PickerChangeListener<Float> {
            override fun onChangeValue(data: Float) {

            }
        })

        return transparentPicker
    }

    override fun placeThumb(x: Float, y: Float) {
        val data = picker.calcPositionToData(x, y)
        placeThumb(data)
    }

    override fun placeThumb(data: Float) {
        val point = picker.calcDataToPosition(data)
        when (pickerOrientation) {
            Orientation.VERTICAL -> {
                thumb.y = point.y + (textureView.marginTop) - (thumb.height / 2).toFloat()
            }
            else -> {
                thumb.x = point.x + (textureView.marginLeft) - (thumb.width / 2).toFloat()
            }
        }

        picker.setValue(data)
    }

    fun changeColor(color: Int) {
        transparentBitmapGenerator?.recycle()
        transparentBitmapGenerator?.backgroundColor = color
        transparentBitmapGenerator?.obtainBitmap()?.let {
            val canvas = textureView.lockCanvas()
            canvas.drawBitmap(it, 0.0f, 0.0f, null)
            textureView.unlockCanvasAndPost(canvas)
        }

        changeColorThumb()
    }

    private fun changeColorThumb() {
        transparentBitmapGenerator?.let {
            val backgroundColor = it.backgroundColor
            val color = Color.argb(
                255,
                Color.red(backgroundColor),
                Color.green(backgroundColor),
                Color.blue(backgroundColor)
            )
            colorImageView?.let { imageView ->
                setBackgroundColorAndRetainShape(color, imageView.background)
            }

        }
    }
}