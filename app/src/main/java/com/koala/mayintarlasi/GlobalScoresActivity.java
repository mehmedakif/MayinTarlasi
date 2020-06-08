package com.koala.mayintarlasi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.koala.mayintarlasi.adapters.UserAdapter;
import com.koala.mayintarlasi.models.UserModel;
import com.koala.mayintarlasi.services.APIClient;
import com.koala.mayintarlasi.services.ProjectService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalScoresActivity extends AppCompatActivity {

    ProjectService projeService = APIClient.getClient().create(ProjectService.class);
    ListView list;
    SeekBar seekbar_difficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_scores);
        Objects.requireNonNull(getSupportActionBar()).hide();
        list = findViewById(R.id.list_global_scores);
        seekbar_difficulty= findViewById(R.id.scores_seekbar_difficulty);
        KullanicilariGetir(seekbar_difficulty.getProgress());

        seekbar_difficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(GlobalScoresActivity.this, "Sunucudan sonuclar getiriliyor...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                KullanicilariGetir(seekbar_difficulty.getProgress());

            }
        });

    }

    private void KullanicilariGetir(int difficulty)
    {
        Toast.makeText(GlobalScoresActivity.this, String.valueOf(difficulty), Toast.LENGTH_LONG).show();
        Call<List<UserModel>> call = projeService.getHard();
        if(difficulty==0)
        {
            call = projeService.getEasy();
        }
        else if (difficulty==1)
        {
            call = projeService.getMedium();
        }

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> veriler = response.body();

                    UserAdapter adapter = new UserAdapter(GlobalScoresActivity.this,veriler);
                    list.setAdapter(adapter);

                } else {
                    try {
                        Toast.makeText(GlobalScoresActivity.this, "Bulunamadı.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(GlobalScoresActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(GlobalScoresActivity.this, "İnternet bağlantınızı kontrol ediniz.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
