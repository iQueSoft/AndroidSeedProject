package net.iquesoft.project.seed.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.presenter.GalleryPresenter;
import net.iquesoft.project.seed.presentation.presenter.adapters.GridViewAdapter;
import net.iquesoft.project.seed.utils.ToastMaker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridGalleryFragment extends BaseFragment {

    Context context;
    GalleryPresenter presenter;

    @BindView(R.id.gvGridGallery)
    GridView gridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        presenter = GalleryPresenter.getInstance();
    }

    @Override
    void initializeInjection() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_gallery, container, false);
        ButterKnife.bind(this, view);

        gridView.setAdapter(new GridViewAdapter(context, presenter.getListOfImagesId()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastMaker.showMessage(context, " Position " + position);
            }
        });
        return view;
    }
}
