package net.iquesoft.project.seed.presentation.di.components;


import net.iquesoft.project.seed.presentation.di.modules.LoginModule;
import net.iquesoft.project.seed.presentation.di.scopes.ActivityScope;
import net.iquesoft.project.seed.presentation.view.activity.MainActivity;
import net.iquesoft.project.seed.presentation.view.fragment.GalleryFragment;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.presentation.view.fragment.SignUpFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {LoginModule.class})
public interface LoginComponent {
    void inject(LoginFragment loginFragment);

    void inject(SignUpFragment signUpFragment);

    void inject(GalleryFragment galleryFragment);

    void inject(MainActivity mainActivity);
}
