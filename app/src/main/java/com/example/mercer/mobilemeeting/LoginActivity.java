package com.example.mercer.mobilemeeting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercer.mobilemeeting.pojo.Contact;
import com.example.mercer.mobilemeeting.widget.Friend.HanziToPinyin;
import com.google.gson.Gson;

import org.json.JSONArray;
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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.account)
    TextView t_account;
    @BindView(R.id.password)
    TextView t_password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;
    private Map<String, Object> result;
    public static final MediaType JSON = MediaType.parse("application/json");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        result = new HashMap<>();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });
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
        RequestBody requestBody = RequestBody.create(JSON, data);

        Request request = new Request.Builder()
                .url("http://" + Constant.IPMercer2 + ":8080/meeting/user/login.do")
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
//                    Message msg = new Message();
//                    msg.what = Constant.SHOW_RSPONSE;
//                    msg.obj = "response.code:"+response.code()
//                            +"response.message:"+response.message()
//                            +"res:"+responseData;

                    result = parseEasyJson(responseData);
                    if ((int)result.get("status")==200) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, result.get("msg").toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
                //所以这样写：


            }
        });

    }


    public Map<String, Object> parseEasyJson(String json) {
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            result.put("status", jsonObject.getInt("status"));
            result.put("msg", jsonObject.getString("msg"));
            System.out.print(result.get("status"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }
}