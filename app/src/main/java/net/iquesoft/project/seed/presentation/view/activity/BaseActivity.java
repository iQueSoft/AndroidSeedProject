package net.iquesoft.project.seed.presentation.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import net.iquesoft.project.seed.presentation.AndroidApplication;
import net.iquesoft.project.seed.presentation.di.components.ApplicationComponent;
import net.iquesoft.project.seed.presentation.di.modules.ActivityModule;
import net.iquesoft.project.seed.presentation.navigation.Navigator;

import javax.inject.Inject;

/**
 * Base class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjection();
    }

    private void initializeInjection() {
        AndroidApplication.get(this).getApplicationComponent().inject(this);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment).addToBackStack(null)
                .commit();
    }

    protected void replaceFragment(FragmentManager fragmentManager, Fragment destinationFragment, int containerViewId) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

//    protected abstract void setupActivityComponent();

    protected ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
