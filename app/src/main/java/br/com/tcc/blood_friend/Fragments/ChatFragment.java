package br.com.tcc.blood_friend.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.tcc.blood_friend.Adapters.UserListAdapter;
import br.com.tcc.blood_friend.Chat;
import br.com.tcc.blood_friend.Model.ChatUserModel;
import br.com.tcc.blood_friend.Model.User;
import br.com.tcc.blood_friend.Adapters.MyAdapter;
import br.com.tcc.blood_friend.R;

public class ChatFragment extends Fragment implements UserListAdapter.OnUserListener {

    private RecyclerView recyclerView;

    private UserListAdapter userListAdapter;
    ArrayList<User> userArrayList;


    FirebaseUser usuario;
    DatabaseReference reference;
    FirebaseFirestore db;

    List<ChatUserModel> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        usuario = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatUser").child(usuario.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                Log.d("testet", dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatUserModel chatUserModel = snapshot.getValue(ChatUserModel.class);
                    userList.add(chatUserModel);
                    Log.d("testet", userList.toString());

                }
                chatListUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void chatListUser() {
        userArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("Usuario").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userArrayList.clear();
                ArrayList<DocumentSnapshot> list = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    User user = d.toObject(User.class);
                    for(ChatUserModel chatUserModel : userList){
                        Log.d("testet", chatUserModel.getId());
                        if(user.getID().equals(chatUserModel.getId())){
                            userArrayList.add(user);
                        }
                    }
                }
                userListAdapter = new UserListAdapter(getContext(), userArrayList, ChatFragment.this);
                recyclerView.setAdapter(userListAdapter);
            }
        });
    }

    @Override
    public void onUserClick(int position) {
        Log.d("teste", "onUserClick: clicked");

        User x = userArrayList.get(position);

        Intent intent = new Intent(getActivity(), Chat.class);
        intent.putExtra("ID", x.getID());
        intent.putExtra("Nome", x.getNome());
        startActivity(intent);
    }

}