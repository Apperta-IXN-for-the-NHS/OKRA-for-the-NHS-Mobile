package org.apperta.okramobile;

import android.content.Context;
import android.view.Menu;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivityNavigationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    private BottomNavigationView navigationView;

    @Before
    public void setupNavigation() {
        MainActivity activity = activityTestRule.getActivity();
        navigationView = activity.findViewById(R.id.nav_view);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("org.apperta.okramobile", appContext.getPackageName());
    }

    @Test
    public void navigationMenuHasCorrectOptions() {
        Menu menu = navigationView.getMenu();

        assertEquals(3, menu.size());
        assertEquals(R.id.navigation_knowledge, menu.getItem(0).getItemId());
        assertEquals(R.id.navigation_cases, menu.getItem(1).getItemId());
        assertEquals(R.id.navigation_contact_us, menu.getItem(2).getItemId());
    }

}
