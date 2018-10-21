package foo.bar.example.foreintro


import android.app.Application
import co.early.fore.core.Affirm
import co.early.fore.core.WorkMode

/**
 * Try not to fill this class with lots of code, if possible move it to a model somewhere
 *
 * Copyright © 2018 early.co. All rights reserved.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        inst = this

        objectGraph = ObjectGraph()
        objectGraph.setApplication(this)
    }

    fun injectSynchronousObjectGraph() {
        objectGraph = ObjectGraph()
        objectGraph.setApplication(this, WorkMode.SYNCHRONOUS)
    }

    fun <T> injectMockObject(clazz: Class<T>, `object`: T) {
        objectGraph.putMock(clazz, `object`)
    }

    companion object {

        var inst: App? = null
            private set
        private lateinit var objectGraph: ObjectGraph

        // unfortunately the android test runner calls Application.onCreate() once _before_ we get a
        // chance to call createApplication() in ApplicationTestCase (contrary to the documentation).
        // So to prevent initialisation stuff happening before we have had a chance to set our mocks
        // during tests, we need to separate out the loadPwdDb() stuff, which is why we put it here,
        // to be called by the base activity of the app
        // http://stackoverflow.com/questions/4969553/how-to-prevent-activityunittestcase-from-calling-application-oncreate
        fun init() {
            objectGraph.init()
        }

        /**
         * This is how dependencies get injected, typically an Activity/Fragment/View will call this
         * during the onCreate()/onCreateView()/onFinishInflate() method respectively for each of the
         * dependencies it needs.
         *
         *
         * Can use the dagger library for similar behaviour using annotations
         *
         *
         * Will return mocks if they have been injected previously in injectMockObject()
         *
         *
         * Call it like this:  YourModel yourModel =
         * App.get(YourModel.class);
         *
         *
         * If you want to more tightly scoped object, one way is to pass a factory class here and create
         * an instance where you need it
         *
         * @param s
         * @return
         */
        operator fun <T> get(s: Class<T>): T {
            Affirm.notNull(objectGraph)
            return objectGraph.get(s)
        }
    }

}
