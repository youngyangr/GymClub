package cn.edu.bjtu.gymclub.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.bjtu.gymclub.R;
import cn.edu.bjtu.gymclub.model.News;


public class FragmentTwoAdapter extends RecyclerView.Adapter<FragmentTwoAdapter.MyViewHolder>{
    private List<News> mList;
    public FragmentTwoAdapter(List<News> list){
        this.mList=list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(mList.get(i).getTitle());
        myViewHolder.image.setImageResource(mList.get(i).getImage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardView;
        RelativeLayout parentLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.newsImage);
            title=itemView.findViewById(R.id.newsTitle);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            cardView=itemView.findViewById(R.id.news_cardview_item);
        }
    }
}
