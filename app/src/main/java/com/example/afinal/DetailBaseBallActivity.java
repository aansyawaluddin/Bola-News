package com.example.afinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailBaseBallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_base_ball);

        String headline = getIntent().getStringExtra("headline");
        String description = getIntent().getStringExtra("description");
        String published = getIntent().getStringExtra("published");
        String link = getIntent().getStringExtra("link");

        ImageView img_back = findViewById(R.id.img_back);
        ImageView imageView = findViewById(R.id.pic3);
        TextView headlineTextView = findViewById(R.id.headline3);
        TextView descriptionTextView = findViewById(R.id.desc3);
        TextView publishedTextView = findViewById(R.id.time3);
        Button btn_readmore = findViewById(R.id.btn_readmore);

        headlineTextView.setText(headline);
        descriptionTextView.setText(description);
        publishedTextView.setText(published);

        img_back.setOnClickListener(v -> {
            finish();
        });

        btn_readmore.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);
        });
    }
}