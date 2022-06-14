package br.com.tcc.blood_friend.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.tcc.blood_friend.Model.User;
import br.com.tcc.blood_friend.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{

    Context context;
    ArrayList<User> userArrayList;
    ArrayList<User> userArrayListFull;
    OnUserListener mOnUserListener;
    boolean ischat;

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
        TextView img_on, img_off;

        public MyViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);

            Nome = itemView.findViewById(R.id.tvnome);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
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
}

