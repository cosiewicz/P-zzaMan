package com.cdsm.pizzaman2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ManagePizzasActivity extends AppCompatActivity {

    private ListView listPizzas;
    private Button bt_EditPizzas;
    private SqlPizza sql;
    private CheckBox cb_selectedPizza;
    private Button bt_delete;
    private Button bt_quit;
    private ArrayList<Pizza> pizzas;
    private Button bt_new;
    private Button bt_save;
    private Button bt_cancel;
    private EditText et_id;
    private EditText et_name;
    private EditText et_price;
    private static int MOD_EDIT=0;
    private static int MOD_CREATE=1;


    /**
     * Listeners
     */

    View.OnClickListener bt_quitPizzaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener bt_saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeMod(MOD_EDIT);
            if(et_id.getText().length()>0 && et_name.getText().length()>0 && et_price.getText().length()>0){
                pizzas.add(new Pizza(et_id.getText().toString(),et_name.getText().toString(),Double.valueOf(et_price.getText().toString())));
                sql.refreshData(pizzas);
                loadList();
            }
            else{
                Toast.makeText(getBaseContext(),"Un champ est vide",Toast.LENGTH_SHORT);
            }
        }
    };

    View.OnClickListener bt_cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeMod(MOD_EDIT);
        }
    };

    View.OnClickListener bt_newListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeMod(MOD_CREATE);
        }
    };

    View.OnClickListener bt_editPizzaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            editData();

        }
    };

    View.OnClickListener bt_deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pizzas);

        init();
        loadList();
    }


    /**
     * Init
     */
    public void init(){
        listPizzas = findViewById(R.id.listPizza);
        bt_EditPizzas = findViewById(R.id.button_editPizzas);
        bt_EditPizzas.setOnClickListener(bt_editPizzaListener);
        cb_selectedPizza=findViewById(R.id.checkBox_selected);
        bt_delete=findViewById(R.id.button_deletePizzas);
        bt_delete.setOnClickListener(bt_deleteListener);
        bt_quit=findViewById(R.id.button_manage_quit);
        bt_quit.setOnClickListener(bt_quitPizzaListener);

        bt_new = findViewById(R.id.button_manage_new);
        bt_cancel = findViewById(R.id.button_manage_cancel);
        bt_save = findViewById(R.id.button_manage_save);

        bt_new.setOnClickListener(bt_newListener);
        bt_cancel.setOnClickListener(bt_cancelListener);
        bt_save.setOnClickListener(bt_saveListener);

        et_id=findViewById(R.id.editText_manage_id);
        et_name=findViewById(R.id.editText_manage_name);
        et_price=findViewById(R.id.editText_manage_price);

    }

    /**
     * Load data on database and inject on list
     */
    public void loadList(){
        sql=new SqlPizza(this,"Pizza.db",null,1);
        pizzas=new ArrayList<>();
        SQLiteDatabase read = sql.getReadableDatabase();
        Cursor cursor = read.rawQuery("SELECT * FROM Pizzas",null);
        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                pizzas.add(new Pizza(id,name,price));
            }while (cursor.moveToNext());
        }

        String[] cols={"_id","name","price"};
        int[] ids={R.id.editText_managePizza_id,R.id.editText_managePizza_name,R.id.editText_managePizza_price};
        SimpleCursorAdapter adapter;
        adapter=new SimpleCursorAdapter(this,R.layout.item_pizza,cursor,cols,ids,0);listPizzas.setAdapter(adapter);
    }

    /**
     * Edit data on database
     */
    public void editData(){
        int count = listPizzas.getAdapter().getCount();
        for (int i=0;i<count;i++){
            View view=listPizzas.getChildAt(i);
            EditText id = view.findViewById(R.id.editText_managePizza_id);
            CheckBox checkBox = view.findViewById(R.id.checkBox_selected);
            EditText name = view.findViewById(R.id.editText_managePizza_name);
            EditText price = view.findViewById(R.id.editText_managePizza_price);
            for (Pizza val:pizzas) {
                if(val.getIdPizza().equals(id.getText().toString())){
                    if(checkBox.isChecked()){
                        val.setNamePizza(name.getText().toString());
                        val.setPrice(Double.valueOf(price.getText().toString()));
                    }
                }
            }
        }
        sql.refreshData(pizzas);
        loadList();
    }

    /**
     * Delete data on database
     */
    public void deleteData(){

        int count = listPizzas.getAdapter().getCount();
        ArrayList<Pizza> bufferPizzas = new ArrayList<>();
        for (int i=0;i<count;i++){
            View view=listPizzas.getChildAt(i);
            EditText id = view.findViewById(R.id.editText_managePizza_id);
            CheckBox checkBox = view.findViewById(R.id.checkBox_selected);
            for (Pizza val:pizzas) {
                if(val.getIdPizza().equals(id.getText().toString())){
                    if(!checkBox.isChecked()){
                        bufferPizzas.add(val);
                    }
                }
            }
        }
        pizzas = bufferPizzas;
        sql.refreshData(pizzas);
        loadList();
    }

    /**
     * Change mod
     * @param mod
     */
    public void changeMod(int mod){
        if(mod==MOD_CREATE){
            bt_EditPizzas.setEnabled(false);
            bt_delete.setEnabled(false);
            bt_save.setEnabled(true);
            bt_cancel.setEnabled(true);
            bt_new.setEnabled(false);
            et_id.setEnabled(true);
            et_name.setEnabled(true);
            et_price.setEnabled(true);

        }
        else if(mod== MOD_EDIT){
            bt_EditPizzas.setEnabled(true);
            bt_delete.setEnabled(true);
            bt_save.setEnabled(false);
            bt_new.setEnabled(true);
            bt_cancel.setEnabled(false);
            et_id.setEnabled(false);
            et_name.setEnabled(false);
            et_price.setEnabled(false);
        }
    }
}