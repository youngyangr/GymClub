package cn.edu.bjtu.gymclub.fragment;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bjtu.gymclub.R;
import cn.edu.bjtu.gymclub.adapter.FragmentTwoAdapter;
import cn.edu.bjtu.gymclub.model.News;
import cn.edu.bjtu.gymclub.model.Trainer;
import cn.edu.bjtu.gymclub.provider.TrainerContentProvider;


public class FragmentTwo extends Fragment {
    private static final int LOADER_TRAINERS = 1;
    private RecyclerView rv;
    private FragmentTwoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<News> mList=new ArrayList<>();

    public FragmentTwo() {}

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initNews();
        View view=inflater.inflate(R.layout.fragment_two,container,false);
        rv = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new FragmentTwoAdapter();
        rv.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_TRAINERS, null,mLoaderCallbacks);

        return view;
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    switch (id) {
                        case LOADER_TRAINERS:
                            return new CursorLoader(getActivity().getApplicationContext(),
                                    TrainerContentProvider.URI_CHEESE,
                                    new String[]{Trainer.COLUMN_NAME},
                                    null, null, null);
                        default:
                            throw new IllegalArgumentException();
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    switch (loader.getId()) {
                        case LOADER_TRAINERS:
                            FragmentTwoAdapter.setTrainers(data);
                            mAdapter.notifyDataSetChanged();
                            break;
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    switch (loader.getId()) {
                        case LOADER_TRAINERS:
                            FragmentTwoAdapter.setTrainers(null);
                            mAdapter.notifyDataSetChanged();
                            break;
                    }
                }

            };

    private void initNews(){
        News news1=new News("健身者必须注意的五个事项",R.drawable.pic);
        mList.add(news1);
        News news2=new News("健身者该有的态度",R.drawable.pic);
        mList.add(news2);
        News news3=new News("这几样食物会给你带来翻天覆地的变化",R.drawable.pic);
        mList.add(news3);
        News news4=new News("哑铃还能这样玩？",R.drawable.pic);
        mList.add(news4);
        News news5=new News("三十天魔鬼腹肌训练",R.drawable.pic);
        mList.add(news5);
        News news6=new News("双人协作，速成魔鬼身材",R.drawable.pic);
        mList.add(news6);
        News news7=new News("心灵与身体的合一：瑜伽之王",R.drawable.pic);
        mList.add(news7);
    }
}

