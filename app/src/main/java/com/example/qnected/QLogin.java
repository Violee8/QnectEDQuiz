package com.example.qnected;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qnected.databinding.ActivityQloginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class QLogin extends AppCompatActivity {

    private ActivityQloginBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityQloginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void setupAction() {
        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLogin.this, QRegister.class);
                startActivity(intent);
            }
        });

        binding.Lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(QLogin.this, "Email Atau Password salah", Toast.LENGTH_SHORT).show();
                } else {
                    checkAkun(username, password);
                }
            }
        });
    }

    private void checkAkun(String username, String password) {
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            QuerySnapshot querySnapshot = task.getResult();
                            boolean isPasswordCorrect = false;

                            for(QueryDocumentSnapshot document : querySnapshot){
                                String storedPassword = document.getString("password");
                                if(storedPassword != null && storedPassword.equals(password)) {
                                    isPasswordCorrect = true;
                                    break;
                                }
                            }

                            if (isPasswordCorrect){
                                Toast.makeText(QLogin.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(QLogin.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(QLogin.this, "Password Salah", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(QLogin.this, "Akun Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QLogin.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                    }
                });

    }




}