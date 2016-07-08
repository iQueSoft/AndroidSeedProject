package net.iquesoft.project.seed.presentation.di.modules;

import android.content.Context;

import net.iquesoft.project.seed.presentation.di.scopes.ActivityScope;
import net.iquesoft.project.seed.presentation.navigation.Navigator;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.presenter.UserSignUpPresenter;
import net.iquesoft.project.seed.presentation.view.activity.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 07-Jul-16.
 */
@Module
public class LoginModule {

    private MainActivity mainActivity;

    public LoginModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Provides
    @ActivityScope
    MainActivity provideMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    UserLoginPresenter provideUserLoginPresenter(Context context) {
        return UserLoginPresenter.getInstance(context);
    }

    @Provides
    @ActivityScope
    UserSignUpPresenter provideUserSignUpPresenter(Context context) {
        return UserSignUpPresenter.getInstance(context);
    }

    @Provides
    @ActivityScope
    Navigator provideNavigator() {
        return new Navigator();
    }
}
