package com.emis.emismobile;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.emis.emismobile.knowledge.KnowledgeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivityNavigationTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.emis.emismobile", appContext.getPackageName());
    }

    @Test
    public void whenClickOnKnowledgeOpenCorrespondingActivity() {
        onView(withId(R.id.open_knowledge_button)).perform(click());

        intended(hasComponent(hasShortClassName(".knowledge.KnowledgeActivity")));
    }

    @Test
    public void whenClickOnContactUsOpenCorrespondingActivity() {
        onView(withId(R.id.open_contact_us_button)).perform(click());

        intended(hasComponent(hasShortClassName(".contact.ContactUsActivity")));
    }
}
