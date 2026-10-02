package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.domain.UniversalExecutionEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AIISG", appName)
  }

  @Test
  fun `verify universal execution loop lifecycle has 14 stages`() {
    assertEquals(14, UniversalExecutionEngine.loopStages.size)
    assertEquals("RECEIVE", UniversalExecutionEngine.loopStages.first())
    assertEquals("COMPLETE", UniversalExecutionEngine.loopStages.last())
    assertTrue(UniversalExecutionEngine.loopStages.contains("TEST"))
    assertTrue(UniversalExecutionEngine.loopStages.contains("VERIFY"))
    assertTrue(UniversalExecutionEngine.loopStages.contains("FIX"))
  }
}
