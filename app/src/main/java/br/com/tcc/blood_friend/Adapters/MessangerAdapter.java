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

import java.util.ArrayList;

import br.com.tcc.blood_friend.Model.ChatModel;
import br.com.tcc.blood_friend.R;

public class MessangerAdapter extends RecyclerView.Adapter<MessangerAdapter.MyViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    ArrayList<ChatModel> chatArrayList;
    FirebaseUser usuario;

    public MessangerAdapter(Context context, ArrayList<ChatModel> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
    }

    @NonNull
    @Override
    public MessangerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
            return new MessangerAdapter.MyViewHolder(v);
        }else{
            View v = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);
            return new MessangerAdapter.MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessangerAdapter.MyViewHolder holder, int position) {

        ChatModel chatModel = chatArrayList.get(position);

        holder.mostrar_msg.setText(chatModel.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mostrar_msg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mostrar_msg = itemView.findViewById(R.id.mostrar_msg);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        if(chatArrayList.get(position).getSender().equals(usuario.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
