package net.iquesoft.project.seed.presentation.di.components;


import net.iquesoft.project.seed.presentation.di.PerActivity;
import net.iquesoft.project.seed.presentation.di.modules.ActivityModule;
import net.iquesoft.project.seed.presentation.di.modules.LoginModule;
import net.iquesoft.project.seed.presentation.view.activity.MainActivity;
import net.iquesoft.project.seed.presentation.view.fragment.GalleryFragment;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.presentation.view.fragment.SignUpFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LoginModule.class})
public interface LoginComponent extends ActivityComponent {

    void inject(LoginFragment loginFragment);

    void inject(SignUpFragment signUpFragment);

    void inject(GalleryFragment galleryFragment);

    void inject(MainActivity mainActivity);

}
