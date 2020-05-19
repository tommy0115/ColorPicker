package com.tommy.project.colorpicker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activitySampleBtn.setOnClickListener {
            startActivity(Intent(this, PickerSampleActivity::class.java))
        }

        dialogSampleBtn.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager
            val dialogFragment = PickerFragmentDialog()
            dialogFragment.show(fm, "pickerFragmentDialog")
        }
    }

   /* private fun showPickerDialog() {
        // 상태를 저장하지 못하기 때문에 패스
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_picker, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.window?.setBackgroundDrawable(
                resources.getDrawable(
                    R.drawable.ic_round_background,
                    null
                )
            )
        } else {
            dialog.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_round_background))
        }

        val picker = PickerViewManager(
            Color.RED,
            view.huePickerView,
            view.satValPickerView,
            view.transparentPickerView,
            object : PickerChangeListener<Int> {
                override fun onChangeValue(data: Int) {

                }
            })

        val userColorImageView = mutableListOf<ImageView>()
        userColorImageView.add(view.userColor1)
        userColorImageView.add(view.userColor2)
        userColorImageView.add(view.userColor3)
        userColorImageView.add(view.userColor4)
        userColorImageView.add(view.userColor5)
        userColorImageView.add(view.userColor6)

        userColorImageView.forEachIndexed { i, v ->
            setBackgroundColorAndRetainShape(getUserColor()[i], v.background)
        }

        userColorImageView.forEach {
            it.setOnClickListener {
                userColorImageView.forEach { view ->
                    view.isSelected = false
                }

                it.isSelected = true
            }
        }

        view.saveUserColorBtn.setOnClickListener {
            var selectedView : View? = null
            var index = -1
            userColorImageView.forEachIndexed { i, imageView ->
                if(imageView.isSelected){
                    selectedView = imageView
                    index = i
                    return@setOnClickListener
                }
            }

            selectedView?.let {
                saveUserColor(index, picker.getColor())
            }
        }


        dialog.setContentView(view)
        dialog.show()
    }

*/


}
