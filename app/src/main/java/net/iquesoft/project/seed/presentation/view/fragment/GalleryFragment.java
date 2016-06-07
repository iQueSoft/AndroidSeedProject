package net.iquesoft.project.seed.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.navigation.Navigator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryFragment extends BaseFragment {


    @BindView(R.id.btnToGridGallery)
    Button btnToGridGallery;
    @BindView(R.id.btnToScrollingGallery)
    Button btnToScrollingGallery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = Navigator.getInstance();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.btnToGridGallery)
    void goToGridGallery() {
        navigator.navigateToGridGalleryFragment(fragmentManager);
    }

    @OnClick(R.id.btnToScrollingGallery)
    void goToScrollingGallery() {
        navigator.navigateToScrollingGalleryFragment(fragmentManager);
    }
}
