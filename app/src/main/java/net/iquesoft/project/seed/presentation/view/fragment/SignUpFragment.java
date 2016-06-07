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
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignUpFragment extends BaseFragment implements LoadDataView, View.OnFocusChangeListener {

    ProgressDialog progressDialog;
    UserSignUpPresenter presenter;
    @BindView(R.id.btnSignUp)
    Button btnLogin;
    @BindView(R.id.tvGoToLogIn)
    TextView tvGoToSignUp;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilRegistrationPassword)
    TextInputLayout tilRegistrationPassword;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.etRegistrationPassword)
    TextInputEditText etRegistrationPassword;
    private Subscription subscription;
    private Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onCompleted() {
            hideLoading();
            showToast("Signed up");
            navigator.navigateToMainFragment(fragmentManager);
        }

        @Override
        public void onError(Throwable e) {
            hideLoading();

            int code = presenter.getErrorCode();
            if (code == Constants.ERROR_EMPTY_NAME) {
                onInvalidName(Constants.ERROR_MESSAGE_EMPTY_FIELD);
            } else if (code == Constants.ERROR_EMPTY_EMAIL) {
                onInvalidEmail(Constants.ERROR_MESSAGE_EMPTY_FIELD);
            } else if (code == Constants.ERROR_EMPTY_PASSWORD) {
                onInvalidPassword(Constants.ERROR_MESSAGE_EMPTY_FIELD);
            } else if (code == Constants.ERROR_INVALID_NAME) {
                onInvalidName(Constants.ERROR_MESSAGE_INVALID_NAME);
            } else if (code == Constants.ERROR_INVALID_EMAIL) {
                onInvalidEmail(Constants.ERROR_MESSAGE_INVALID_EMAIL);
            } else if (code == Constants.ERROR_INVALID_PASSWORD) {
                onInvalidPassword(Constants.ERROR_MESSAGE_INVALID_PASSWORD);
            } else if (code == Constants.ERROR_CONNECTION_LOST) {
                showError(Constants.ERROR_MESSAGE_CONNECTION_LOST);
            } else if (code == Constants.ERROR_UNKNOWN) {
                showError(Constants.ERROR_MESSAGE_UNKNOWN_ERROR);
            }
        }

        @Override
        public void onNext(Integer code) {
        }
    };

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
        attemptToSignUp();
    }

    @OnClick(R.id.tvGoToLogIn)
    protected void goToLogIn() {
        navigator.navigateToLogInFragment(fragmentManager);
    }

    private void attemptToSignUp() {
        onInvalidEmail(null);
        onInvalidPassword(null);
        showLoading();
        presenter.setName(etName.getText().toString());
        presenter.setEmail(etEmail.getText().toString());
        presenter.setPassword(etRegistrationPassword.getText().toString());
        subscription = presenter.getObservable().
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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

    public void onInvalidName(String error_message) {
        tilName.setError(error_message);
        if (error_message == null) {
            tilName.setErrorEnabled(false);
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
                case R.id.etName:
                    tilName.setError(null);
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
