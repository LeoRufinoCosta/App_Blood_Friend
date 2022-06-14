package br.com.tcc.blood_friend.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.tcc.blood_friend.Model.User;
import br.com.tcc.blood_friend.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    ArrayList<User> userArrayList;
    ArrayList<User> userArrayListFull;
    OnUserListener mOnUserListener;
    boolean ischat;

    public MyAdapter(Context context, ArrayList<User> userArrayList, OnUserListener onUserListener, boolean ischat) {
        this.context = context;
        this.userArrayListFull = userArrayList;
        this.userArrayList = new ArrayList<>(userArrayListFull);
        this.mOnUserListener = onUserListener;
        this.ischat = ischat;
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

        TextView Nome, TipoSanguineo;
        TextView img_on, img_off;
        OnUserListener onUserListener;

        public MyViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);

            Nome = itemView.findViewById(R.id.tvnome);
            TipoSanguineo = itemView.findViewById(R.id.tvsangue);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);

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
