package com.example.chaos_ping_pong;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterPanels extends BaseAdapter {
    private Context context;

    public ImageAdapterPanels(Context c) {
        context = c;
    }

    public int getCount() {
        return Crate.panelIds.length;
    }

    public Object getItem(int position) {
        return Crate.panelIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(60, 240));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(Crate.panelIds[position]);
        return imageView;
    }
}
