package com.rocketseat.rocketia

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var appContext: Context

    // Roda sempre antes de cada chamada de função existente.
    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        assertEquals("com.rocketseat.rocketia", appContext)
    }

    fun readAppNameFromResource() {
        val appName = appContext.getString(R.string.app_name)

        assertEquals("RocketIA", appName)

    }

}