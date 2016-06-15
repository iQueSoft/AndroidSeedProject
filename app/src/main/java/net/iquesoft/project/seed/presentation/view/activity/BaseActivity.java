package net.iquesoft.project.seed.presentation.view.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Base class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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


}
