package com.example.trabablho3;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabablho3.Firebase.ConfigFirebase;
import com.example.trabablho3.Room.Todo;
import com.example.trabablho3.Room.TodoDao;
import com.example.trabablho3.SQLite.TodoReaderDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRoom;
    private List<Todo> listaRoom = new ArrayList<Todo>();

    private RecyclerView recyclerViewSQLite;
    private List<com.example.trabablho3.SQLite.Todo> listaSQLite = new ArrayList<com.example.trabablho3.SQLite.Todo>();

    private EditText edtNome;
    private EditText edtDescricao;

    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.containerCL);

        final Context context = getBaseContext();

        final TextView txtAppName = (TextView)findViewById(R.id.appName);

        edtNome = (EditText)findViewById(R.id.edtNome);
        edtDescricao = (EditText)findViewById(R.id.edtDescricao);

        /* Configuração do Recycler View para o Room - Lista */
        recyclerViewRoom = (RecyclerView) findViewById(R.id.viewRoom);
        recyclerViewRoom.setHasFixedSize(true);

        LinearLayoutManager mLayoutManagerRoom = new LinearLayoutManager(this);
        recyclerViewRoom.setLayoutManager(mLayoutManagerRoom);

        MyAdapterRoom mAdapterRoom = new MyAdapterRoom(new ArrayList<Todo>());
        recyclerViewRoom.setAdapter(mAdapterRoom);

        /* Configuração do Recycler View para o SQLite - Lista */
        recyclerViewSQLite = (RecyclerView) findViewById(R.id.viewSQLite);
        recyclerViewSQLite.setHasFixedSize(true);

        LinearLayoutManager mLayoutManagerSQLite = new LinearLayoutManager(this);
        recyclerViewSQLite.setLayoutManager(mLayoutManagerSQLite);

        MyAdapterSQLite mAdapterSQLite = new MyAdapterSQLite(new ArrayList<com.example.trabablho3.SQLite.Todo>());
        recyclerViewSQLite.setAdapter(mAdapterSQLite);

        /* Listener dos Botões */
        Button btnInsertSQLite = (Button) findViewById(R.id.btnInsertSqlite);
        btnInsertSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inserir no SQLite
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        insertSQLite();
                    }
                });
                th.start();
            }
        });

        Button btnInsertRoom = (Button) findViewById(R.id.btnInsertRoom);
        btnInsertRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inserir no Room
                 Thread rn = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        insertRoom();
                    }
                });
                rn.start();
            }
        });

        Button btnAtualizarSQLite = (Button) findViewById(R.id.btnAtualizarSqlite);
        btnAtualizarSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarListaSQLite();
            }
        });

        Button btnAtualizarRoom = (Button) findViewById(R.id.btnAtualizarRoom);
        btnAtualizarRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarListaRoom();
            }
        });

        /**
         * Adiciona o listener do Firebase
         */
        ControlLifeCicleApp.myRef.addValueEventListener(new ValueEventListener() {
            /**
             * Disparado quando acontece alguma alteração na Web
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConfigFirebase config = dataSnapshot.getValue(ConfigFirebase.class);
                txtAppName.setText(config.appName);
                container.setBackgroundColor(Color.rgb(config.red, config.green, config.blue));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FIREBASE", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void insertRoom(){

        String nome = edtNome.getText().toString();
        String descricao = edtDescricao.getText().toString();

        if( (!"".equals(nome)) && (!"".equals(descricao)) ){

            Todo obj = new Todo();

            obj.nome = nome;
            obj.descricao = descricao;

            ControlLifeCicleApp.dbRoom.todoDao().create(obj);

            Log.d("Insert","Registro salvo no Room");
        } else Log.d("Insert","Informaçoes vazias");
    }

    public void insertSQLite(){

        String nome = edtNome.getText().toString();
        String descricao = edtDescricao.getText().toString();

        if( (!"".equals(nome)) && (!"".equals(descricao)) ){
            com.example.trabablho3.SQLite.Todo obj = new com.example.trabablho3.SQLite.Todo();

            obj.nome = nome;
            obj.descricao = descricao;

            ControlLifeCicleApp.dbSQLite.create(obj);

            Log.d("Insert","Registro salvo no SQLite");

        } else Log.d("Insert","Informaçoes vazias");
    }

    public void atualizarListaSQLite(){

        Thread rn = new Thread(new Runnable() {
            @Override
            public void run() {
                listaSQLite = ControlLifeCicleApp.dbSQLite.read();
            }
        });

        try{
            rn.start();
            while(rn.getState() != Thread.State.TERMINATED);
            recyclerViewSQLite.setAdapter(new MyAdapterSQLite(listaSQLite));
        } catch (Exception e) {
            Log.i("AtualizaRoom","Erro" );
        }
    }

    public void atualizarListaRoom(){
        Thread rn = new Thread(new Runnable() {
            @Override
            public void run() {
                listaRoom = ControlLifeCicleApp.dbRoom.todoDao().read();
            }
        });

        try{
            rn.start();
            while(rn.getState() != Thread.State.TERMINATED);
            recyclerViewRoom.setAdapter(new MyAdapterRoom(listaRoom));
        } catch (Exception e) {
            Log.i("AtualizaRoom","Erro" );
        }
    }
}
