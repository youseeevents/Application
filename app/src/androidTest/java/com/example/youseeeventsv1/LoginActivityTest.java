package com.example.youseeeventsv1;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);
    @Test
    public void testLogin() {

        onView(withId(R.id.user_email)).perform(typeText("testemail@website.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("testpassword"), closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

    }

}