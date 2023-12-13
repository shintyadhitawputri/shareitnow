package com.dicoding.mysubmissionstory.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.mysubmissionstory.R

class MyEditTextPassword : AppCompatEditText {

    private var isPasswordAppear: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        val watcherPassword = resources.getDrawable(R.drawable.baseline_eye_24)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, watcherPassword, null)

        setOnClickListener {
            isPasswordAppear = !isPasswordAppear
            transformationMethod = if (isPasswordAppear) {
                null
            } else {
                PasswordTransformationMethod.getInstance()
            }
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError(context.getString(R.string.passwordwarning), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}

