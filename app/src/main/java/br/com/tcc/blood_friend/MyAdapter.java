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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    private OnUserListener mOnUserListener;

    public MyAdapter(Context context, ArrayList<User> userArrayList, OnUserListener onUserListener) {
        this.context = context;
        this.userArrayList = userArrayList;
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

        holder.Nome.setText(userArrayList.get(position).getNome());
        holder.TipoSanguineo.setText(userArrayList.get(position).getTipoSanguineo());
        holder.Localizacao.setText(userArrayList.get(position).getLocalizacao());

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Nome, Localizacao, TipoSanguineo;
        OnUserListener onUserListener;

        public MyViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);

            Nome = itemView.findViewById(R.id.tvnome);
            Localizacao = itemView.findViewById(R.id.tvloc);
            TipoSanguineo = itemView.findViewById(R.id.tvsangue);
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
