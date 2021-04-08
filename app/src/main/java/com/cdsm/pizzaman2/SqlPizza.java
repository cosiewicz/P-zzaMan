package com.cdsm.pizzaman2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqlPizza extends SQLiteOpenHelper {

    /**
     * Constructor
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public SqlPizza(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE Pizzas("+
                "_id VARCHAR,"+
                "name VARCHAR,"+
                "price FLOAT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Pizzas");
    }

    /**
     * Delete data in database en insert arraylist data
     * @param pizzas
     */
    public void refreshData(ArrayList<Pizza>pizzas){
        SQLiteDatabase write=this.getWritableDatabase();
        write.delete("Pizzas",null,null);
        String REQ = "INSERT INTO Pizzas VALUES(?,?,?)";
        for (Pizza val:pizzas) {
            Object[] obj={val.getIdPizza(),val.getNamePizza(),val.getPrice()};
            write.execSQL(REQ,obj);
        }
        write.close();
    }
}
