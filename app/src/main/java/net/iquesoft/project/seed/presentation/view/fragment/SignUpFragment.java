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

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.navigation.Navigator;
import net.iquesoft.project.seed.presentation.presenter.UserSignUpPresenter;
import net.iquesoft.project.seed.utils.Constants;
import net.iquesoft.project.seed.utils.ToastMaker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class SignUpFragment extends BaseFragment implements LoadDataView, View.OnFocusChangeListener {

    ProgressDialog progressDialog;
    UserSignUpPresenter presenter;
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
    private Subscription subscription;
    private Action1<Boolean> action1 = new Action1<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
        }
    };

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
        presenter = UserSignUpPresenter.getInstance(getActivity());
        navigator = Navigator.getInstance();
        fragmentManager = getFragmentManager();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnSignUp)
    protected void signUp() {
        onInvalidEmail(null);
        onInvalidPassword(null);
        showLoading();
        presenter.setEmail(etEmail.getText().toString());
        presenter.setPassword(etRegistrationPassword.getText().toString());
        if (presenter.isCredentialsValid()) {
            subscription = presenter.getSignUpObservable().
                    subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(action1);
        } else {
            onError();
        }
    }

    @OnClick(R.id.tvGoToLogIn)
    protected void goToLogIn() {
        navigator.navigateToLogInFragment(fragmentManager);
    }


    @Override
    public void showLoading() {
        progressDialog = presenter.getProgressDialog();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
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
        tilEmail.setError(error_message);
        if (error_message == null) {
            tilEmail.setErrorEnabled(false);
        }
    }


    public void onInvalidPassword(String error_message) {
        tilRegistrationPassword.setError(error_message);
        if (error_message == null) {
            tilRegistrationPassword.setErrorEnabled(false);
        }
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
    public void onPause() {
        super.onPause();
        if (subscription != null) {
            subscription.unsubscribe();
        }
        hideLoading();
    }

}
