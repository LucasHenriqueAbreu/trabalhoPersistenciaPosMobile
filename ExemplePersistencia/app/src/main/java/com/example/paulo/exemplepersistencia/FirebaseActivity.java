package com.example.paulo.exemplepersistencia;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.paulo.exemplepersistencia.Firebase.Cor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ResourceBundle;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;

import static com.example.paulo.exemplepersistencia.ControlLifeCycleApp.myRef;

public class FirebaseActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private LinearLayout container;
    private SeekBar seekRed;
    private SeekBar seekGreen;
    private SeekBar seekBlue;
    private Cor cor = new Cor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        container = findViewById(R.id.containerCL);

        seekRed = (SeekBar) findViewById(R.id.seekRed);
        seekRed.setOnSeekBarChangeListener(this);
        seekGreen = findViewById(R.id.seekGreen);
        seekGreen.setOnSeekBarChangeListener(this);
        seekBlue = findViewById(R.id.seekBlue);
        seekBlue.setOnSeekBarChangeListener(this);

        ControlLifeCycleApp.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cor cor = dataSnapshot.getValue(Cor.class);

                if(cor.red != seekRed.getProgress())
                    seekRed.setProgress(cor.red);

                if(cor.green != seekGreen.getProgress())
                    seekGreen.setProgress(cor.green);

                if(cor.blue != seekBlue.getProgress())
                    seekBlue.setProgress(cor.blue);

                container.setBackgroundColor(Color.rgb(cor.red, cor.green, cor.blue));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FIREBASE", "Failed to read value.", databaseError.toException());
            }
        });
        ;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekRed:
                cor.red = seekRed.getProgress();
                break;
            case R.id.seekGreen:
                cor.green = seekGreen.getProgress();
                break;
            case R.id.seekBlue:
                cor.blue = seekBlue.getProgress();
                break;
        }
        ControlLifeCycleApp.myRef.setValue(cor);
    }
}
