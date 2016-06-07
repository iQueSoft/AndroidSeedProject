package net.iquesoft.project.seed.presentation.presenter.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import net.iquesoft.project.seed.domain.BitMapLoader;
import net.iquesoft.project.seed.utils.ScreenUtils;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    ScreenUtils screenUtils;
    ArrayList<Integer> listOfImagesId;

    public GridViewAdapter(Context context, ArrayList<Integer> listOfImagesId) {
        this.context = context;
        this.listOfImagesId = listOfImagesId;
        screenUtils = new ScreenUtils(context);
    }

    @Override
    public int getCount() {
        return listOfImagesId.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfImagesId.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listOfImagesId.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ImageView imageView;
        int lWidth = screenUtils.getGridViewWidth();
        int lHeight = screenUtils.getGridViewHeight();
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(lWidth, lHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (ImageView) convertView;
        }
        final int resID = listOfImagesId.get(position);
        new BitMapLoader(context).loadBitmap(context, resID, imageView, lWidth, lHeight);
        return imageView;
    }
}
