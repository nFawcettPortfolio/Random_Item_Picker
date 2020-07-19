package com.example.randomitempicker;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {
    DataBaseHelper db_helper;
    private ListView lv;
    private Button addItemBtn;

    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        db_helper = new DataBaseHelper(this);
        setContentView(R.layout.list_layout);

        View.OnClickListener onButtonClick = new View.OnClickListener(){

            @Override
            public void onClick(View v){
                addItemBtnClicked();
            }

        };
        lv = (ListView)findViewById(R.id.listView);
        addItemBtn=(Button)findViewById(R.id.addItemBtn);
        addItemBtn.setOnClickListener(onButtonClick);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = (String) parent.getItemAtPosition(position);
                final AlertDialog.Builder selectItemDialog= new AlertDialog.Builder(context);
                selectItemDialog.setTitle(selectedItem);
                final EditText et = new EditText(context);
                et.setText(selectedItem);
                selectItemDialog.setView(et);
                selectItemDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!et.getText().toString().equals(selectedItem))
                        {
                            db_helper.ChangeItem(et.getText().toString(),selectedItem);
                            populateListView();
                        }
                    }
                });

                selectItemDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_helper.DeleteItem(selectedItem);
                        populateListView();
                    }
                });

                selectItemDialog.show();
            }
        });

        populateListView();
    }

    private void addItemBtnClicked() {
        final Context context=this;
        final AlertDialog.Builder addItemDialog= new AlertDialog.Builder(context);
        addItemDialog.setTitle("Add New Item:");
        final EditText et = new EditText(context);
        addItemDialog.setView(et);
        addItemDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (et.getText().length()!=0)
                {
                    AddData(et.getText().toString());
                    populateListView();
                }else {
                    toastMessage("Please enter something in the text field.");
                }
            }
        });

        addItemDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        addItemDialog.show();
    }

    private void populateListView() {

        Cursor data = db_helper.getData();
        ArrayList<String>listData=new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        lv.setAdapter(adapter);

    }

    public void AddData(String newEntry){
        boolean insertData = db_helper.addData(newEntry);
        if (insertData) {
            toastMessage("Item Successfully Added!");
        }else{
            toastMessage("Something went wrong");
        }
    }


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
