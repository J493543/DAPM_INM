package com.dapm.android.dapm_inm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {
    Context mContext;
    ArrayList<Inmueble> mInmuebles;
    ItemClickListener mItemClickListener;  //Se crea la variable para el ItemClickListener del RecyclerView

    //Constructor del adaptador del recyclerview
    public InmuebleAdapter(Context context, ArrayList<Inmueble> inmuebles, ItemClickListener itemClickListener){
        mContext    = context;
        mInmuebles  = inmuebles;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    //Se infla la vista de los items del recyclerview
      public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new InmuebleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,parent,false));
    }

    //Se enlazan los elementos del layour con los datos del inmueble
    @Override
    public void onBindViewHolder(InmuebleViewHolder holder, int position) {
        holder.Tipo.setText(mInmuebles.get(position).getType());
        holder.Adquisicion.setText(mInmuebles.get(position).getAdq());
        holder.Direccion.setText(mInmuebles.get(position).getAddress());
        Picasso.get().load(mInmuebles.get(position).getImg()).into(holder.Img_url);
        holder.itemView.setOnClickListener(view -> {
            mItemClickListener.onItemClickListener(mInmuebles.get(position)); //posicion del item en el recyclerview
        });
    }

    @Override
    public int getItemCount() {
        return mInmuebles.size();
    }

    //Se crea metodo ItemClickListener para poder seleccionar un item dentro del RecyclerView
    public interface ItemClickListener {
        void onItemClickListener(Inmueble inmueble);
    }

    //Creacion del ViewHolder
    class InmuebleViewHolder extends RecyclerView.ViewHolder{

        TextView  Tipo;
        TextView  Adquisicion;
        TextView  Direccion;
        ImageView Img_url;
        Button    mBttn_SeeMore;


        public InmuebleViewHolder(@NonNull View itemView){
            super (itemView);

            //Se inicializan las variables
            Tipo            = itemView.findViewById(R.id.item_TVType);
            Adquisicion     = itemView.findViewById(R.id.item_TVAdq);
            Direccion       = itemView.findViewById(R.id.item_TVAddress);
            Img_url         = itemView.findViewById(R.id.item_Img);
            mBttn_SeeMore   = itemView.findViewById(R.id.item_BttnSeeMore);

        }
    }
}
