package com.example.project_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private DBLogin databaseHelper;
    private EditText editTextUsername, editTextPassword, editTextConfirmPassword;
    private CheckBox checkBoxTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnReturn = findViewById(R.id.atras);

        databaseHelper = new DBLogin(this);
        editTextUsername = findViewById(R.id.editTextRegisterUsername);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        checkBoxTerms = findViewById(R.id.checkBoxTerms);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please, fill in all the fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "The passwords do not match1452 ",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!checkBoxTerms.isChecked()) {
                    Toast.makeText(RegisterActivity.this,
                            "You must accept the terms and conditions",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.addUser(username, password)) {
                    Toast.makeText(RegisterActivity.this, "Account successfully created",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error creating the account",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}