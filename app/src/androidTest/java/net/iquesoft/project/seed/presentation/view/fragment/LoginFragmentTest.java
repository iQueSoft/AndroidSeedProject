package net.iquesoft.project.seed.presentation.view.fragment;

import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.facebook.FacebookSdk;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.view.activity.TestFragmentActivity;
import net.iquesoft.project.seed.utils.ToastMaker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginFragmentTest {

    @Rule
    public ActivityTestRule<TestFragmentActivity> activityTestRule =
            new ActivityTestRule<>(TestFragmentActivity.class);
    private String testString = "Test String";
    private String testErrorString = "Test error string";

    private static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity();
        FacebookSdk.sdkInitialize(activityTestRule.getActivity());
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new LoginFragment(), null).commit();
    }

    @Test
    public void testControlsCreated() throws Exception {
        onView(withId(R.id.etLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_google)).check(matches(isDisplayed()));
        onView(withId(R.id.tvGoToSignUp)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginField() throws Exception {
        onView(withId(R.id.etLogin)).perform(typeText(testString), closeSoftKeyboard());
        onView(withId(R.id.etLogin)).check(matches(withText(testString)));
    }

    @Test
    public void testLoginError() throws Exception {
        showLoginError();
        onView(withId(R.id.tilLogin)).check(matches(hasTextInputLayoutErrorText(testErrorString)));
    }

    @Test
    public void testPasswordError() throws Exception {
        showPasswordError();
        onView(withId(R.id.tilPassword)).check(matches(hasTextInputLayoutErrorText(testErrorString)));
    }

    @Test
    public void testPasswordField() throws Exception {
        onView(withId(R.id.etPassword)).perform(typeText(testString), closeSoftKeyboard());
        onView(withId(R.id.etPassword)).check(matches(withText(testString)));
    }

    @Test
    public void testProgressBar() throws Exception {
        onView(withId(R.id.rl_progress)).check(matches(not(isDisplayed())));
        showProgress();
        onView(withId(R.id.rl_progress)).check(matches(isDisplayed()));
        hideProgress();
        onView(withId(R.id.rl_progress)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testGoToSignUp() throws Exception {
        onView(withId(R.id.tvGoToSignUp)).perform(click());
        onView(withId(R.id.tvGoToLogIn)).check(matches(isDisplayed()));
        onView(withId(R.id.tvGoToLogIn)).perform(click());
        onView(withId(R.id.tvGoToSignUp)).check(matches(isDisplayed()));
    }

    @Test
    public void testShowError() throws Exception {
        showToast();
        onView(withText(testString)).inRoot(withDecorView(
                not(is(activityTestRule.getActivity().
                        getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    private void showLoginError() {
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextInputLayout) activityTestRule.getActivity().findViewById(R.id.tilLogin)).setError(testErrorString);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void showPasswordError() {
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextInputLayout) activityTestRule.getActivity().findViewById(R.id.tilPassword)).setError(testErrorString);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void showToast() {
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastMaker.showMessage(activityTestRule.getActivity(), testString, ToastMaker.duration.LONG);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void showProgress() {
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    (activityTestRule.getActivity().findViewById(R.id.rl_progress)).setVisibility(View.VISIBLE);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void hideProgress() {
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    (activityTestRule.getActivity().findViewById(R.id.rl_progress)).setVisibility(View.INVISIBLE);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}