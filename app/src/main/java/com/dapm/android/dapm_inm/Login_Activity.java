package com.dapm.android.dapm_inm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Activity extends AppCompatActivity {


    TextView            mTv_Register;
    TextView            mTv_ForgotPassword;
    Button              mButton_Login;
    ImageButton         mIb_GoogleLogin;
    EditText            mEt_Email;
    EditText            mEt_Password;
    FirebaseAuth        mFirebaseAuth;
    GoogleSignInClient  mGoogleSignInClient;
    static final int    RC_SIGN_IN = 100;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Log in con Google exitoso
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWith_GoogleAccount(account);

            }catch (Exception e){
                //Fallo en el log in de Google
                Log.d("GOOGLE_SIGN_IN_TAG", "OnActivityResult: "+e.getMessage());
            }
        }
    }

    private void firebaseAuthWith_GoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Log in exitoso
                        Toast.makeText(Login_Activity.this, "Log In con Google exitoso",Toast.LENGTH_LONG).show();

                        //obtener el usuario loggeado
                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                        //verificar si es un usuario nuevo o ya existente
                        if (authResult.getAdditionalUserInfo().isNewUser()){
                            Toast.makeText(Login_Activity.this, "Cuenta creada",Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Toast.makeText(Login_Activity.this, "Usuario existente",Toast.LENGTH_SHORT).show();
                        }

                        //Ir al siguiente acitivity
                        Intent intent=new Intent(Login_Activity.this,ListInmuebles.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login_Activity.this, "Ups, algo salio mal: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializacion de elementos en el layout
        mTv_Register        = findViewById(R.id.goto_signup);
        mTv_ForgotPassword  = findViewById(R.id.tv_forgotpassword);
        mButton_Login       = findViewById(R.id.login_button);
        mIb_GoogleLogin     = findViewById(R.id.google_acc);
        mEt_Email           = findViewById(R.id.login_email);
        mEt_Password        = findViewById(R.id.login_password);

        //Inicializacion de la Autenticacion en Firebase
        mFirebaseAuth       = FirebaseAuth.getInstance();

        //Configuracion del Sign In con Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Login_Activity.this,googleSignInOptions);


        //Ir al activity de restauracion de contrase??a
        mTv_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        //Listener para el boton de Log In con cuenta de google
        mIb_GoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

        //Ir al activity de registro
        mTv_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });



        //Loggearse a la aplicacion
        mButton_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Asignacion de variables a Strings
                String user_email;
                String user_password;

                user_email      = mEt_Email.getText().toString().trim();
                user_password   = mEt_Password.getText().toString().trim();

                //Validacion de campos

                //Email vacio
                if (TextUtils.isEmpty(user_email)){
                    mEt_Email.setError("Campo no puede estar vacio");
                    return;
                }

                //Revision de email valido
                if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
                    mEt_Email.setError("Ingrese un correo valido");
                    return;
                }

                //Contrase??a vacia
                if (TextUtils.isEmpty(user_password)){
                    mEt_Password.setError("Campo no puede estar vacio");
                    return;
                }

                //Cantidad de caracteres en contrase??a
                if (user_password.length()<6){
                    mEt_Password.setError("Contrase??a debe tener mas de 6 caracteres");
                    return;
                }

                //Autenticacion de usuario
                mFirebaseAuth.signInWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login_Activity.this,"Log In exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Login_Activity.this,ListInmuebles.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login_Activity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
