package net.iquesoft.project.seed.presentation.di.components;

import net.iquesoft.project.seed.presentation.di.PerActivity;
import net.iquesoft.project.seed.presentation.di.modules.ActivityModule;
import net.iquesoft.project.seed.presentation.di.modules.SplashActivityModule;
import net.iquesoft.project.seed.presentation.view.activity.SplashActivity;
import net.iquesoft.project.seed.presentation.view.fragment.SplashFragment;

import dagger.Component;

/**
 * Created by andre on 11-Jul-16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, SplashActivityModule.class})
public interface SplashActivityComponent extends ActivityComponent {
    void inject(SplashActivity splashActivity);

    void inject(SplashFragment splashFragment);
}
