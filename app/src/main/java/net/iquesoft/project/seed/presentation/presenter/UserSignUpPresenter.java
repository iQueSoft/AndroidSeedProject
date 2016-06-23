package net.iquesoft.project.seed.presentation.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.domain.exception.DefaultErrorBundle;
import net.iquesoft.project.seed.domain.exception.EmptyFieldException;
import net.iquesoft.project.seed.domain.exception.ErrorMessageFactory;
import net.iquesoft.project.seed.domain.exception.InvalidEmailException;
import net.iquesoft.project.seed.domain.exception.InvalidPasswordException;
import net.iquesoft.project.seed.domain.firebase.MyFirebaseAuth;
import net.iquesoft.project.seed.presentation.view.fragment.SignUpFragment;

public class UserSignUpPresenter implements Presenter, OnCompleteListener {

    private static UserSignUpPresenter ourInstance;
    private final MyFirebaseAuth firebaseAuth;
    RegularExpressionValidation validation;
    private Context context;
    private SignUpFragment fragmentView;

    private UserSignUpPresenter(Context context) {
        this.context = context;
        firebaseAuth = MyFirebaseAuth.getInstance();
    }

    public static UserSignUpPresenter getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserSignUpPresenter(context);
        }
        return ourInstance;
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

    public void setView(SignUpFragment fragmentView) {
        this.fragmentView = fragmentView;
    }

    public void signUpWithEmailAndPassword(String email, String password) {
        validation = new RegularExpressionValidation();
        this.fragmentView.showLoading();
        this.fragmentView.clearTextField();
        try {
            validation.isEmailValid(email);
        } catch (InvalidEmailException | EmptyFieldException e) {
            this.fragmentView.hideLoading();
            showEmailError(new DefaultErrorBundle(e));
            return;
        }
        try {
            validation.isPasswordValid(password);
        } catch (EmptyFieldException | InvalidPasswordException e) {
            this.fragmentView.hideLoading();
            showPasswordError(new DefaultErrorBundle(e));
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this);
    }

    private void showPasswordError(DefaultErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(context,
                errorBundle.getException());
        this.fragmentView.showPasswordError(errorMessage);
    }

    private void showEmailError(DefaultErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(context,
                errorBundle.getException());
        this.fragmentView.showEmailError(errorMessage);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        fragmentView.hideLoading();
        if (!task.isSuccessful()) {
            fragmentView.updateUI(null);
            try {
                throw task.getException();
            } catch (Exception e) {
                fragmentView.showError(e.getMessage());
            }
        }
    }
}
