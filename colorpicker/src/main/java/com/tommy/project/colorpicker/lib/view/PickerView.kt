package com.tommy.project.colorpicker.lib.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tommy.project.colorpicker.lib.PickerChangeListener
import com.tommy.project.colorpicker.lib.R
import com.tommy.project.colorpicker.lib.bitmap.BitmapGenerator
import com.tommy.project.colorpicker.lib.getDpToPixel
import com.tommy.project.colorpicker.lib.picker.Picker


interface PickerReadyListener{
    fun onDestroy()
    fun onChange()
    fun onReady()
}

abstract class PickerView<T> : FrameLayout, TextureView.SurfaceTextureListener {

    enum class Orientation {
        VERTICAL, HORIZONTAL, SURFACE
    }

    /** xml attribute */
    protected var pickerOrientation = Orientation.VERTICAL
    protected var pickerStokeWidth: Int = 0
    protected var pickerStokeColor: Int = Color.TRANSPARENT
    protected var pickerStokeRadius: Float = 0f
    protected var pickerThumbSize: Int = 0

    protected lateinit var textureView: TextureView
    protected lateinit var picker: Picker<T>
    protected lateinit var thumb: View

    private lateinit var bitmapGenerator: BitmapGenerator

    var isReady = false
    private var pickerReadyListener : PickerReadyListener? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializationAttribute(context, attrs)
        initialization()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializationAttribute(context, attrs)
        initialization()
    }

    fun setOnPickerReadyListener(listener: PickerReadyListener?){
        this.pickerReadyListener = listener
    }

    open fun initializationAttribute(context: Context, attrs: AttributeSet?) {
        val obtainAttr = context.obtainStyledAttributes(attrs, R.styleable.PickerView)
        pickerOrientation =
            Orientation.values()[obtainAttr.getInt(R.styleable.PickerView_pickerOrientation, 0)]

        pickerStokeWidth = obtainAttr.getDimensionPixelSize(
            R.styleable.PickerView_pickerStokeWidth,
            getDpToPixel(0f, context).toInt()
        )
        pickerStokeColor =
            obtainAttr.getColor(R.styleable.PickerView_pickerStokeColor, Color.TRANSPARENT)

        pickerStokeRadius = obtainAttr.getDimension(
            R.styleable.PickerView_pickerStokeRadius,
            getDpToPixel(0f, context)
        )
        pickerThumbSize = obtainAttr.getDimensionPixelSize(
            R.styleable.PickerView_pickerThumbDrawableSize,
            getDpToPixel(0f, context).toInt()
        )

        setBackgroundColor(Color.TRANSPARENT)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialization() {

        thumb = createThumbView()
        bitmapGenerator = createBitmapGenerator()
        picker = createPicker(bitmapGenerator)

        textureView = TextureView(context)
        textureView.isOpaque = false
        textureView.surfaceTextureListener = this

        textureView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    placeThumb(event.x, event.y)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    placeThumb(event.x, event.y)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        addView(
            textureView, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = Gravity.CENTER
                val margin = (pickerThumbSize / 2).coerceAtLeast(pickerStokeWidth)
                when (pickerOrientation) {
                    Orientation.VERTICAL -> {
                        setMargins(margin / 2, margin, margin / 2, margin)
                    }
                    Orientation.HORIZONTAL -> {
                        setMargins(margin, margin / 2, margin, margin / 2)
                    }
                    Orientation.SURFACE -> {
                        setMargins(margin, margin, margin, margin)
                    }
                }
            }
        )

        /** 계산된 사이즈를 구한 후 추가 초기화 진행 */
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (viewTreeObserver.isAlive)
                    viewTreeObserver.removeOnPreDrawListener(this)

                val width = textureView.width + pickerStokeWidth * 2
                val height = textureView.height + pickerStokeWidth * 2

                /** set stroke option */
                val frameLayout = FrameLayout(context).apply {
                    layoutParams = LayoutParams(width, height).apply {
                        gravity = Gravity.CENTER
                    }

                    val shape = GradientDrawable()

                    if (pickerStokeRadius >= 0) {
                        shape.cornerRadius = pickerStokeRadius + pickerStokeWidth / 2
                    }

                    shape.setStroke(pickerStokeWidth, pickerStokeColor)
                    background = shape
                }

                frameLayout.setOnTouchListener(null)
                addView(frameLayout)

                /** set picker size */
                val thumbParams =
                    LayoutParams(pickerThumbSize, pickerThumbSize).apply {
                        gravity = when (pickerOrientation) {
                            Orientation.VERTICAL -> {
                                Gravity.CENTER_HORIZONTAL
                            }
                            Orientation.HORIZONTAL -> {
                                Gravity.CENTER_VERTICAL
                            }
                            Orientation.SURFACE -> {
                                Gravity.CENTER_HORIZONTAL
                            }
                        }
                    }

                addView(thumb, thumbParams)
                return true
            }
        })
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        Log.e("PickerView", "onSurfaceTextureSizeChanged")
        isReady = false

        bitmapGenerator.recycle()
        bitmapGenerator.createBitmap(width, height, pickerStokeRadius, pickerOrientation).let {
            val canvas = textureView.lockCanvas()
            canvas.drawBitmap(it, 0.0f, 0.0f, null)
            textureView.unlockCanvasAndPost(canvas)
        }

        pickerReadyListener?.onChange()
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        Log.e("PickerView", "onSurfaceTextureUpdated")

        if (!isReady){
            isReady = true
            pickerReadyListener?.onReady()
        }
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        Log.e("PickerView", "onSurfaceTextureDestroyed")
        bitmapGenerator.recycle()
        picker.clearChangeValueCallback()
        isReady = false
        pickerReadyListener?.onDestroy()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        Log.e("PickerView", "onSurfaceTextureAvailable")
        bitmapGenerator.recycle()
        bitmapGenerator.createBitmap(width, height, pickerStokeRadius, pickerOrientation)?.let {
            val canvas = textureView.lockCanvas()
            canvas.drawBitmap(it, 0.0f, 0.0f, null)
            textureView.unlockCanvasAndPost(canvas)
        }
    }

    protected abstract fun createThumbView(): View
    protected abstract fun createBitmapGenerator(): BitmapGenerator
    protected abstract fun createPicker(bitmapGenerator: BitmapGenerator): Picker<T>
    abstract fun placeThumb(x: Float, y: Float)
    abstract fun placeThumb(data: T)

    protected fun setBackgroundColorAndRetainShape(
        color: Int,
        background: Drawable
    ) {
        when (background) {
            is ShapeDrawable -> {
                (background.mutate() as ShapeDrawable).paint.color = color
            }
            is GradientDrawable -> {
                (background.mutate() as GradientDrawable).setColor(color)
            }
            is ColorDrawable -> {
                (background.mutate() as ColorDrawable).color = color
            }
            else -> {
            }
        }
    }

    fun addChangeValue(listener: PickerChangeListener<T>) {
        picker.addChangeValueCallback(listener)
    }

}