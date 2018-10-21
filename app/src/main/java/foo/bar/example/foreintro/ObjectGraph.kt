package foo.bar.example.foreintro

import android.app.Application
import co.early.fore.core.Affirm.notNull
import co.early.fore.core.WorkMode
import co.early.fore.core.logging.AndroidLogger
import co.early.fore.core.logging.Logger
import foo.bar.example.foreintro.feature.RandomStateFetcher
import foo.bar.example.foreintro.feature.SlotMachineModel
import java.util.HashMap

/**
 *
 * Copyright © 2018 early.co. All rights reserved.
 */
internal class ObjectGraph {

    @Volatile
    private var initialized = false
    private val dependencies = HashMap<Class<*>, Any>()

    @JvmOverloads
    fun setApplication(application: Application, workMode: WorkMode = WorkMode.ASYNCHRONOUS) {

        notNull(application)
        notNull(workMode)


        // create dependency graph
        val logger = AndroidLogger("SLOTS_")
        val slotMachineModel = SlotMachineModel(RandomStateFetcher(), logger, WorkMode.ASYNCHRONOUS)


        // add models to the dependencies map if you will need them later
        dependencies.put(SlotMachineModel::class.java, slotMachineModel)
        dependencies.put(Logger::class.java, logger)

    }

    fun init() {
        if (!initialized) {
            initialized = true

            // run any necessary initialization code once object graph has been created here

//            For example, something like:
//
//            val slotMachineModel = get(SlotMachineModel::class.java)
//            slotMachineModel.init()

        }
    }

    operator fun <T> get(model: Class<T>): T {

        notNull(model)
        val t = model.cast(dependencies[model])
        notNull(t)

        return t
    }

    fun <T> putMock(clazz: Class<T>, `object`: T) {

        notNull(clazz)
        notNull(`object`)

        dependencies.put(clazz, `object`!!)
    }

}
