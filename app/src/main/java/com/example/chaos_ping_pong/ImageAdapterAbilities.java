package com.example.chaos_ping_pong;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterAbilities extends BaseAdapter {
    private Context context;

    public ImageAdapterAbilities(Context c) {
        context = c;
    }

    public int getCount() {
        return Crate.abilityIDs.length;
    }

    public Object getItem(int position) {
        return Crate.abilityIDs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(15, 15, 15, 15);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(Crate.abilityIDs[position]);
        return imageView;
    }
}
