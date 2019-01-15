package foo.bar.example.foreintro.feature

import co.early.fore.core.WorkMode
import co.early.fore.core.logging.Logger
import co.early.fore.core.observer.ObservableImp
import co.early.fore.core.threading.AsyncBuilder
import java.util.Random

/**
 *
 * Copyright © 2018 early.co. All rights reserved.
 */
class SlotMachineModel constructor(
        private val stateFetcher: RandomStateFetcher,
        private val logger: Logger,
        private val workMode: WorkMode) : ObservableImp(workMode) {

    enum class State {
        SPINNING,
        CHERRY,
        DICE,
        BELL
    }

    private val rnd = Random()
    private val wheel1 = Wheel(State.CHERRY)
    private val wheel2 = Wheel(State.DICE)
    private val wheel3 = Wheel(State.BELL)


    fun spin() {

        logger.i(TAG, "spin()")

        spinWheel(wheel1)
        spinWheel(wheel2)
        spinWheel(wheel3)
    }

    private fun spinWheel(wheel: Wheel) {

        //if wheel is already spinning, just ignore
        if (wheel.state != State.SPINNING) {
            wheel.state = State.SPINNING
            notifyObservers()
            AsyncBuilder<Unit, State>(workMode)
                .doInBackground {
                    stateFetcher.fetchRandom(randomDelayMs())
                }
                .onPostExecute { state ->
                    wheel.state = state; notifyObservers()
                }
                .execute()
        }
    }

    fun getState1(): State {
        logger.i(TAG, "getState1() $wheel1.state")
        return wheel1.state
    }

    fun getState2(): State {
        logger.i(TAG, "getState2() $wheel2.state")
        return wheel2.state
    }

    fun getState3(): State {
        logger.i(TAG, "getState3() $wheel3.state")
        return wheel3.state
    }

    fun isWon(): Boolean {
        return (wheel1.state == wheel2.state
                && wheel2.state == wheel3.state
                && wheel1.state != State.SPINNING)
    }

    //anywhere between 1 and 10 seconds
    private fun randomDelayMs(): Long {
        return (1000 + rnd.nextInt(8) * 1000 + rnd.nextInt(1000)).toLong()
    }

    internal inner class Wheel(var state: State)

    companion object {
        val TAG = SlotMachineModel::class.java.simpleName
    }
}
