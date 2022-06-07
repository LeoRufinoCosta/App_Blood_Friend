package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.tcc.blood_friend.Adapters.MessangerAdapter;
import br.com.tcc.blood_friend.Model.ChatModel;

public class Chat extends AppCompatActivity {

    TextView username;
    String nome, Id;
    FirebaseUser usuario;
    FirebaseFirestore db;
    Button buttonChat;
    EditText editTextChat;
    DatabaseReference reference;

    MessangerAdapter messangerAdapter;
    ArrayList<ChatModel> chatArrayList;
    RecyclerView recyclerView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.voltar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_voltar:
                Intent intent = new Intent(Chat.this, Principal.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = findViewById(R.id.nomeChat);
        buttonChat = findViewById(R.id.buttonChat);
        editTextChat = findViewById(R.id.editTextChat);
        usuario = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recycler_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        nome = intent.getStringExtra("Nome");
        Id = intent.getStringExtra("ID");
        //Log.d("TESTEID", nome+Id);

        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensagem = editTextChat.getText().toString();
                if(!mensagem.equals("")){
                    MandarMsg(usuario.getUid(), Id, mensagem);
                }else{
                    Toast.makeText(Chat.this,"Não pode enviar mensagem vazia", Toast.LENGTH_SHORT).show();
                }
                editTextChat.setText("");
            }
        });

        db = FirebaseFirestore.getInstance();
        username.setText(nome);
        //Log.d("TESTEID", reference.toString());

        LerMsg(usuario.getUid(), Id);
    }

    private void MandarMsg(String sender, String receiver, String msg){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", msg);

        reference.child("Chat").push().setValue(hashMap);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatUser").child(usuario.getUid()).child(Id);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(Id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LerMsg(String myid, String userid){
        chatArrayList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(myid) &&  chatModel.getSender().equals(userid) ||
                    chatModel.getReceiver().equals(userid) && chatModel.getSender().equals(myid)){
                        chatArrayList.add(chatModel);
                    }

                    messangerAdapter = new MessangerAdapter(Chat.this, chatArrayList);
                    recyclerView.setAdapter(messangerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}