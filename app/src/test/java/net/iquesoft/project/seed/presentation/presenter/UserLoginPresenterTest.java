package net.iquesoft.project.seed.presentation.presenter;

import android.content.Context;

import net.iquesoft.project.seed.utils.Constants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserLoginPresenterTest {
    Context context;
    UserLoginPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = UserLoginPresenter.getInstance(context);
    }

    @Test
    public void testLogIn() throws Exception {
        String fakePass = "fakePass123";
        String fakeEmail = "fake.user_email@log.ina";
        Assert.assertEquals(Constants.CODE_OK, presenter.logIn(fakeEmail, fakePass));
    }
}