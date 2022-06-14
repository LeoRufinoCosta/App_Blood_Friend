package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.tcc.blood_friend.Adapters.MyAdapter;
import br.com.tcc.blood_friend.Fragments.ChatFragment;
import br.com.tcc.blood_friend.Fragments.ConfigFragment;
import br.com.tcc.blood_friend.Fragments.HomeFragment;
import br.com.tcc.blood_friend.Fragments.PerfilFragment;
import br.com.tcc.blood_friend.Model.User;

public class Principal extends AppCompatActivity implements MyAdapter.OnUserListener{

    RecyclerView recyclerView;
    Spinner busca_sangueOFF;
    ArrayList<User> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    String usuarioID;
    String IDarmazenado;

    SharedPreferences save;
    SharedPreferences.Editor editor;

    BottomNavigationView bt_nav;
    HomeFragment homeFragment = new HomeFragment();
    PerfilFragment perfilFragment = new PerfilFragment();
    ChatFragment chatFragment = new ChatFragment();
    ConfigFragment configFragment = new ConfigFragment();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sair:
                //Status("offline", FirebaseAuth.getInstance().getCurrentUser().getUid());
                FirebaseAuth.getInstance().signOut();
                VerificarAuth();
                //Intent intent = new Intent(Principal.this, Inicial.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //getSupportActionBar().hide();
        VerificarAuth();

        save = getSharedPreferences("save", Context.MODE_PRIVATE);
        IDarmazenado = save.getString("valor",FirebaseAuth.getInstance().getCurrentUser().getUid());

        busca_sangueOFF = findViewById(R.id.busca_sangue);
        bt_nav = findViewById(R.id.bt_nav);

        getSupportFragmentManager().beginTransaction().replace(R.id.teste, homeFragment).commit();

        bt_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        //img_buscar.setVisibility(View.VISIBLE);
                        busca_sangueOFF.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        getSupportActionBar().show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.teste,homeFragment).commit();
                        //Intent intent = new Intent(Principal.this, Principal.class);
                        //startActivity(intent);
                        return true;
                    case R.id.chat:
                        //img_buscar.setVisibility(View.INVISIBLE);
                        busca_sangueOFF.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        getSupportActionBar().show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.teste,chatFragment).commit();
                        //Intent intent2 = new Intent(Principal.this, Principal.class);
                        //startActivity(intent2);
                        return true;
                    case R.id.perfil:
                        //img_buscar.setVisibility(View.INVISIBLE);
                        busca_sangueOFF.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        getSupportActionBar().show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.teste,perfilFragment).commit();
                        //Intent intent3 = new Intent(Principal.this, Perfil.class);
                        //startActivity(intent3);
                        return true;
                    case R.id.config:
                        busca_sangueOFF.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        getSupportActionBar().hide();
                        getSupportFragmentManager().beginTransaction().replace(R.id.teste,configFragment).commit();
                        return true;

                }
                return false;
            }
        });
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Spinner spinner_sangue = findViewById(R.id.busca_sangue);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.tipo_sanguineo, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sangue.setAdapter(adapter2);
        spinner_sangue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String semSangue = adapterView.getItemAtPosition(0).toString();
                String tipoSangue = adapterView.getSelectedItem().toString();
                recyclerView.clearOnChildAttachStateChangeListeners();
                if(tipoSangue.equals(semSangue)){
                    recyclerView.setVisibility(View.VISIBLE);
                    userArrayList = new ArrayList<>();
                    telaUser();
                }else{
                    userArrayList = new ArrayList<>();
                    filtroReceptores(tipoSangue);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }


    private void filtroReceptores(String tipoSangue){
        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference receptorUser = db.collection("Receptor");


        receptorUser.whereEqualTo("TipoSanguineo", tipoSangue).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("documento", document.getId());
                            receptorUser.orderBy("Nome").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    ArrayList<DocumentSnapshot> list = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {
                                        if (document.getId().equals(d.getId())) {
                                            User obj = d.toObject(User.class);

                                            userArrayList.add(obj);
                                        }
                                    }
                                    myAdapter = new MyAdapter(Principal.this, userArrayList, Principal.this, true);
                                    recyclerView.setAdapter(myAdapter);
                                    myAdapter.notifyDataSetChanged();
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }else{

                        recyclerView.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });
    }

    private void telaUser(){
        db.collection("Receptor").orderBy("Nome").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<DocumentSnapshot> list = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list)
                {
                    User obj=d.toObject(User.class);
                    userArrayList.add(obj);

                }
                myAdapter = new MyAdapter(Principal.this, userArrayList, Principal.this, true);
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }
        });

    }

    private void EventChangeListener() {

        db.collection("Usuarios").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.ADDED){

                        userArrayList.add(dc.getDocument().toObject(User.class));
                    }

                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void VerificarAuth(){
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(Principal.this, Inicial.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onUserClick(int position) {
        Log.d("teste", "onUserClick: clicked");

        User x = userArrayList.get(position);

        Intent intent = new Intent(Principal.this, Chat.class);
        intent.putExtra("ID", x.getID());
        intent.putExtra("Nome", x.getNome());
        startActivity(intent);
    }

    private void Status(String status, String IDusuario){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("Status", status);

        DocumentReference documentReference = db.collection("Usuario").document(IDusuario);
        documentReference.update(usuarios);

        DocumentReference documentReference2 = db.collection("Doador").document(IDusuario);
        DocumentReference documentReference3 = db.collection("Receptor").document(IDusuario);

        documentReference2.update(usuarios);
        documentReference3.update(usuarios);
    }

    @Override
    protected void onResume() {
        super.onResume();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Status("online", usuarioID);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        editor = save.edit();
        editor.putString("valor", IDarmazenado);
        editor.commit();
        Status("offline", IDarmazenado);
        Log.d("idarmazenado", IDarmazenado);
        super.onStop();
    }
}