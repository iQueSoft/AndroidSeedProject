package net.iquesoft.project.seed.presentation.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.utils.Constants;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

public class UserSignUpPresenter implements Presenter {

    private static UserSignUpPresenter ourInstance;
    RegularExpressionValidation validation;
    private Context context;
    private String password;
    private String login;
    private int errorCode;
    private String name;

    private UserSignUpPresenter(Context context) {
        this.context = context;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Observable getObservable() {
        return observableSigningUp();
    }

    private Observable<Integer> observableSigningUp() {
        return Observable.defer(new Func0<Observable<Integer>>() {
            public int code;

            @Override
            public Observable<Integer> call() {
                try {
                     /*
                      *  HTTP Request imitation
                      */
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    code = signUp(name, login, password);
                } catch (InterruptedException e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just(code);
            }
        });
    }

    public int signUp(String name, String email, String password) throws InterruptedException {
        validation = new RegularExpressionValidation();
        int code = validation.isNameValid(name);
        if (code != Constants.CODE_OK) {
            errorCode = code;
            throw new InterruptedException();
        }
        code = validation.isEmailValid(email);
        if (code != Constants.CODE_OK) {
            errorCode = code;
            throw new InterruptedException();
        }
        code = validation.isPasswordValid(password);
        if (code != Constants.CODE_OK) {
            errorCode = code;
            throw new InterruptedException();
        }

        /*
         * Here should be used HTTP Request
         */

        return Constants.CODE_OK;

    }

    public ProgressDialog getProgressDialog() {
        return ProgressDialog.show(context, "Signing up", "", true, true);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
