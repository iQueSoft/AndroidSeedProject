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

import com.google.firebase.auth.FirebaseUser;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.di.components.LoginComponent;
import net.iquesoft.project.seed.presentation.presenter.UserSignUpPresenter;
import net.iquesoft.project.seed.presentation.view.interfaces.LoginView;
import net.iquesoft.project.seed.utils.ToastMaker;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpFragment extends BaseFragment implements LoginView, View.OnFocusChangeListener {
    @Inject
    UserSignUpPresenter presenter;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.btnSignUp)
    Button btnLogin;
    @BindView(R.id.tvGoToLogIn)
    TextView tvGoToSignUp;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilRegistrationPassword)
    TextInputLayout tilRegistrationPassword;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etRegistrationPassword)
    TextInputEditText etRegistrationPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();


    }

    @Override
    void initializeInjection() {
        getComponent(LoginComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.presenter.setView(this);
    }

    @OnClick(R.id.btnSignUp)
    protected void signUp() {
        this.presenter.signUpWithEmailAndPassword(etEmail.getText().toString(), etRegistrationPassword.getText().toString());
    }

    @OnClick(R.id.tvGoToLogIn)
    protected void goToLogIn() {
        navigator.navigateToLogInFragment(fragmentManager);
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
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.etEmail:
                    tilEmail.setError(null);
                    break;
                case R.id.etRegistrationPassword:
                    tilRegistrationPassword.setError(null);
                    break;
            }
        }
    }


    @Override
    public void clearTextField() {
        tilEmail.setErrorEnabled(false);
        tilRegistrationPassword.setErrorEnabled(false);
    }

    @Override
    public void showEmailError(String errorMessage) {
        tilEmail.setError(errorMessage);
    }

    @Override
    public void showPasswordError(String errorMessage) {
        tilRegistrationPassword.setError(errorMessage);

    }

    @Override
    public void updateUI(FirebaseUser firebaseUser) {

    }
}
