package com.conceptodigital.fuentedevida.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.conceptodigital.fuentedevida.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Url {
    public boolean isLocal  =   true;
    public String get   =   (isLocal) ? "http://192.168.0.101/api": "https://fuente-vida.conceptodigital.org/api";

    public Dialog loading(Context context) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);

        return dialog;
    }

    public void sendPostRequest(String endpoint, Map<String, String> params, final MyCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Crea el cuerpo de la solicitud con los parámetros
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = formBuilder.build();

        // Crea la solicitud POST
        Request request = new Request.Builder()
                .url(get+endpoint)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Unexpected code " + response);
                } else {
                    // Aquí es donde obtienes los datos de la respuesta
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                }
            }
        });
    }

    public void sendGetRequest(String endpoint, final MyCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Crea la solicitud POST
        Request request = new Request.Builder()
                .url(get+endpoint)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Unexpected code " + response);
                } else {
                    // Aquí es donde obtienes los datos de la respuesta
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                }
            }
        });
    }
    public String[] responseText(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return new String[]{jsonObject.getString("status"), jsonObject.getString("message")};
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public interface MyCallback {
        void onSuccess(String result);
        void onError(String error);
    }
}
