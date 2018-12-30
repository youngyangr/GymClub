package cn.edu.bjtu.gymclub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.bjtu.gymclub.R;
import cn.edu.bjtu.gymclub.utils.ApiUtils;
import cn.edu.bjtu.gymclub.interfaces.UserService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogupActivity extends AppCompatActivity {
    EditText edtUsername;
    EditText edtPassword1;
    EditText edtPassword2;
    Button btnLogin;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);

        edtUsername = (EditText) findViewById(R.id.et_userName);
        edtPassword1 = (EditText) findViewById(R.id.et_password1);
        edtPassword2 = (EditText) findViewById(R.id.et_password2);
        btnLogin = (Button) findViewById(R.id.btn_login);
        userService = ApiUtils.getUserService();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password1 = edtPassword1.getText().toString();
                String password2 = edtPassword2.getText().toString();
                //validate form
                if(validateLogup(username, password1,password2)){
                    //do login
                    doLogup(username, password1);
                }
            }
        });

    }

    private boolean validateLogup(String username, String password1, String password2){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password1 == null || password1.trim().length() <= 6){
            Toast.makeText(this, "Password new is incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password2 == null || password2.trim().length() <= 6){
            Toast.makeText(this, "Password confirm is incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password1.equals(password2)){
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void doLogup(final String username,final String password){
        Call<ResponseBody> call = userService.logup(username,password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(LogupActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LogupActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LogupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

