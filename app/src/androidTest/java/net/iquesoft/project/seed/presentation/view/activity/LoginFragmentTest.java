package net.iquesoft.project.seed.presentation.view.activity;

import android.test.ActivityInstrumentationTestCase2;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;

import org.junit.After;
import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class LoginFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
    UserLoginPresenter presenter;
    private String fakeUserLogin = "fakeUserLogin";
    private String fakeUserPassword = "fakeUserPassword123";

    public LoginFragmentTest() {
        super(TestFragmentActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new LoginFragment(), null).commit();
        presenter = UserLoginPresenter.getInstance(getActivity());
        assertNotNull(presenter);
    }

    public void testControlsCreated() throws Exception {
        onView(withId(R.id.etLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_google)).check(matches(isDisplayed()));
        onView(withId(R.id.tvGoToSignUp)).check(matches(isDisplayed()));
    }

    public void testLoginField() throws Exception {
        onView(withId(R.id.etLogin)).perform(typeText(fakeUserLogin), closeSoftKeyboard());
        onView(withId(R.id.etLogin)).check(matches(withText(fakeUserLogin)));
    }

    public void testPasswordField() throws Exception {
        onView(withId(R.id.etPassword)).perform(typeText(fakeUserPassword), closeSoftKeyboard());
        onView(withId(R.id.etPassword)).check(matches(withText(fakeUserPassword)));
    }


    public void testGoToSignUpButton() throws Exception {
        onView(withId(R.id.tvGoToSignUp)).perform(click());
        onView(withId(R.id.btnSignUp)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}