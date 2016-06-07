package net.iquesoft.project.seed.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.presenter.GalleryPresenter;
import net.iquesoft.project.seed.utils.ToastMaker;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class ScrollingGalleryFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener {


    ArrayList<Integer> data;
    GalleryPresenter presenter;
    private FragmentActivity context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        presenter = GalleryPresenter.getInstance();
        data = presenter.getListOfImagesId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrolling_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeSlider(view);
    }

    private void initializeSlider(View view) {
        SliderLayout sliderShow = (SliderLayout) view.findViewById(R.id.slider);
        for (int i : data) {
            DefaultSliderView sliderView = new DefaultSliderView(context);
            sliderView.image(i);
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putInt("ID", i);
            sliderView.setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
            sliderView.setOnSliderClickListener(this);
            sliderShow.addSlider(sliderView);
        }
        if (sliderShow != null) {
            sliderShow.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
            sliderShow.stopAutoCycle();
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        int ID = (int) slider.getBundle().get("ID");
        ToastMaker.showMessage(context, " Image ID " + ID);
    }
}
