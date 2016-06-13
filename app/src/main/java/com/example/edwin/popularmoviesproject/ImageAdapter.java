/*Copyright 2013 Square, Inc.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.*/

package com.example.edwin.popularmoviesproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mImageURL;
    private String[] mValues;

    public ImageAdapter(Context context, String[] mobileValues, String[] imageUrls) {
        mContext = context;
        mValues = mobileValues;
        mImageURL = imageUrls;
    }

    public int getCount() {
        return mValues.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = new View(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        gridView = inflater.inflate(R.layout.grid_item, null);

        TextView textView = (TextView) gridView
                .findViewById(R.id.grid_item_label);
        textView.setText(mValues[position]);

        ImageView imageView = (ImageView) gridView
                .findViewById(R.id.grid_item_image);
        Picasso.with(mContext).load(mImageURL[position]).into(imageView);

        return gridView;
    }
}