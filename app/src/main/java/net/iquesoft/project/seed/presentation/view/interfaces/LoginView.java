package net.iquesoft.project.seed.presentation.view.interfaces;

import com.google.firebase.auth.FirebaseUser;

public interface LoginView extends LoadDataView {

    void clearTextField();

    void showEmailError(String errorMessage);

    void showPasswordError(String errorMessage);

    void updateUI(FirebaseUser firebaseUser);
}
