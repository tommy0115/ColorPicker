package com.tommy.project.colorpicker.lib.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
import com.tommy.project.colorpicker.lib.bitmap.HueBitmapGenerator
import com.tommy.project.colorpicker.lib.picker.HuePicker
import com.tommy.project.colorpicker.lib.picker.Picker

class HuePickerView : PickerView<Float> {

    private var colorImageView: ImageView? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun createThumbView(): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.thumb_layout, null) as FrameLayout
        colorImageView = view.findViewById(R.id.colorImageView)
        return view
    }

    override fun createBitmapGenerator(): BitmapGenerator {
        return HueBitmapGenerator(context)
    }

    override fun createPicker(bitmapGenerator: BitmapGenerator): Picker<Float> {
        val huePicker = HuePicker(bitmapGenerator, 0f)
        colorImageView?.let {
            (it.background as GradientDrawable).apply {
                setColor(Color.HSVToColor(255, floatArrayOf(0f, 1f, 1f)))
            }
        }

        huePicker.addChangeValueCallback(object : PickerChangeListener<Float> {
            override fun onChangeValue(data: Float) {
                huePicker.setValue(data)
                colorImageView?.let {
                    (it.background as GradientDrawable).apply {
                        setColor(Color.HSVToColor(255, floatArrayOf(data, 1f, 1f)))
                    }
                }
            }
        })

        return huePicker
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val heightMode = MeasureSpec.getMode(heightMeasureSpec);
        var convertHeightSpec = heightMeasureSpec
        if (pickerOrientation == Orientation.HORIZONTAL) {
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
        if (pickerOrientation == Orientation.VERTICAL) {
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
}