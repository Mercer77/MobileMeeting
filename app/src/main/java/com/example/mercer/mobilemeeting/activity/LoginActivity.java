package com.example.mercer.mobilemeeting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercer.mobilemeeting.Constant;
import com.example.mercer.mobilemeeting.MainActivity;
import com.example.mercer.mobilemeeting.R;
import com.example.mercer.mobilemeeting.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.account) TextView t_account;
    @BindView(R.id.password) TextView t_password;
    @BindView(R.id.login) Button login;
    @BindView(R.id.register) Button register;

    private Map<String, Object> result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        result = new HashMap<>();
        register.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
        login.setOnClickListener(view -> login());
    }

    private void login() {
        String phone = t_account.getText().toString().trim();
        String password = t_password.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {

            Toast.makeText(LoginActivity.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String data = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(Constant.JSON, data);

        Request request = new Request.Builder()
                .url("http://" + Constant.IP_LIANG_BLUETOOTH + ":8080/MeetingSystem/user/login.do")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("okhttp", "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //注意response.body().string()只能调用一次
                    String responseData = response.body().string();
                    Log.d("okhttp", "response.code:" + response.code());
                    Log.d("okhttp", "response.message:" + response.message());
                    Log.d("okhttp", "res:" + responseData);

                    //也可以使用Handler发送消息

                    result = parseEasyJson(responseData);
                    if ((int)result.get("status")==200) {
                        SharedPreferencesUtils.setUserName("userId",result.get("userId").toString());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else{
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, result.get("msg").toString(), Toast.LENGTH_SHORT).show());
                    }


                }
                //所以这样写：


            }
        });

    }

    /**
     * 将json封装至HashMap
     * @param json
     * @return
     */
    public Map<String, Object> parseEasyJson(String json) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            result.put("status", jsonObject.getInt("status"));
            result.put("msg", jsonObject.getString("msg"));
            result.put("userId",jsonObject.getInt("userId"));
            System.out.print(result.get("status"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }
}