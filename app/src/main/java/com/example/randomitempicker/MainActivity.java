package com.example.randomitempicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper db_helper;
    private Button btnAdd, btnView, randBtn;
    private EditText editTxt;
    private TextView randItemTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnView=(Button)findViewById(R.id.viewButton);
        randBtn=(Button)findViewById(R.id.randomBtn);
        randItemTV=(TextView)findViewById(R.id.randomItemTV);
        db_helper = new DataBaseHelper(this);
    }

    public void viewBtnClicked(View view){
        Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
        startActivity(intent);
    }

    public void randomBtnClicked(View view){
        try{
            String item = db_helper.getRandomItem();
            randItemTV.setText(item);
        }
        catch(Exception ex){
            toastMessage("Something went wrong!\nMake sure there are items to pick.");

        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
