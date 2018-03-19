package com.example.paulo.exemplepersistencia.Room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Paulo on 17/03/2018.
 */
@Entity
public class Feed {
    @PrimaryKey
    public int id;
    public String title;
    public String subtitle;
}
