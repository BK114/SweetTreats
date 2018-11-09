package bk_dev.apps.brockrice.sweettreats.activities;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.example.brockrice.sweettreats.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by brockrice on 1/25/18.
 */

@RunWith(JUnit4.class)
public class RecipeStepsListTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void showBakingSteps() {

        onView(withId(R.id.recycler_view_main)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.recycler_view_steps)).check(matches(isDisplayed()));
    }
}
