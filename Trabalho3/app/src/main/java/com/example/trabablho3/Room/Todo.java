package com.example.trabablho3.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Paulo on 17/03/2018.
 */
@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nome;
    public String descricao;
}
