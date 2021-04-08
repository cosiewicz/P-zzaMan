package com.cdsm.pizzaman2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPizzaActivity extends AppCompatActivity {

    private EditText et_id;
    private EditText et_name;
    private EditText et_price;
    private Button bt_exit;
    private Button bt_save;
    private Intent intent;

    /**
     * listeners
     */
    View.OnClickListener bt_saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(et_id.getText().length()>0 && et_name.getText().length()>0 && et_price.getText().length()>0){
                intent.putExtra("pizza",new Pizza(et_id.getText().toString(),et_name.getText().toString(),Integer.valueOf(et_price.getText().toString())));
                setResult(1, intent);
                finish();
            }
            else{
                Toast.makeText(getBaseContext(),"Un champ est vide",Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener bt_exitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pizza);
        intent=new Intent();
        init();
    }


    /**
     * init
     */
    public void init(){
        et_id=findViewById(R.id.editText_pizzaId);
        et_name=findViewById(R.id.editText_pizzaName);
        et_price=findViewById(R.id.editText_pizzaPrice);
        bt_save = findViewById(R.id.button_pizzaSave);
        bt_exit = findViewById(R.id.button_pizzaExit);
        bt_save.setOnClickListener(bt_saveListener);
        bt_exit.setOnClickListener(bt_exitListener);
    }






}