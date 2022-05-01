package com.example.todo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SettingsActivity extends AppCompatActivity {

    private Button deleteAccount;
    private Button next;
    private Button changePassword;
    private EditText oldPass;
    private EditText newPass;
    private EditText pnewPass;
    private AlertDialog dialogProgress;
    private TextView tvProgress;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        deleteAccount = (Button) v.findViewById(R.id.buttonUsunKonto);
        next = (Button) v.findViewById(R.id.buttonDalejZmianaHasla);
        changePassword = (Button) v.findViewById(R.id.buttonZmienHaslo);
        oldPass = (EditText) v.findViewById(R.id.StareHaslo);
        newPass = (EditText) v.findViewById(R.id.NoweHaslo);
        pnewPass = (EditText) v.findViewById(R.id.PNoweHaslo);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getBaseContext(), R.style.CustomDialog);
        LayoutInflater inflater1 = this.getLayoutInflater();
        View dialogView = inflater1.inflate(R.layout.custom_progress_dialog, null);
        tvProgress = dialogView.findViewById(R.id.NazwaProgressBar);
        dialogBuilder.setView(dialogView);
        dialogProgress = dialogBuilder.create();

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccount();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword();
            }
        });

        return v;
    }


    public void DeleteAccount() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getBaseContext(), R.style.CustomDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_delete_account, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        Button Usun = dialogView.findViewById(R.id.buttonUsunKontoNapewno);
        Button Anuluj = dialogView.findViewById(R.id.buttonAnulujUsunKonto);


        Anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        Usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvProgress.setText(" Usuwanie Konta Trwa ");
                dialogProgress.show();
                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(null);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, " Konto zostało Usunięte ", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SettingsActivity.this, RegistrationActivity.class);
                                    startActivity(intent);
                                    return;
                                } else {
                                    Exception e = task.getException();
                                    String message = e.getMessage();
                                    if (message.equals("This operation is sensitive and requires recent authentication. Log in again before retrying this request.")) {
                                        Toast.makeText(SettingsActivity.this, " Zaloguj Się Ponownie i Spróbuj jeszcze raz ", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SettingsActivity.this, " Coś Poszło nie tak, Spróbuj Ponownie Później ", Toast.LENGTH_LONG).show();
                                    }

                                }
                                dialogProgress.dismiss();
                            }
                        });
            }
        });


    }

    public void ChangePassword() {

        String OldPass = oldPass.getText().toString().trim();

        if (!OldPass.equals("")) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email, OldPass);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        oldPass.setVisibility(View.GONE);
                        next.setVisibility(ViewPager.GONE);
                        newPass.setVisibility(ViewPager.VISIBLE);
                        pnewPass.setVisibility(ViewPager.VISIBLE);
                        changePassword.setVisibility(ViewPager.VISIBLE);

                        changePassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String NewPass = newPass.getText().toString().trim();
                                String PNewPass = pnewPass.getText().toString().trim();

                                if (NewPass.length() < 6) {
                                    Toast.makeText(SettingsActivity.this, " Podaj Poprawne Hasło [ Hasło powinno zawierac wiecej niz 6 znaków ] ", Toast.LENGTH_LONG).show();
                                } else {
                                    if (NewPass.equals(PNewPass)) {
                                        user.updatePassword(NewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SettingsActivity.this, " Hasło zostało Zmodyfikowane. Zaloguj sie Ponownie ", Toast.LENGTH_LONG).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                    Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(SettingsActivity.this, " Coś poszło nie tak. Spróbuj ponownie poźniej ", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(SettingsActivity.this, " Wprowadzone Hasła nie zgadzają się ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });

                    } else {
                        Toast.makeText(SettingsActivity.this, " Uwierzytelnianie nie powiodło się, Podaj Poprawne Hasło ", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });
        } else {
            Toast.makeText(SettingsActivity.this, " Podaj Stare Hasło ", Toast.LENGTH_LONG).show();
        }


    }


   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingsActivity.this.setTitle("Ustawienia Konta");
    }

    */
}



