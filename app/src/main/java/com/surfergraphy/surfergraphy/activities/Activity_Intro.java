package com.surfergraphy.surfergraphy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.surfergraphy.surfergraphy.BaseLifecycleActivity;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.login.Activity_Login;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Intro extends BaseLifecycleActivity {

    @BindView(R.id.textView1)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        Intent intent = new Intent(Activity_Intro.this, Activity_Login.class);
        startActivity(intent);
        finish();
/*
        textView.setOnClickListener(v -> {
            Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://surfergraphyapi.azurewebsites.net/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            AccountUserService accountUserService = retrofit.create(AccountUserService.class);
            final Call<AccessToken> call = accountUserService.login("password", "test@nate.com", "Dltktwla1@");
            new NetworkCall().execute(call);
        });
*/
    }

}
