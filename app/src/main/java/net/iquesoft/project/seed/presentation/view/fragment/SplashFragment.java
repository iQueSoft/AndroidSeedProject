package net.iquesoft.project.seed.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trncic.library.DottedProgressBar;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.di.components.SplashActivityComponent;
import net.iquesoft.project.seed.presentation.presenter.SplashActivityPresenter;
import net.iquesoft.project.seed.presentation.view.interfaces.LoadDataView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashFragment extends BaseFragment implements LoadDataView {

    @Inject
    SplashActivityPresenter presenter;

    @BindView(R.id.progress_bar_splash)
    DottedProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setView(this);
        presenter.initiateAppLaunching();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    void initializeInjection() {
        getComponent(SplashActivityComponent.class).inject(this);

    }

    @Override
    public void showLoading() {
        progressBar.startProgress();
    }

    @Override
    public void hideLoading() {
        progressBar.stopProgress();
    }

    @Override
    public void showError(String message) {

    }

    public void navigateToMainActivity() {
        navigator.navigateToMainActivity(getContext());
    }
}
