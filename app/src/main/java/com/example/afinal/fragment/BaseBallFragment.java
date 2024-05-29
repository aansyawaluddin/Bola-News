package com.example.afinal.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.afinal.R;
import com.example.afinal.adapter.BaseBallAdapter;
import com.example.afinal.api.ApiConfig;
import com.example.afinal.api.ApiService;
import com.example.afinal.models.NewsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

import android.os.Handler;
import android.os.Looper;

public class BaseBallFragment extends Fragment {

    private ApiService apiService;
    private BaseBallAdapter baseBallAdapter;
    private RecyclerView recyclerView;
    private LottieAnimationView lottieAnimationView;
    private TextView eror;
    private Context context;
    private ArrayList<NewsModel> newsModels = new ArrayList<>();
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_baseball, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        eror = view.findViewById(R.id.error);
        context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        apiService = ApiConfig.getClient().create(ApiService.class);

        baseBallAdapter = new BaseBallAdapter(newsModels, context);
        recyclerView.setAdapter(baseBallAdapter);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        fetchDataFromApi();
    }

    private void fetchDataFromApi() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.cancelAnimation();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<NewsModel>> call = apiService.getNewsBaseball();
                try {
                    Response<List<NewsModel>> response = call.execute();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            lottieAnimationView.setVisibility(View.GONE);
                            lottieAnimationView.cancelAnimation();
                            if (response.isSuccessful()) {
                                List<NewsModel> data = response.body();
                                if (data != null) {
                                    newsModels.addAll(data);
                                    baseBallAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            lottieAnimationView.setVisibility(View.GONE);
                            lottieAnimationView.cancelAnimation();
                            eror.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}
