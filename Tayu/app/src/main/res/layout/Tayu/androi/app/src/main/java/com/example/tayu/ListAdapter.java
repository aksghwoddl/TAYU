package com.example.tayu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tayu.domain.model.club.ClubItemData;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    LayoutInflater inflater=null;
    Context mContext=null;
    ArrayList<ClubItemData> sample;


    public ListAdapter(Context context,ArrayList<ClubItemData> data){
        mContext=context;
        sample=data;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public ClubItemData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(R.layout.clublis_item,null);

        ImageView cImage=(ImageView)view.findViewById(R.id.clubImg);
        TextView cTitle=(TextView)view.findViewById(R.id.clubtitle);
        TextView cSize=(TextView)view.findViewById(R.id.clubsize);
        TextView cLOC=(TextView)view.findViewById(R.id.clubLoc);

        cImage.setImageResource(sample.get(position).getImg());
        cTitle.setText("이름: "+sample.get(position).getStrTitle());
        cSize.setText("인원수: "+sample.get(position).getStrSize());
        cLOC.setText("활동지역: "+sample.get(position).getStrLoc());
        return view;
    }
}
