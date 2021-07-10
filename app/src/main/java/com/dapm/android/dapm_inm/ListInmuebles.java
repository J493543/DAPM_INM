package com.dapm.android.dapm_inm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListInmuebles extends AppCompatActivity {
    RecyclerView      mRecyclerView;
    InmuebleAdapter   mInmuebleAdapter;
    DatabaseReference mDatabaseReference;
    ArrayList<Inmueble> mInmuebleArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_inmuebles);

        //Inicializacion RecyclerView
        mRecyclerView       = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListInmuebles.this));

        //Inicializacion referencia a Firebase
        mDatabaseReference  = FirebaseDatabase.getInstance().getReference();

        //Inicializacion de Arraylist
        mInmuebleArrayList  = new ArrayList<Inmueble>();

        //Metodo para obtener datos de Firebase
        GetDataFromFirebase();

    }

    private void GetDataFromFirebase() {

        Query query = mDatabaseReference.child("Inmuebles");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Inmueble inmuebles = new Inmueble();
                    inmuebles.setKey(snapshot.getKey());
                    inmuebles.setAdq(snapshot.child("Adquisicion").getValue().toString());
                    inmuebles.setType(snapshot.child("Tipo").getValue().toString());
                    inmuebles.setAddress(snapshot.child("Direccion").getValue().toString());
                    inmuebles.setImg(snapshot.child("Img_url").getValue().toString());

                    mInmuebleArrayList.add(inmuebles);
                }

                mInmuebleAdapter = new InmuebleAdapter(ListInmuebles.this, mInmuebleArrayList, new InmuebleAdapter.ItemClickListener() {

                    //Se implementa el metodo del Adapter para poder seleccionar el inmueble del que se quiera saber los detalles
                    @Override
                    public void onItemClickListener(Inmueble inmueble) {
                        //El toast es solo para comprobar su funcionamiento
                        Toast.makeText(ListInmuebles.this, "El key item presionado es:"+inmueble.getKey(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ListInmuebles.this, DetailProperty_Activity.class);
                        intent.putExtra("selected_inmueble_key", inmueble.getKey());
                        startActivity(intent);

                    }
                });
                mRecyclerView.setAdapter(mInmuebleAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
