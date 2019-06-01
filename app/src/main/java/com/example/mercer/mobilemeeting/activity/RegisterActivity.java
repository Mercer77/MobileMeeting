package com.example.mercer.mobilemeeting.activity;

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
import com.example.mercer.mobilemeeting.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.phone_number)
    TextView t_phone;
    @BindView(R.id.verification_code)
    TextView t_verification_code;
    @BindView(R.id.username)
    TextView t_username;
    @BindView(R.id.pass1)
    TextView t_pass1;
    @BindView(R.id.pass2)
    TextView t_pass2;
    @BindView(R.id.button_send_verification_code)
    Button b_sendVerificationCode;
    @BindView(R.id.reg1)
    Button b_reg;
    @BindView(R.id.login1)
    TextView t_toLogin;
    @BindView(R.id.register_back)
    Button b_back;
    public static final MediaType JSON = MediaType.parse("application/json");
    private Map<String, Object> result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two2);
        ButterKnife.bind(this);
        initListener();
    }

    public void initListener() {
        b_back.setOnClickListener(this);
        b_reg.setOnClickListener(this);
        t_toLogin.setOnClickListener(this);
        b_sendVerificationCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.reg1:
                register();
                break;
            case R.id.button_send_verification_code:
                break;
            case R.id.login1:
                finish();
                break;
        }
    }

    private void register() {
        String phone = t_phone.getText().toString().trim();
        String password = t_pass1.getText().toString().trim();
        String passwordcf = t_pass2.getText().toString().trim();
        String name = t_username.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {

            Toast.makeText(RegisterActivity.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passwordcf)) {
            Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            result = new HashMap<>();
            RequestBody requestBody = new FormBody.Builder()
                    .add("phone", phone)
                    .add("password", password)
                    .add("name", name)
                    .build();
            Request request = new Request.Builder()
                    .url("http://" + Constant.IPMercer2 + ":8080/meeting/user/register.do")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("okhttp", "失败");
                    Log.d("exception",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Log.d("okhttp", "response.code:" + response.code());
                        Log.d("okhttp", "response.message:" + response.message());
                        Log.d("okhttp", "res:" + responseData);
                        result = parseEasyJson(responseData);

                        if ((int) result.get("status") == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, result.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }



                    }
                }
            });

        }
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
