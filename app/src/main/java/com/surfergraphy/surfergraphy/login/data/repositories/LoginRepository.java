package com.surfergraphy.surfergraphy.login.data.repositories;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.login.data.api.AccountUserService;
import com.surfergraphy.surfergraphy.utils.DateDeserializer;
import com.surfergraphy.surfergraphy.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;

public class LoginRepository {
    private Realm realm;

    public void loginAccount(final Realm realm, final String identity, final String password) {
        this.realm = realm;

        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        AccountUserService accountUserService = retrofit.create(AccountUserService.class);
        final Call<AccessToken> call = accountUserService.login("password", identity, password);
        new NetworkCall().execute(call);
    }

    private class NetworkCall extends AsyncTask<Call, Void, AccessToken> {
        @Override
        protected AccessToken doInBackground(Call[] params) {
            try {
                Call<AccessToken> call = params[0];
                Response<AccessToken> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AccessToken result) {
            createOrUpdateAccessToken(result);
        }
    }

    private AccessToken createOrUpdateAccessToken(final AccessToken accessToken) {
        realm.beginTransaction();
        AccessToken token = accessTokenModel(realm).createOrUpdate(accessToken);
        realm.commitTransaction();
        return token;
    }
}
