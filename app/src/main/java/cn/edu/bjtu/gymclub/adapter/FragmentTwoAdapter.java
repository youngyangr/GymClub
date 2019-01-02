package cn.edu.bjtu.gymclub.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.bjtu.gymclub.model.Trainer;


public class FragmentTwoAdapter extends RecyclerView.Adapter<FragmentTwoAdapter.ViewHolder>{
    private static Cursor mCursor;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            holder.mText.setText(mCursor.getString(
                    mCursor.getColumnIndexOrThrow(Trainer.COLUMN_NAME)));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public static void setTrainers(Cursor cursor) {
        mCursor = cursor;
        //notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mText;

        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(
                    android.R.layout.simple_list_item_1, parent, false));
            mText = itemView.findViewById(android.R.id.text1);
        }

    }

}

