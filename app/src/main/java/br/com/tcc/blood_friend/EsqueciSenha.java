package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueciSenha extends AppCompatActivity {

    EditText enviarEmail;
    Button envio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        enviarEmail = findViewById(R.id.enviarEmail);
        envio = findViewById(R.id.envio);

        envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = enviarEmail.getText().toString();
                Log.d("emailvazio", email);
                if (email.equals("")){
                    Toast.makeText(getApplicationContext(), "Digite um e-mail válido!", Toast.LENGTH_LONG).show();
                } else {
                    abrirDialog(view);
                }
            }
        });


    }



    public void abrirDialog(View view){


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REDEFINIR SENHA");
        dialog.setMessage("Confirmar redefinição de senha?");
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_secure);

        dialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            final String email = enviarEmail.getText().toString();
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "E-mail enviado!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EsqueciSenha.this, Login.class);
                            startActivity(intent);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        dialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getApplicationContext(), "E-mail não en", Toast.LENGTH_LONG).show();
            }
        });
        dialog.create();
        dialog.show();

    }
}