package net.iquesoft.project.seed.presentation.di.modules;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.domain.firebase.MyFirebaseAuth;
import net.iquesoft.project.seed.presentation.di.PerActivity;
import net.iquesoft.project.seed.presentation.model.UserModel;
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
    @PerActivity
    MainActivity provideMainActivity() {
        return mainActivity;
    }

    @Provides
    MyFirebaseAuth provideMyFirebaseAuth() {
        return new MyFirebaseAuth(mainActivity);
    }
    @Provides
    @PerActivity
    UserLoginPresenter provideUserLoginPresenter(MainActivity mainActivity,
                                                 MyFirebaseAuth myFirebaseAuth,
                                                 RegularExpressionValidation regularExpressionValidation,
                                                 UserModel userModel) {
        return new UserLoginPresenter(mainActivity, myFirebaseAuth, regularExpressionValidation, userModel);
    }

    @Provides
    @PerActivity
    UserSignUpPresenter provideUserSignUpPresenter(MainActivity mainActivity,
                                                   MyFirebaseAuth myFirebaseAuth,
                                                   RegularExpressionValidation regularExpressionValidation,
                                                   UserModel userModel) {
        return new UserSignUpPresenter(mainActivity, myFirebaseAuth, regularExpressionValidation, userModel);
    }

    @Provides
    @PerActivity
    RegularExpressionValidation provideValidation() {
        return new RegularExpressionValidation();
    }
}
