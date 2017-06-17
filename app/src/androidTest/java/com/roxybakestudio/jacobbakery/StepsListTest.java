package com.roxybakestudio.jacobbakery;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.ui.StepsListActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;


@RunWith(AndroidJUnit4.class)
public class StepsListTest {

    @Rule
    public final ActivityTestRule<StepsListActivity> mStepsList =
            new ActivityTestRule<>(StepsListActivity.class);

    @Test
    public void checkIntent() {

        Intent resultData = new Intent();

        Uri uri = RecipeContract.RecipeMain.BuildRecipeUriWithId(1);
        String title = "Nutella Pie";

        resultData.setData(uri);
        resultData.putExtra("title", title);

        String packageName = InstrumentationRegistry.getTargetContext().getPackageName();
        ComponentName componentName = new ComponentName(packageName,
                StepsListActivity.class.getName());
        resultData.setComponent(componentName);

        Intents.init();
        InstrumentationRegistry.getContext().startActivity(resultData);

        Matcher<Intent> expectedIntent = hasComponent(componentName);
        intended(expectedIntent);
        Intents.release();


    }
}
