package net.iquesoft.project.seed.presentation.presenter;

import net.iquesoft.project.seed.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class GalleryPresenter implements Presenter {


    private static GalleryPresenter ourInstance;
    private ArrayList<Integer> listOfImagesId;

    private GalleryPresenter() {
    }

    public static GalleryPresenter getInstance() {
        if (ourInstance == null) {
            ourInstance = new GalleryPresenter();
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

    public ArrayList<Integer> getListOfImagesId() {
        int length = Constants.mThumbIds.length;
        listOfImagesId = new ArrayList<>(length);
        listOfImagesId.addAll(Arrays.asList(Constants.mThumbIds).subList(0, length));
        return listOfImagesId;
    }

}
