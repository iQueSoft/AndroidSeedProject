package net.iquesoft.project.seed.presentation.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.model.UserModel;
import net.iquesoft.project.seed.presentation.navigation.Navigator;
import net.iquesoft.project.seed.presentation.presenter.UserLoginPresenter;
import net.iquesoft.project.seed.presentation.view.activity.MainActivity;
import net.iquesoft.project.seed.utils.Constants;
import net.iquesoft.project.seed.utils.ToastMaker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class LoginFragment extends BaseFragment implements LoadDataView, View.OnFocusChangeListener {


    UserModel userModel;
    ProgressDialog progressDialog;
    UserLoginPresenter presenter;
    ImageLoader imageLoader;

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


    private Subscription subscription;

    private void onError() {
        hideLoading();
        int code = presenter.getErrorCode();
        if (code == Constants.ERROR_EMPTY_EMAIL) {
            onInvalidEmail(Constants.ERROR_MESSAGE_EMPTY_FIELD);
        } else if (code == Constants.ERROR_EMPTY_PASSWORD) {
            onInvalidPassword(Constants.ERROR_MESSAGE_EMPTY_FIELD);
        } else if (code == Constants.ERROR_INVALID_EMAIL) {
            onInvalidEmail(Constants.ERROR_MESSAGE_INVALID_EMAIL);
        } else if (code == Constants.ERROR_INVALID_PASSWORD) {
            onInvalidPassword(Constants.ERROR_MESSAGE_INVALID_PASSWORD);
        } else if (code == Constants.ERROR_CONNECTION_LOST) {
            showError(Constants.ERROR_MESSAGE_CONNECTION_LOST);
        } else if (code == Constants.ERROR_UNKNOWN) {
            showError(Constants.ERROR_MESSAGE_UNKNOWN_ERROR);
        } else if (code == Constants.ERROR_AUTH_FAILED) {
            showError(Constants.ERROR_MESSAGE_AUTH_FAILED);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = UserLoginPresenter.getInstance(getActivity());
        navigator = Navigator.getInstance();
        fragmentManager = getFragmentManager();
        userModel = UserModel.getInstance();
        imageLoader = ImageLoader.getInstance();

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
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.sign_in_facebook);
        ((MainActivity) getActivity()).registerFacebookCallback(loginButton);
    }

    @OnClick(R.id.sign_in_google)
    void signInGoogle() {
        ((MainActivity) getActivity()).signInGoogle();
    }

    @OnClick(R.id.btnLogin)
    protected void btnLoginClicked() {
        onInvalidEmail(null);
        onInvalidPassword(null);
        showLoading();
        presenter.setEmail(etLogin.getText().toString());
        presenter.setPassword(etPassword.getText().toString());
        if (presenter.isCredentialsValid()) {
            subscription = presenter.getLogInObservable().
                    subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        } else {
            onError();
        }
    }

    @OnClick(R.id.tvGoToSignUp)
    protected void tvGoToSignUpClicked() {
        navigator.navigateToSignUpFragment(fragmentManager);
    }

    @Override
    public void showLoading() {
        ((MainActivity) getActivity()).showLoading();
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideLoading();
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        ToastMaker.showMessage(getActivity(), message);
    }

    public void onInvalidEmail(String error_message) {
        tilLogin.setError(error_message);
        if (error_message == null) {
            tilLogin.setErrorEnabled(false);
        }
    }

    public void onInvalidPassword(String error_message) {
        tilPassword.setError(error_message);
        if (error_message == null) {
            tilPassword.setErrorEnabled(false);
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
        if (subscription != null) {
            subscription.unsubscribe();
        }
        hideLoading();
    }

}
