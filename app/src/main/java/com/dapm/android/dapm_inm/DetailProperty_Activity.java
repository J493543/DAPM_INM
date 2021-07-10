package com.dapm.android.dapm_inm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailProperty_Activity extends AppCompatActivity {

    ImageView mImg;

    TextView mTV_Meters;
    TextView mTV_Type;
    TextView mTV_Rooms;
    TextView mTV_Address;
    TextView mTV_Floors;
    TextView mTV_Price;
    TextView mTV_Adq;

    String mString_ImgUrl;

    Button mBttn_Favorites;

    DatabaseReference mRootReference;
    DatabaseReference mInmueblesReference;
    DatabaseReference mFavs_Reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Obtención del ID del usuario loggeado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currenUserID = user.getUid();

        //Inicializacion de variables
        mImg        = findViewById(R.id.detail_Img);
        mTV_Meters  = findViewById(R.id.detail_TVMeters);
        mTV_Type    = findViewById(R.id.detail_TVType);
        mTV_Rooms   = findViewById(R.id.detail_TVRooms);
        mTV_Address = findViewById(R.id.detail_TVAddress);
        mTV_Floors  = findViewById(R.id.detail_TVFloors);
        mTV_Price   = findViewById(R.id.detail_TVPrice);
        mTV_Adq     = findViewById(R.id.detail_TVAdq);
        mBttn_Favorites = findViewById(R.id.detail_BttnFavorite);

        //Obtención del extra que se pasó de la lista de inmuebles a este activity.
        String keyID = getIntent().getStringExtra("selected_inmueble_key");

        //Referencia al inmuebles especifico que coincida con el id del extra del intent
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mInmueblesReference = mRootReference.child("Inmuebles").child(keyID);

        //Referencia a la lista de favoritos con el id del usuario y el id del objeto inmueble
        mFavs_Reference = mRootReference.child("Favorites").child(currenUserID).child(keyID); //Se especifica el child de la base datos con la key

        mInmueblesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Inmueble i=dataSnapshot.getValue(Inmueble.class);

                //Se obtienen los datos que correspondan al key en firebase
                mTV_Meters.setText(String.valueOf(dataSnapshot.child("Metros").getValue()));
                mTV_Type.setText(dataSnapshot.child("Tipo").getValue().toString());
                mTV_Rooms.setText(dataSnapshot.child("Nro_Habitaciones").getValue().toString());
                mTV_Address.setText(dataSnapshot.child("Direccion").getValue().toString());
                mTV_Floors.setText(String.valueOf(dataSnapshot.child("Pisos").getValue()));
                mTV_Price.setText(String.valueOf(dataSnapshot.child("Precio").getValue()));
                mTV_Adq.setText(dataSnapshot.child("Adquisicion").getValue().toString());
                mString_ImgUrl=dataSnapshot.child("Img_url").getValue().toString();
                Picasso.get().load(dataSnapshot.child("Img_url").getValue().toString()).into(mImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mBttn_Favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se almacenan los datos en las variables de los textview que se establecieron previamente
                int Metros = Integer.parseInt(mTV_Meters.getText().toString()) ;
                String Tipo = mTV_Type.getText().toString();
                int Rooms = Integer.parseInt(mTV_Rooms.getText().toString());
                String Address = mTV_Address.getText().toString();
                int Floors = Integer.parseInt(mTV_Floors.getText().toString());
                int Price = Integer.parseInt(mTV_Price.getText().toString());
                String Adq = mTV_Adq.getText().toString();
                String Img_url = mString_ImgUrl;

                //Se crea un objeto inmueble
                Inmueble inmueble = new Inmueble(Metros,Rooms, Floors,Price, Tipo,Address,Adq,Img_url);

                //Se guarda el objeto inmueble en firebase
                mFavs_Reference.setValue(inmueble).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Se almaceno correctamente
                        Toast.makeText(DetailProperty_Activity.this, "Se guardo correctamente", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
