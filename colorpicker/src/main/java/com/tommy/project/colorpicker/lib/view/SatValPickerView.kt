package com.tommy.project.colorpicker.lib.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.tommy.project.colorpicker.lib.PickerChangeListener
import com.tommy.project.colorpicker.lib.R
import com.tommy.project.colorpicker.lib.bitmap.BitmapGenerator
import com.tommy.project.colorpicker.lib.bitmap.SatValBitmapGenerator
import com.tommy.project.colorpicker.lib.data.SatVal
import com.tommy.project.colorpicker.lib.picker.Picker
import com.tommy.project.colorpicker.lib.picker.SatValPicker

class SatValPickerView : PickerView<SatVal> {

    private var colorImageView: ImageView? = null
    private var satValBitmapGenerator: SatValBitmapGenerator? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun initializationAttribute(context: Context, attrs: AttributeSet?) {
        super.initializationAttribute(context, attrs)
        pickerOrientation = Orientation.SURFACE
    }

    override fun createThumbView(): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.thumb_layout, null) as FrameLayout
        colorImageView = view.findViewById(R.id.colorImageView)
        return view
    }

    override fun createBitmapGenerator(): BitmapGenerator {
        return SatValBitmapGenerator(context, 0f).also {
            satValBitmapGenerator = it
        }
    }

    override fun createPicker(bitmapGenerator: BitmapGenerator): Picker<SatVal> {
        val satValPicker = SatValPicker(bitmapGenerator)
        satValPicker.addChangeValueCallback(object : PickerChangeListener<SatVal> {
            override fun onChangeValue(data: SatVal) {
                colorImageView?.let { imageView ->
                    satValBitmapGenerator?.let {
                        setBackgroundColorAndRetainShape(
                            Color.HSVToColor(
                                255,
                                floatArrayOf(it.hue, data.sat, data.value)
                            ), imageView.background
                        )
                    }
                }
            }
        })

        return satValPicker
    }

    override fun placeThumb(x: Float, y: Float) {
        val data = picker.calcPositionToData(x, y)
        placeThumb(data)
    }

    override fun placeThumb(data: SatVal) {
        val point = picker.calcDataToPosition(data)
        thumb.x = point.x + (textureView.marginLeft) - (thumb.width / 2).toFloat()
        thumb.y = point.y + (textureView.marginTop) - (thumb.height / 2).toFloat()

        picker.setValue(data)
    }

    fun changeHue(hue: Float) {
        satValBitmapGenerator?.recycle()
        satValBitmapGenerator?.hue = hue
        satValBitmapGenerator?.obtainBitmap()?.let {
            val canvas = textureView.lockCanvas()
            canvas.drawBitmap(it, 0.0f, 0.0f, null)
            textureView.unlockCanvasAndPost(canvas)
        }

        changeHueThumb()
    }

    private fun changeHueThumb() {
        colorImageView?.let { imageView ->
            satValBitmapGenerator?.let {
                setBackgroundColorAndRetainShape(
                    Color.HSVToColor(
                        255,
                        floatArrayOf(it.hue, picker.getValue().sat, picker.getValue().value)
                    ), imageView.background
                )
            }
        }
    }
}