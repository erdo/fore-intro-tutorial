package foo.bar.example.foreintro.feature

import java.util.Random

/**
 *
 * We put this in a separate class because it makes it easy to test SlotMachineModel later by
 * mocking out this class. If you wanted to get your slot machine states from the network, you
 * would do the actual network connection outside of SlotMachineModel in a class like this.
 *
 * Take a look here for sample network layer code: https://erdo.github.io/android-fore/#fore-4-retrofit-example
 *
 * Copyright © 2018 early.co. All rights reserved.
 */

class RandomStateFetcher() {

    private val rnd = Random()

    fun fetchRandom(millisecondsDelay: Long): SlotMachineModel.State {

        //fake wait to simulate physical wheels or network connection
        try {
            Thread.sleep(millisecondsDelay)
        } catch (e: InterruptedException) {
            //move on
        }

        return SlotMachineModel.State.values()[1 + rnd.nextInt(SlotMachineModel.State.values().size - 1)]
    }
}
