package com.dapm.android.dapm_inm;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_Activity extends AppCompatActivity {
    EditText mEt_Name;
    EditText mEt_LastName;
    EditText mEt_Email;
    EditText mEt_Password;
    EditText mEt_Con_Password;

    Button mButton_Register;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inicializacion de Variables
        mEt_Name=findViewById(R.id.et_name);
        mEt_LastName=findViewById(R.id.et_lastname);
        mEt_Email=findViewById(R.id.et_email);
        mEt_Password=findViewById(R.id.et_password);
        mEt_Con_Password=findViewById(R.id.et_confirmpassword);

        mButton_Register=findViewById(R.id.register_button);

        mFirebaseAuth=FirebaseAuth.getInstance();

        //Confirmacion si el usuario a crear no ha sido creado previamente
        if (mFirebaseAuth.getCurrentUser() != null ){

        }

        //Accion a realizar al seleccionar el boton de registar
        mButton_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Asignación de valores de editText en Strings
                String user_name;
                String user_lastname;
                String user_email;
                String user_password;
                String con_password;

                //Obtención de datos ingresados en el los edit text
                user_name=mEt_Name.getText().toString().trim();
                user_lastname=mEt_LastName.getText().toString().trim();
                user_email=mEt_Email.getText().toString().trim();
                user_password=mEt_Password.getText().toString().trim();
                con_password=mEt_Con_Password.getText().toString().trim();

                //Validacion de datos ingresados en el formulario de registro

                //Nombre del usuario vacio
                if (TextUtils.isEmpty(user_name)){
                    mEt_Name.setError("Datos obligatorios");
                    return;
                }

                //Apellidos del usuario vacios
                if (TextUtils.isEmpty(user_lastname)){
                    mEt_LastName.setError("Datos obligatorios");
                    return;
                }

                //Email del usuario vacios
                if (TextUtils.isEmpty(user_email)){
                    mEt_Email.setError("Datos obligatorios");
                    return;
                }

                //Email del usuario no concuerda con los patrones de un correo electronico
                if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
                    mEt_Email.setError("Correo Invalido");
                    return;
                }

                //Contraseña del usuario vacia
                if (TextUtils.isEmpty(user_password)){
                    mEt_Password.setError("Datos obligatorios");
                    return;
                }

                //Cantidad de caracteres dentro de la contraseña
                if (user_password.length()<6){
                    mEt_Password.setError("Contraseña debe tener mas de 6 caracteres");
                    return;
                }

                //Confirmacion de la contraseña
                if (!con_password.equals(user_password)){
                    mEt_Con_Password.setError("Contraseñas no coinciden");
                    return;
                }

                //Creacion de usuario con firebase
                mFirebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Si la creacion de usuario se realizo exitosamente
                        if (task.isSuccessful()){
                            Toast.makeText(Register_Activity.this, "Usuario creado exitosamente", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Register_Activity.this,"Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
