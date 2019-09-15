package com.arpadfodor.android.chain_reaction.view.subclass

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.arpadfodor.android.chain_reaction.R
import com.arpadfodor.android.chain_reaction.presenter.AudioPresenter

/**
 * Base Button of the app - can be inherited from
 */
open class BaseButton : Button{

    /**
     * Appearing animation of the Button
     */
    private val appearingAnimation : Animation? = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_top)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        this.background = resources.getDrawable(R.drawable.base_button)
        this.setTextColor(resources.getColor(R.color.colorButtonText))
        this.isAllCaps = true
        this.gravity = Gravity.CENTER
        this.setPadding(40,40,40,40)

        this.setOnClickListener {
            AudioPresenter.soundLocked()
        }

    }

    /**
     * Sets the Button on click listener
     *
     * @param    func        Lambda to execute when the Button is pressed
     */
    fun setOnClickEvent(func: () -> Unit){
        this.setOnClickListener {
            AudioPresenter.soundButtonClick()
            func()
        }
    }

    /**
     * Starts the Button's appearing animation
     */
    fun startAppearingAnimation(){
        this.animation = appearingAnimation
        this.animation?.start()
        this.animation?.fillAfter = true
    }

}