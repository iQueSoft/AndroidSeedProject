package net.iquesoft.project.seed.presentation.navigation;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.view.fragment.GalleryFragment;
import net.iquesoft.project.seed.presentation.view.fragment.GridGalleryFragment;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.presentation.view.fragment.MainFragment;
import net.iquesoft.project.seed.presentation.view.fragment.ScrollingGalleryFragment;
import net.iquesoft.project.seed.presentation.view.fragment.SignUpFragment;

/**
 * Class used to navigate through the application.
 */

public class Navigator {
    private static Navigator ourInstance;
    private String galleryStack = "galleryStack";
    private String mainStack = "mainStack";

    private Navigator() {
    }

    public static Navigator getInstance() {
        if (ourInstance == null) {
            ourInstance = new Navigator();
        }
        return ourInstance;
    }

    public void navigateToSignUpFragment(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new SignUpFragment())
                .commit();
    }

    public void navigateToMainFragment(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new MainFragment())
                .addToBackStack(mainStack)
                .commit();
    }

    public void navigateToLogInFragment(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit();
    }

    public void navigateToGalleryFragment(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new GalleryFragment())
                .addToBackStack(galleryStack)
                .commit();
    }

    public void navigateToGridGalleryFragment(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new GridGalleryFragment())
                .commit();
    }

    public void navigateToScrollingGalleryFragment(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new ScrollingGalleryFragment())
                .commit();
    }
}
