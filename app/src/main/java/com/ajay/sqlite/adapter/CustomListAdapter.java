package com.ajay.sqlite.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajay.sqlite.R;
import com.ajay.sqlite.model.StudentData;
import com.ajay.sqlite.utilities.Utility;

import java.util.List;

import com.ajay.sqlite.model.StudentData;

public class CustomListAdapter extends BaseAdapter {
    private List<StudentData> list;
    private Context context;
    public CustomListAdapter(List<StudentData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_profile_item, parent, false);
        ImageView imageView = convertView.findViewById(R.id.item_image_view);
        TextView tvName = convertView.findViewById(R.id.item_name);
        TextView tvName1 = convertView.findViewById(R.id.item_contact);
        TextView tvName2 = convertView.findViewById(R.id.item_email);
        TextView tvName3 = convertView.findViewById(R.id.item_address);



        StudentData data = list.get(position);

        Bitmap bitmap = Utility.convertBase64ToBitmap(data.getImage());
        imageView.setImageBitmap(bitmap);
        tvName.setText(data.getfName()+" "+data.getlName());
        tvName1.setText(data.getcNumber());
        tvName2.setText(data.geteMail());
        tvName3.setText(data.getAddress());

        return convertView;
    }
}
