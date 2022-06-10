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

    public UserListAdapter(Context context, ArrayList<User> userArrayList, OnUserListener onUserListener) {
        this.context = context;
        this.userArrayListFull = userArrayList;
        this.userArrayList = new ArrayList<>(userArrayListFull);
        this.mOnUserListener = onUserListener;
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
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Nome;
        OnUserListener onUserListener;

        public MyViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);

            Nome = itemView.findViewById(R.id.tvnome);
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

