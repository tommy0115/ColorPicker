package com.tommy.project.colorpicker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.tommy.project.colorpicker.lib.PickerChangeListener
import com.tommy.project.colorpicker.lib.PickerViewManager
import kotlinx.android.synthetic.main.dialog_picker.*
import kotlinx.android.synthetic.main.dialog_picker.view.*

class PickerFragmentDialog : DialogFragment() {

    companion object {
        const val COLOR_PREFERENCE = "ColorPreference"
        const val COLOR_COUNT = 6
        const val COLOR_KEY = "colorKey"
    }

    private val userColorSelectorView = mutableListOf<View>()
    private val userColorImageView = mutableListOf<ImageView>()
    private var pickerViewManager: PickerViewManager? = null

    private var defaultUserColorList: List<Int> = mutableListOf(
        Color.BLACK,
        Color.GRAY,
        Color.WHITE,
        Color.RED,
        Color.GREEN,
        Color.BLUE
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window?.setBackgroundDrawable(
                    resources.getDrawable(
                        R.drawable.ic_round_background,
                        null
                    )
                )
            } else {
                window?.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_round_background))
            }
        }

        val view = inflater.inflate(R.layout.dialog_picker, container)
        setUserColorSelectorView(view)
        setUserColorImageView(view)
        updateUserColorBackground()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickerViewManager = PickerViewManager(
            Color.RED,
            view.huePickerView,
            view.satValPickerView,
            view.transparentPickerView,
            object : PickerChangeListener<Int> {
                override fun onChangeValue(data: Int) {
                    view.selectColorPreview.setBackgroundColor(data)
                }
            })

        userColorSelectorView.forEach {
            it.setOnClickListener { clickedView ->
                var selectedIndex = 0

                run {
                    userColorSelectorView.forEachIndexed { i, view ->
                        if (clickedView == view) {
                            selectedIndex = i
                            return@run
                        }
                    }
                }

                selectUserColor(selectedIndex)
            }
        }

        view.saveUserColorBtn.setOnClickListener {
            var index = -1
            run {
                userColorSelectorView.forEachIndexed { i, userView ->
                    if (userView.isSelected) {
                        index = i
                        return@run
                    }
                }
            }

            if (index < 0) {
                return@setOnClickListener
            }

            Log.d("index", index.toString())
            pickerViewManager?.let {
                saveUserColor(index, it.getColor())
                updateUserColorBackground()
                unSelectAllUserColor()
                Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectUserColor(index: Int) {
        userColorSelectorView.forEachIndexed { i, view ->
            if (index != i)
                view.isSelected = false
        }

        if (!userColorSelectorView[index].isSelected) {
            userColorSelectorView[index].isSelected = true
            saveIconView.visibility = View.VISIBLE
            pickerViewManager?.setColor(getUserColor()[index])
        } else {
            userColorSelectorView[index].isSelected = false
            saveIconView.visibility = View.INVISIBLE
        }
    }

    private fun unSelectAllUserColor() {
        userColorSelectorView.forEach {
            it.isSelected = false
        }

        saveIconView.visibility = View.INVISIBLE
    }

    private fun saveUserColor(index: Int, color: Int) {
        context?.let {
            val pref = it.getSharedPreferences(COLOR_PREFERENCE, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(COLOR_KEY + index, color)
            editor.commit()
        }
    }

    private fun saveUserColor(colors: IntArray) {
        context?.let {
            val pref = it.getSharedPreferences(COLOR_PREFERENCE, Context.MODE_PRIVATE)
            val editor = pref.edit()
            colors.forEachIndexed { i, color ->
                editor.putInt(COLOR_KEY + i, color)
            }
            editor.commit()
        }
    }

    private fun getUserColor(): List<Int> {
        return context?.let { context ->
            val pref = context.getSharedPreferences(COLOR_PREFERENCE, Context.MODE_PRIVATE)
            val userColorList = mutableListOf<Int>()
            for (i in 0 until COLOR_COUNT) {
                userColorList.add(pref.getInt(COLOR_KEY + i, defaultUserColorList[i]))
            }
            return userColorList
        } ?: defaultUserColorList
    }

    private fun setBackgroundColorAndRetainShape(
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

    private fun setUserColorSelectorView(view: View) {
        userColorSelectorView.clear()
        userColorSelectorView.add(view.userColorSelector1)
        userColorSelectorView.add(view.userColorSelector2)
        userColorSelectorView.add(view.userColorSelector3)
        userColorSelectorView.add(view.userColorSelector4)
        userColorSelectorView.add(view.userColorSelector5)
        userColorSelectorView.add(view.userColorSelector6)
    }

    private fun setUserColorImageView(view: View) {
        userColorImageView.clear()
        userColorImageView.add(view.userColor1)
        userColorImageView.add(view.userColor2)
        userColorImageView.add(view.userColor3)
        userColorImageView.add(view.userColor4)
        userColorImageView.add(view.userColor5)
        userColorImageView.add(view.userColor6)
    }

    private fun updateUserColorBackground() {
        val colors = getUserColor()
        userColorImageView.forEachIndexed { i, v ->
            setBackgroundColorAndRetainShape(colors[i], v.background)
        }
    }
}