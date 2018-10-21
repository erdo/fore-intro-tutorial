package foo.bar.example.foreintro.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import co.early.fore.core.logging.Logger
import co.early.fore.core.ui.SyncableView
import foo.bar.example.foreintro.App
import foo.bar.example.foreintro.feature.SlotMachineModel
import kotlinx.android.synthetic.main.activity_main.view.slots_1_slotview
import kotlinx.android.synthetic.main.activity_main.view.slots_2_slotview
import kotlinx.android.synthetic.main.activity_main.view.slots_3_slotview
import kotlinx.android.synthetic.main.activity_main.view.slots_handle_imageview
import kotlinx.android.synthetic.main.activity_main.view.slots_win

/**
 *
 * Copyright © 2018 early.co. All rights reserved.
 */
class SlotMachineView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        RelativeLayout(context, attrs, defStyleAttr), SyncableView {


    //models that we need
    private lateinit var slotMachineModel: SlotMachineModel
    private lateinit var logger: Logger

    //single observer reference
    internal var observer = this::syncView


    override fun onFinishInflate() {
        super.onFinishInflate()

        //(get view references handled for us by kotlin tools)

        getModelReferences()

        setClickListeners()
    }


    private fun getModelReferences() {
        slotMachineModel = App.get(SlotMachineModel::class.java)
        logger = App.get(Logger::class.java)
    }

    private fun setClickListeners() {
        slots_handle_imageview.setOnClickListener { slotMachineModel.spin() }
    }


    //data binding stuff below - syncView() is a similar concept to the render() method in MVI

    override fun syncView() {
        slots_1_slotview.setState(slotMachineModel.getState1())
        slots_2_slotview.setState(slotMachineModel.getState2())
        slots_3_slotview.setState(slotMachineModel.getState3())
        slots_win.visibility = if (slotMachineModel.isWon()) View.VISIBLE else View.INVISIBLE
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        slotMachineModel.addObserver(observer)
        syncView() //  <- don't forget this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        slotMachineModel.removeObserver(observer)
    }

}
