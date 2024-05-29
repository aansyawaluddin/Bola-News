package com.example.afinal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.afinal.R;
import com.example.afinal.adapter.FotballAdapter;
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

public class FotballFragment extends Fragment {

    private ApiService apiService;
    private FotballAdapter fotballAdapter;
    private RecyclerView recyclerView;
    private LottieAnimationView lottieAnimationView;
    private TextView error;
    private Context context;
    private ArrayList<NewsModel> newsModels = new ArrayList<>();
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fotball, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        error = view.findViewById(R.id.error);
        context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        apiService = ApiConfig.getClient().create(ApiService.class);

        fotballAdapter = new FotballAdapter(newsModels, context);
        recyclerView.setAdapter(fotballAdapter);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        fetchDataFromApi();
    }

    private void fetchDataFromApi() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<NewsModel>> call = apiService.getNewsFootball();
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
                                    fotballAdapter.notifyDataSetChanged();
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
                            error.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}
