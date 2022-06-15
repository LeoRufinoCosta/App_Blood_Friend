package br.com.tcc.blood_friend.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.tcc.blood_friend.Model.ChatModel;
import br.com.tcc.blood_friend.Model.User;
import br.com.tcc.blood_friend.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{

    Context context;
    ArrayList<User> userArrayList;
    ArrayList<User> userArrayListFull;
    OnUserListener mOnUserListener;
    boolean ischat;

    String ultimaMensagem;

    public UserListAdapter(Context context, ArrayList<User> userArrayList, OnUserListener onUserListener, boolean ischat) {
        this.context = context;
        this.userArrayListFull = userArrayList;
        this.userArrayList = new ArrayList<>(userArrayListFull);
        this.mOnUserListener = onUserListener;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_user_recents, parent, false);
        return new MyViewHolder(v, mOnUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {

        User user = userArrayList.get(position);

        holder.Nome.setText(user.getNome());

        if(ischat){
            ultimaMSG(user.getID(), holder.ultima_msg);
        } else {
            holder.ultima_msg.setVisibility(View.GONE);
        }

        if(ischat){
            if(user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Nome;
        OnUserListener onUserListener;
        TextView img_on, img_off, ultima_msg;

        public MyViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);

            Nome = itemView.findViewById(R.id.tvnome);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            ultima_msg =itemView.findViewById(R.id.ultima_msg);
            this.onUserListener = onUserListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            onUserListener.onUserClick(getAdapterPosition());
        }
    }

    public interface OnUserListener{
        void onUserClick(int position);
    }

    private void ultimaMSG(String userid, TextView ultima_MSG){
        ultimaMensagem = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if(chatModel.getReceiver().equals(firebaseUser.getUid()) && chatModel.getSender().equals(userid) ||
                            chatModel.getReceiver().equals(userid) && chatModel.getSender().equals(firebaseUser.getUid())){
                        ultimaMensagem = chatModel.getMessage();
                    }
                }

                switch (ultimaMensagem){
                    case "default":
                        ultima_MSG.setText("Sem mensagem");
                        break;

                    default:
                        ultima_MSG.setText(ultimaMensagem);
                        break;
                }

                ultimaMensagem = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

