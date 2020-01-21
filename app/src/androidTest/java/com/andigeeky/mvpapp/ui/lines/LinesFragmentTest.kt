package com.andigeeky.mvpapp.ui.lines

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.andigeeky.mvpapp.R
import com.andigeeky.mvpapp.api.vo.Resource
import com.andigeeky.mvpapp.lines.presentation.LinesFragment
import com.andigeeky.mvpapp.lines.presentation.LinesViewModel
import com.andigeeky.mvpapp.lines.data.vo.Line
import com.andigeeky.mvpapp.testing.MainActivity
import com.andigeeky.mvpapp.ui.common.FragmentBindingAdapters
import com.andigeeky.mvpapp.ui.util.TestUtil
import com.andigeeky.mvpapp.ui.util.mock
import com.andigeeky.mvpapp.utils.*
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LinesFragmentTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()
    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)
    private lateinit var viewModel: LinesViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters
    private val linesData = MutableLiveData<Resource<List<Line>>>()
    private val testFragment = TestLinesFragment()

    @Before
    fun init() {
        viewModel = mock()
        `when`(viewModel.getLines()).thenReturn(linesData)
        mockBindingAdapter = mock()

        testFragment.appExecutors = countingAppExecutors.appExecutors
        testFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        testFragment.dataBindingComponent = object : DataBindingComponent {
            override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                return mockBindingAdapter
            }
        }
        activityRule.activity.setFragment(testFragment)
        EspressoTestUtil.disableProgressBarAnimations(activityRule)
    }

    @Test
    fun test_loading_dialog_when_state_is_LOADING() {
        linesData.postValue(Resource.loading(null))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun test_error_message_when_state_is_ERROR() {
        linesData.postValue(Resource.error("error", null))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_msg)).check(matches(withText("error")))
    }

    @Test
    fun test_data_is_reflected_in_recycler_view() {
        val lines = getLines(2)
        for (pos in lines.indices) {
            val line = lines[pos]
            onView(listMatcher().atPosition(pos)).apply {
                check(matches(hasDescendant(withText(line.name))))
            }
        }
        val line = getLines(3)[2]
        onView(listMatcher().atPosition(2)).check(
            matches(hasDescendant(withText(line.name)))
        )
    }

    @Test
    fun test_error_data_is_reflected_in_recycler_view() {
        getLines(5)
        onView(listMatcher().atPosition(1)).check(matches(isDisplayed()))
        linesData.postValue(Resource.error("error", null))
        onView(listMatcher().atPosition(0)).check(doesNotExist())
    }

    private fun listMatcher() = RecyclerViewMatcher(R.id.listLines)

    private fun getLines(count: Int): List<Line> {
        val lines = TestUtil.createLines(count)
        linesData.postValue(Resource.success(lines))
        return lines
    }

    class TestLinesFragment : LinesFragment() {
        override fun navController() = mock<NavController>()
    }
}
