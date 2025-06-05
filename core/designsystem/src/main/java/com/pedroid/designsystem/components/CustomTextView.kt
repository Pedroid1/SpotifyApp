package com.pedroid.designsystem.components

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.pedroid.core.design_system.R

class CustomTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomTextView).apply {
            val textType = EnumTextType.fromIndex(
                getInt(
                    R.styleable.CustomTextView_textType,
                    EnumTextType.BODY1.ordinal
                )
            )
            val isTitle = getBoolean(R.styleable.CustomTextView_isTitle, false)
            val priority = EnumTextViewPriority.fromIndex(
                getInt(
                    R.styleable.CustomTextView_textPriority,
                    EnumTextViewPriority.PRIMARY.ordinal
                )
            )

            applyTextStyle(textType)
            applyAccessibility(isTitle)
            if (hasValue(R.styleable.CustomTextView_customTextColor)) {
                val customTextColor = getColor(
                    R.styleable.CustomTextView_customTextColor,
                    ContextCompat.getColor(context, R.color.primary_text_color)
                )
                setTextColor(customTextColor)
            } else {
                applyPriorityColor(priority)
            }

            recycle()
        }

        letterSpacing = 0.03f
    }

    private fun applyTextStyle(type: EnumTextType) {
        val fontResId = when (type) {
            EnumTextType.TITLE1 -> R.font.rubik_bold
            EnumTextType.TITLE2 -> R.font.rubik_semi_bold
            EnumTextType.TITLE3 -> R.font.rubik_bold
            EnumTextType.SUBTITLE1 -> R.font.rubik_semi_bold
            EnumTextType.SUBTITLE2 -> R.font.rubik_bold
            EnumTextType.BODY1 -> R.font.rubik_regular
            EnumTextType.BODY2 -> R.font.rubik_medium
            EnumTextType.SMALL -> R.font.rubik_semi_bold
            EnumTextType.EXTRA_SMALL -> R.font.rubik_light
        }

        val textSizeSp = when (type) {
            EnumTextType.TITLE1 -> 24f
            EnumTextType.TITLE2 -> 18f
            EnumTextType.TITLE3,
            EnumTextType.SUBTITLE1,
            EnumTextType.BODY1 -> 16f

            EnumTextType.SUBTITLE2,
            EnumTextType.BODY2 -> 14f

            EnumTextType.SMALL,
            EnumTextType.EXTRA_SMALL -> 12f
        }

        typeface = ResourcesCompat.getFont(context, fontResId)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp)
    }

    private fun applyAccessibility(isTitle: Boolean) {
        ViewCompat.setAccessibilityHeading(this, isTitle)
    }

    private fun applyPriorityColor(priority: EnumTextViewPriority) {
        val colorResId = when (priority) {
            EnumTextViewPriority.PRIMARY -> R.color.primary_text_color
            EnumTextViewPriority.SECONDARY -> R.color.secondary_text_color
        }
        setTextColor(ContextCompat.getColor(context, colorResId))
    }

    enum class EnumTextType {
        TITLE1, TITLE2, TITLE3,
        SUBTITLE1, SUBTITLE2,
        BODY1, BODY2,
        SMALL, EXTRA_SMALL;

        companion object {
            fun fromIndex(index: Int): EnumTextType =
                entries.getOrNull(index) ?: BODY1
        }
    }

    enum class EnumTextViewPriority {
        PRIMARY, SECONDARY;

        companion object {
            fun fromIndex(index: Int): EnumTextViewPriority =
                entries.getOrElse(index) { PRIMARY }
        }
    }
}
