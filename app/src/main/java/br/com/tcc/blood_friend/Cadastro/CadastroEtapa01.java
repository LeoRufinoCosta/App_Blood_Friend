package br.com.tcc.blood_friend.Cadastro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.com.tcc.blood_friend.Inicial;
import br.com.tcc.blood_friend.R;

public class CadastroEtapa01 extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.voltar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_voltar:
                Intent intent = new Intent(CadastroEtapa01.this, Inicial.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_etapa01);

        Button bt_doador = findViewById(R.id.bt_doador);
        Button bt_receptor = findViewById(R.id.bt_receptor);

        bt_doador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEtapa01.this, CadastroDoador.class);
                startActivity(intent);
            }
        });

        bt_receptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEtapa01.this, CadastroReceptor.class);
                startActivity(intent);
            }
        });
    }
}