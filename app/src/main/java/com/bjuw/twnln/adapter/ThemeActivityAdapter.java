package com.bjuw.twnln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjuw.twnln.R;



public class ThemeActivityAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private int[] themes_list;
    public static final int[] THEMES_LIST = {R.drawable.themes1, R.drawable.themes2,
            R.drawable.themes3, R.drawable.themes4,
            R.drawable.themes5, R.drawable.themes6,
            R.drawable.themes7, R.drawable.themes8,
            R.drawable.themes9, R.drawable.themes10,
            R.drawable.themes11, R.drawable.themes12,
            R.drawable.themes13, R.drawable.themes14,
            R.drawable.themes15, R.drawable.themes16,
            R.drawable.themes17, R.drawable.themes18,
            R.drawable.themes19, R.drawable.themes20,
            R.drawable.themes21, R.drawable.themes22};

    public ThemeActivityAdapter(Context context) {
        this.themes_list = THEMES_LIST;
        this.context = context;

    }

    @Override
    public int getCount() {
        return themes_list.length;
    }

    @Override
    public Object getItem(int position) {
        return themes_list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView imageItem;
        TextView themes_no;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_theme_item, null);
            holder.imageItem = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.themes_no = (TextView) convertView.findViewById(R.id.themes_no);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.themes_no.setText("No. " + (position + 1)+".");
        holder.imageItem.setImageResource(themes_list[position]);


        return convertView;
    }

}
