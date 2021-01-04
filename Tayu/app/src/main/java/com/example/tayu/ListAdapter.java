package com.example.tayu;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    LayoutInflater inflater=null;
    Context mContext=null;
    ArrayList<ItemData> sample;


    public ListAdapter(Context context,ArrayList<ItemData> data){
        mContext=context;
        sample=data;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public ItemData getItem(int position) {
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

        LinearLayout cmdArea=(LinearLayout)view.findViewById(R.id.cmdArea);
        cmdArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),sample.get(position).getStrTitle(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(v.getContext(),ClubContent.class);
                intent.putExtra("title1",sample.get(position).getStrTitle());
                intent.putExtra("size1",sample.get(position).getStrSize());
                intent.putExtra("loc1",sample.get(position).getStrLoc());
                intent.putExtra("con1",sample.get(position).getStrCon());
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
