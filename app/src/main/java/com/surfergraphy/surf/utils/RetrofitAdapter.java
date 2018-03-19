package com.surfergraphy.surf.utils;

import com.google.gson.Gson;
import com.surfergraphy.surf.login.data.LoginMember;

import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {
    public static final int CONNECT_TIMEOUT = 15;
    public static final int WRITE_TIMEOUT = 15;
    public static final int READ_TIMEOUT = 15;
    public static final String API_SERVER_URL = "http://surfergraphyapi.azurewebsites.net/";

    private static OkHttpClient client;

    public static Retrofit getInstance(String apiServerUrl, Gson gson) {

        //OkHttpClient 를 생성합니다.
        client = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS) //연결 타임아웃 시간 설정
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS) //쓰기 타임아웃 시간 설정
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS) //읽기 타임아웃 시간 설정
                .addInterceptor(chain -> {
                    // hasRegistered 정보 가져오기
                    Realm realm = Realm.getDefaultInstance();
                    LoginMember token = realm.where(LoginMember.class).findFirst();
                    String authorization = null;
//                    if (token != null)
//                        authorization = token.tokenType + " " + token.accessToken;
                    realm.close();

                    Request original = chain.request();

                    // 헤더를 자유 자재로 변경
                    Request.Builder builder = original.newBuilder();
                    if (authorization != null)
                        builder.addHeader("Authorization", authorization);

                    builder.method(original.method(),original.body());
                    Request request = builder.build();

                    okhttp3.Response response = chain.proceed(request);

                    // 아래 소스는 response 로 오는 데이터가 URLEncode 되어 있을 때
                    // URLDecode 하는 소스 입니다.
                    return response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(), URLDecoder.decode(response.body().string(), "utf-8")))
                            .build();
                }).build();

            //Retrofit 설정
            return new Retrofit.Builder()
                    .baseUrl(apiServerUrl)
                    .client(client)
                    .addConverterFactory(gson == null ? GsonConverterFactory.create() : GsonConverterFactory.create(gson)) //Json Parser 추가
                    .build(); //인터페이스 연결
    }
}