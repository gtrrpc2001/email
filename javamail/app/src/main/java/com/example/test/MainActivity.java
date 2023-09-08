package com.example.test;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String getCode = "";
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Button sendEmailButton = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editTextText);
        Button btnCheck = (Button) findViewById(R.id.btnCheck);
        EditText code = (EditText) findViewById(R.id.editTextCheck);
        editText.requestFocus();
                sendEmailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send();
                        String getEmail = editText.getText().toString();
                        SendMail mailServer = new SendMail(getEmail);
                        String title = "msl";
                        String content = "lookheart";

                        if (getEmail.length() != 0 && getEmail.contains("@")){

                            getCode = mailServer.sendSecurityCode(getApplicationContext(),title,content);
                            code.requestFocus();
                            if (getCode.equals("")){
                                result("실패");
                            }
                        }else{
                            result("실패");
                        }

                }

            });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeStr = code.getText().toString();
                if(codeStr.length() != 0){
                    if(codeStr.equals(getCode)){
                        codeStr = "";
                        code.setText("");
                        editText.setText("");
                        result("인증성공");
                    }
                }
            }
        });
    }

    private void result(String Text){
        Toast.makeText(MainActivity.this,Text,Toast.LENGTH_LONG).show();
    }
}