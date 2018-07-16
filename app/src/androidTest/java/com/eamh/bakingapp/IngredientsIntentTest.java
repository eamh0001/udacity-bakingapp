package com.eamh.bakingapp;

import android.content.ComponentName;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class IngredientsIntentTest {

    @Rule
    public IntentsTestRule<MainActivity_> mActivityRule = new IntentsTestRule<>(MainActivity_.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    /**
     * For phones, or tablets with width less than 800dp
     * an intent to RecipeDetailsActivity_ is fired
     */
    @Test
    public void checkIntentOnIngredientsClicked() {
        onView(withId(R.id.rvRecipeList))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withText("Ingredients")).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), RecipeDetailsActivity_.class)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}