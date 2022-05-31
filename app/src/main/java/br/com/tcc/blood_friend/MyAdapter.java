package br.com.tcc.blood_friend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    ArrayList<User> userArrayList;
    ArrayList<User> userArrayListFull;
    OnUserListener mOnUserListener;

    public MyAdapter(Context context, ArrayList<User> userArrayList, OnUserListener onUserListener) {
        this.context = context;
        this.userArrayListFull = userArrayList;
        this.userArrayList = new ArrayList<>(userArrayListFull);
        this.mOnUserListener = onUserListener;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(v, mOnUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        User user = userArrayList.get(position);

        //holder.ID.setText(user.getID());
        holder.Nome.setText(user.getNome());
        holder.TipoSanguineo.setText(user.getTipoSanguineo());
        holder.Localizacao.setText(user.getLocalizacao());

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Nome, Localizacao, TipoSanguineo;
                //ID;
        OnUserListener onUserListener;

        public MyViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);

            Nome = itemView.findViewById(R.id.tvnome);
            Localizacao = itemView.findViewById(R.id.tvloc);
            TipoSanguineo = itemView.findViewById(R.id.tvsangue);
            //ID = itemView.findViewById(R.id.ID);
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
