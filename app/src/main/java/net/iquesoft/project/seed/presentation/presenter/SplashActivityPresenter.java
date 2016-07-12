package net.iquesoft.project.seed.presentation.presenter;

import android.os.Handler;

import net.iquesoft.project.seed.presentation.view.activity.SplashActivity;
import net.iquesoft.project.seed.presentation.view.fragment.SplashFragment;

public class SplashActivityPresenter implements Presenter {

    private final SplashActivity activity;
    private SplashFragment fragmentView;

    public SplashActivityPresenter(SplashActivity splashActivity) {
        this.activity = splashActivity;
    }

    public void setView(SplashFragment splashFragment) {
        this.fragmentView = splashFragment;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void initiateAppLaunching() {
        fragmentView.showLoading();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                * Some work like authenticating, downloading or something else could be done here
                */
                fragmentView.hideLoading();
                fragmentView.navigateToMainActivity();
            }
        }, 3000);

    }
}
