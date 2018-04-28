package com.takehome.denshaotoko.takehome.github;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.takehome.denshaotoko.takehome.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;

/**
 * Tests for the TakeHome main Screen
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GithubScreenTest {

    private final static String USERID1 = "octocat";

    private final static String USERID2 = "octokit";

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<GithubActivity> mGithubActivityTestRule = new ActivityTestRule<>(GithubActivity.class);

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    @Test
    public void loadGithubData_checkRepoCount() throws Exception {

        // Enter text to edit text
        onView(withId(R.id.et_search)).perform(clearText(), typeText(USERID1));

        // click on the search button
        onView(withId(R.id.bt_search)).perform(click());

        // Wait till data is fetched
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.list_repo_details)).check(new RecyclerViewItemCountAssertion(7));

    }

    @Test
    public void clickOnView_checkDialogShown() throws Exception {

        // Enter text to edit text
        onView(withId(R.id.et_search)).perform(clearText(), typeText(USERID2));

        // click on the search button
        onView(withId(R.id.bt_search)).perform(click());

        // Wait till data is fetched
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.list_repo_details)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));

        onView(withId(R.id.tv_last_update)).check(matches(isDisplayed()));

    }

}
