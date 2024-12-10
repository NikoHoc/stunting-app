package com.dicoding.stunting.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.stunting.R

class EmailEditText: AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = resources.getString(R.string.email)
        setHintTextColor(resources.getColor(R.color.white, null))
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                when {
                    email.isBlank() -> {
                        error = context.getString(R.string.empty_input)
                    }
                    !email.contains("@") -> {
                        error = context.getString(R.string.email_must_contain_at)
                    }
                    !email.endsWith("gmail.com") -> {
                        error = context.getString(R.string.email_must_end_with_gmail_com)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

}