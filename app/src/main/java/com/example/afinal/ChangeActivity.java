package com.example.afinal;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.afinal.sqlite.DbConfig;

public class ChangeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 100;
    private Uri selectedImageUri;
    EditText et_name, et_number, tv_address;
    ImageView iv_foto;
    Button btn_simpan;
    private DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        dbConfig = new DbConfig(this);

        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        tv_address = findViewById(R.id.tv_addres);
        btn_simpan = findViewById(R.id.btn_simpan);
        iv_foto = findViewById(R.id.iv_profile);

        // Request permission if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }

        ActivityResultLauncher<Intent> launcherIntentGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                iv_foto.setImageURI(selectedImageUri);
                            }
                        }
                    }
                }
        );

        iv_foto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent openGallery = new Intent(Intent.ACTION_PICK);
                openGallery.setType("image/*");
                launcherIntentGallery.launch(openGallery);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ_EXTERNAL_STORAGE);
            }
        });

        // Load user data from SQLite
        loadUserData();

        btn_simpan.setOnClickListener(view -> {
            String name = et_name.getText().toString();
            String number = et_number.getText().toString();
            String address = tv_address.getText().toString();

            if (!name.isEmpty() && !number.isEmpty() && !address.isEmpty()) {
                saveUserData(name, number, address);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(ChangeActivity.this, "Data successfully saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangeActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                iv_foto.performClick();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadUserData() {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = db.query(
                DbConfig.TABLE_NAME,
                new String[]{DbConfig.COLUMN_USERNAME, DbConfig.COLUMN_PHONE, DbConfig.COLUMN_ADDRESS, DbConfig.COLUMN_PHOTO_URI},
                DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_USERNAME));
            int phone = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHONE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ADDRESS));
            String photoUri = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHOTO_URI));

            et_name.setText(name);
            et_number.setText(String.valueOf(phone));
            tv_address.setText(address);

            if (photoUri != null) {
                Uri uri = Uri.parse(photoUri);
                iv_foto.setImageURI(uri);
            }
        }

        cursor.close();
        db.close();
    }

    private void saveUserData(String name, String number, String address) {
        SQLiteDatabase db = dbConfig.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_USERNAME, name);
        values.put(DbConfig.COLUMN_PHONE, Integer.parseInt(number));
        values.put(DbConfig.COLUMN_ADDRESS, address);
        if (selectedImageUri != null) {
            values.put(DbConfig.COLUMN_PHOTO_URI, selectedImageUri.toString());
        }

        db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_IS_LOGGED_IN + " = ?", new String[]{"1"});
        db.close();
    }
}
