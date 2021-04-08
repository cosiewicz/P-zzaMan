package com.cdsm.pizzaman2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<Pizza> pizzas;
    private Button bt_list;
    private Button bt_exit;
    private TextView tv_title;
    private Button bt_addPizza;
    private Button bt_managePizza;
    private SqlPizza sql;


    /**
     * Listeners
     */
    View.OnClickListener bt_listListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showLog();
        }
    };

    View.OnClickListener bt_exitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            exitApp();
        }
    };

    View.OnClickListener bt_addPizzaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addPizza();
        }
    };

    View.OnClickListener bt_managePizzaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(),ManagePizzasActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sql=new SqlPizza(this,"Pizza.db",null,1);
        sql.getReadableDatabase();
        init();
        pizzas=new ArrayList<>();
        showAlertDialog();
    }

    /**
     * Init
     */
    public void init(){
        bt_list = findViewById(R.id.button_list);
        bt_list.setOnClickListener(bt_listListener);

        bt_exit=findViewById(R.id.button_pizzaExit);
        bt_exit.setOnClickListener(bt_exitListener);

        tv_title = findViewById(R.id.textView_title);
        registerForContextMenu(tv_title);

        bt_addPizza = findViewById(R.id.button_addPizza);
        bt_addPizza.setOnClickListener(bt_addPizzaListener);

        bt_managePizza = findViewById(R.id.button_managePizza);
        bt_managePizza.setOnClickListener(bt_managePizzaListener);
    }

    /**
     * add pizzas on arraylist
     */
    public void initPizzas(){

        pizzas=new ArrayList<>();
        pizzas.add(new Pizza("AhduzyeEa871Sw","BBQ Veggie Pizzas ",12.5));
        pizzas.add(new Pizza("Didfhadz874028","Double Kiff XL Pizzas",12.5));
        pizzas.add(new Pizza("fClgozial47984","Vegan BBQ Pizzas",12.5));
        pizzas.add(new Pizza("Cbvùrfpâz4445","Bœuf Teriyaki Pizzas",12.5));
        pizzas.add(new Pizza("Clmgozaze6978","Figue - Chèvre Pizzas",12.5));
        pizzas.add(new Pizza("Clfgiaoaa9x870","Savoyarde Pizzas",12.5));
        sql.refreshData(pizzas);
    }

    /**
     * show pizzas informations on log
     */
    public void showLog(){
        for (Pizza val: pizzas) {
            Log.i("Pizza","ID : "+val.getIdPizza()+"  NAME : "+val.getNamePizza()+" -  PRICE : "+val.getPrice());
        }
        Toast.makeText(getBaseContext(),String.valueOf(pizzas.size()),Toast.LENGTH_LONG).show();
    }

    /**
     * Exit app
     */
    public void exitApp(){
        System.exit(0);
    }

    /**
     * inflate menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    /**
     * on selected item cast action
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_managePizza :
                break;
            case R.id.menu_exit :
                exitApp();
                break;
            case R.id.menu_list :
                showLog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * inflate context menu
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_color,menu);
    }

    /**
     * on selected item on context menu cast action (edit textColor)
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_red:
                tv_title.setTextColor(Color.RED);
                break;
            case R.id.menu_green:
                tv_title.setTextColor(Color.GREEN);
                break;
            case R.id.menu_blue:
                tv_title.setTextColor(Color.BLUE);
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * start new activity
     */
    public void addPizza(){

        Intent intent =new Intent(this,AddPizzaActivity.class);
        startActivityForResult(intent,1);

    }

    /**
     * get result on activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            Pizza pizza =(Pizza) data.getSerializableExtra("pizza");
            pizzas.add(pizza);
            sql.refreshData(pizzas);
        }
    }

    /**
     * show dialog with option yes : initialize database  no : don't touch database
     */
    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Chargement des données");
        builder.setMessage("Voulez vous réinitialiser la base de données et charger les pizzas de bases");

        builder.setPositiveButton("Non", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sql.refreshData(pizzas);
                initPizzas();
                sql.refreshData(pizzas);
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}