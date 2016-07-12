package net.iquesoft.project.seed.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.di.components.LoginComponent;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.view.activity.MainActivity;
import net.iquesoft.project.seed.presentation.view.interfaces.LoginView;
import net.iquesoft.project.seed.utils.ToastMaker;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements LoginView, View.OnFocusChangeListener {

    @Inject
    UserLoginPresenter presenter;

    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.sign_in_google)
    SignInButton btnSignInGoogle;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvGoToSignUp)
    TextView tvGoToSignUp;
    @BindView(R.id.tilLogin)
    TextInputLayout tilLogin;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.etLogin)
    TextInputEditText etLogin;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentManager = getFragmentManager();
    }

    @Override
    void initializeInjection() {
        getComponent(LoginComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.presenter.setView(this);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.sign_in_facebook);
        presenter.registerFacebookCallback(loginButton);
    }

    @OnClick(R.id.sign_in_google)
    void signInGoogle() {
        presenter.logInWithGoogle(getActivity());
    }

    @OnClick(R.id.btnLogin)
    protected void btnLoginClicked() {
        this.presenter.logInWithEmailAndPassword(etLogin.getText().toString(), etPassword.getText().toString());
    }

    @OnClick(R.id.tvGoToSignUp)
    protected void tvGoToSignUpClicked() {
        navigator.navigateToSignUpFragment(fragmentManager);
    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showError(String message) {
        ToastMaker.showMessage(getActivity(), message, ToastMaker.duration.LONG);
    }

    @Override
    public void clearTextField() {
        tilLogin.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
    }

    @Override
    public void showEmailError(String errorMessage) {
        tilLogin.setError(errorMessage);
    }

    @Override
    public void showPasswordError(String errorMessage) {
        tilPassword.setError(errorMessage);

    }

    @Override
    public void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            ((MainActivity) getActivity()).showUserInfoInUI(firebaseUser);
        } else {
            ((MainActivity) getActivity()).showDefaultUserInUI();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.etLogin:
                    tilLogin.setError(null);
                    break;
                case R.id.etPassword:
                    tilPassword.setError(null);
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

}
