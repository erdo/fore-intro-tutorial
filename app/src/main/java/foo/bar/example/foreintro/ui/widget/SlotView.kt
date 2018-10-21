package foo.bar.example.foreintro.ui.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.widget.ImageView
import foo.bar.example.foreintro.R
import foo.bar.example.foreintro.feature.SlotMachineModel.State

/**
 *
 * If we have an AnimationDrawable as a background, start it as soon as we are inflated
 *
 * Copyright © 2018 early.co. All rights reserved.
 */
class SlotView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : ImageView(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        startAimIfPresent()
    }

    fun setState(state: State) {
        when (state) {
            State.SPINNING -> {
                setBackgroundResource(R.drawable.anim_working)
                startAimIfPresent()
            }
            State.CHERRY -> setBackgroundResource(R.drawable.slots_cherry)
            State.DICE -> setBackgroundResource(R.drawable.slots_dice)
            State.BELL -> setBackgroundResource(R.drawable.slots_bell)
        }
    }

    private fun startAimIfPresent(){
        try {
            (background as AnimationDrawable).start()
        }catch (t: Throwable){
            //not an AnimationDrawable or we are not inflated
        }
    }
}
