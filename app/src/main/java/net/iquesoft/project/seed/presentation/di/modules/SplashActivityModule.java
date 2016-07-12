package net.iquesoft.project.seed.presentation.di.modules;

import net.iquesoft.project.seed.presentation.di.PerActivity;
import net.iquesoft.project.seed.presentation.presenter.SplashActivityPresenter;
import net.iquesoft.project.seed.presentation.view.activity.SplashActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 11-Jul-16.
 */
@Module
public class SplashActivityModule {
    private final SplashActivity splashActivity;

    public SplashActivityModule(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }


    @Provides
    @PerActivity
    SplashActivity provideActivity() {
        return this.splashActivity;
    }


    @Provides
    @PerActivity
    SplashActivityPresenter providePresenter(SplashActivity splashActivity) {
        return new SplashActivityPresenter(splashActivity);
    }
}
