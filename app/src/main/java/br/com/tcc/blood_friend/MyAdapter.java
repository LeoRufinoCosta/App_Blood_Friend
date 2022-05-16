package br.com.tcc.blood_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;

    public MyAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);


        return new MyViewHolder(v);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Nome, Localizacao, TipoSanguineo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Nome = itemView.findViewById(R.id.tvnome);
            Localizacao = itemView.findViewById(R.id.tvloc);
            TipoSanguineo = itemView.findViewById(R.id.tvsangue);
        }
    }
}
