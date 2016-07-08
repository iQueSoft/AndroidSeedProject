package net.iquesoft.project.seed.presentation.di.modules;

import net.iquesoft.project.seed.presentation.di.PerActivity;
import net.iquesoft.project.seed.presentation.model.UserModel;
import net.iquesoft.project.seed.presentation.presenter.PrimaryActivityPresenter;
import net.iquesoft.project.seed.presentation.view.activity.PrimaryActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 09-Jul-16.
 */
@Module
public class PrimaryActivityModule {
    private PrimaryActivity primaryActivity;

    public PrimaryActivityModule(PrimaryActivity primaryActivity) {
        this.primaryActivity = primaryActivity;
    }

    @Provides
    @PerActivity
    PrimaryActivity provideActivity() {
        return primaryActivity;
    }

    @Provides
    @PerActivity
    PrimaryActivityPresenter providePresenter(PrimaryActivity primaryActivity, UserModel userModel) {
        return new PrimaryActivityPresenter(primaryActivity, userModel);
    }
}
