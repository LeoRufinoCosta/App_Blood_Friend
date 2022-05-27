package br.com.tcc.blood_friend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity implements MyAdapter.OnUserListener{

    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;

    BottomNavigationView bt_nav;
    HomeFragment homeFragment = new HomeFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    private Spinner busca_loc, busca_sangue;
    private ImageView img_buscar;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sair:
                FirebaseAuth.getInstance().signOut();
                VerificarAuth();
                Intent intent = new Intent(Principal.this, Inicial.class);
                startActivity(intent);

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

        img_buscar = findViewById(R.id.img_buscar);
        busca_loc = findViewById(R.id.busca_loc);
        busca_sangue = findViewById(R.id.busca_sangue);

        img_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        bt_nav = findViewById(R.id.bt_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.teste, homeFragment).commit();

        bt_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        img_buscar.setVisibility(View.VISIBLE);
                        busca_loc.setVisibility(View.VISIBLE);
                        busca_sangue.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.teste,homeFragment).commit();
                        //Intent intent = new Intent(Principal.this, Principal.class);
                        //startActivity(intent);
                        return true;
                    case R.id.chat:
                        img_buscar.setVisibility(View.INVISIBLE);
                        busca_loc.setVisibility(View.INVISIBLE);
                        busca_sangue.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        //Intent intent2 = new Intent(Principal.this, Principal.class);
                        //startActivity(intent2);
                        return true;
                    case R.id.perfil:
                        img_buscar.setVisibility(View.INVISIBLE);
                        busca_loc.setVisibility(View.INVISIBLE);
                        busca_sangue.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.teste,perfilFragment).commit();
                        //Intent intent3 = new Intent(Principal.this, Perfil.class);
                        //startActivity(intent3);
                        return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<>();
        myAdapter = new MyAdapter(Principal.this, userArrayList, this);

        recyclerView.setAdapter(myAdapter);



        db = FirebaseFirestore.getInstance();
        db.collection("Receptor").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<DocumentSnapshot> list = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list)
                {
                    User obj=d.toObject(User.class);
                    userArrayList.add(obj);

                }
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
            Intent intent = new Intent(Principal.this, Inicial.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onUserClick(int position) {
        Log.d("teste", "onUserClick: clicked");
        //userArrayList.get(position);
        Intent intent = new Intent(Principal.this, Chat.class);
        intent.putExtra("some_object", "algo");
        startActivity(intent);
    }
}