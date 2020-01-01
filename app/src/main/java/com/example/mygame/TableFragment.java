package com.example.mygame;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mygame.Winner.winners;

public class TableFragment extends Fragment {
    private View rootView;
    private FragmentHighScoreListener listener;

    public interface FragmentHighScoreListener {
        void onGameUserInfoSent(Winner user);
    }

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.table_new, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loadData();
            for (int i = 0; i < winners.size(); i++) {
                Collections.sort(winners, new Comparator<Winner>() {
                    @Override
                    public int compare(Winner o1, Winner o2) {
                        return o2.getScore() - o1.getScore();
                    }
                });
            }

            if(winners.size() <= 10 ) {
                adapter = new RecyclerAdapter(winners);
            }
            if(winners.size() >=10) {
                adapter = new RecyclerAdapter(new ArrayList<Winner>(winners.subList(0, 10)));
            }
        // adapter = new RecyclerAdapter(new ArrayList<Winner>(winners.subList(0,10)));
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Winner gameuser = winners.get(position);
                listener.onGameUserInfoSent(gameuser);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentHighScoreListener) {
            listener = (FragmentHighScoreListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implements FragmentHighScoreListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null; // now cleaning up!
    }


    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Winner>>() {
        }.getType();
        Winner.winners = gson.fromJson(json, type);
        if (Winner.winners == null)
            return;
    }
}