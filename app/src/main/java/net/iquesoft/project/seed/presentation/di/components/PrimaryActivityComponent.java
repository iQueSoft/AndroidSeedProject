package net.iquesoft.project.seed.presentation.di.components;

import net.iquesoft.project.seed.presentation.di.PerActivity;
import net.iquesoft.project.seed.presentation.di.modules.ActivityModule;
import net.iquesoft.project.seed.presentation.di.modules.PrimaryActivityModule;
import net.iquesoft.project.seed.presentation.view.activity.PrimaryActivity;

import dagger.Component;

/**
 * Created by andre on 09-Jul-16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, PrimaryActivityModule.class})
public interface PrimaryActivityComponent extends ActivityComponent {
    void inject(PrimaryActivity primaryActivity);
}
