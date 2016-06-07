package net.iquesoft.project.seed.presentation.view.fragment;

import android.support.v4.app.Fragment;

import net.iquesoft.project.seed.presentation.navigation.Navigator;
import net.iquesoft.project.seed.utils.ToastMaker;

public abstract class BaseFragment extends Fragment {

    Navigator navigator;
    android.support.v4.app.FragmentManager fragmentManager;

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToast(String message) {
        ToastMaker.showMessage(getActivity(), message);
    }


/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */

}
