package com.example.codemaster.todolist;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {
    ArrayList<String> listOfItems = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateList();
        //call updatelist on create so that existing list items in database are loaded
        //if you want to clear it, write a function similar to clearAll listener
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateList(){
        ListView todolistview=(ListView)findViewById(R.id.listView);
        listOfItems.clear();
        SQLiteDatabase db= openOrCreateDatabase("todolist",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS ListItems(id INTEGER PRIMARY KEY AUTOINCREMENT,item VARCHAR);");
        Cursor resultSet = db.rawQuery("Select * from ListItems",null);
        //get cursor to retrieve data from table
        resultSet.moveToFirst();
        while(!resultSet.isAfterLast()){
            listOfItems.add(resultSet.getString(resultSet.getColumnIndex("item")));
            resultSet.moveToNext();
        }
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfItems);
        todolistview.setAdapter(listadapter);
        //Plugin adapter to listview to populate listview with list of strings in database
    }

    public void clearList(View v){
        ListView todolistview=(ListView)findViewById(R.id.listView);
        listOfItems.clear();
        SQLiteDatabase db= openOrCreateDatabase("todolist",MODE_PRIVATE,null);
        //open a connection to database named todolist
        db.delete("ListItems",null,null);
        //delete the rows of ListItems = equivalent to the following SQL statement
        //delete from ListItems;
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfItems);
        todolistview.setAdapter(listadapter);
        //Plugin adapter to listview to populate listview with nothing
    }

    public void myEvent(View v){

        EditText et=(EditText)findViewById(R.id.editText);
        String str=et.getText().toString();
//        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db= openOrCreateDatabase("todolist",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS ListItems(id INTEGER PRIMARY KEY AUTOINCREMENT,item VARCHAR);");
        db.execSQL("INSERT INTO ListItems (item) VALUES ('"+str+"')");
        updateList();
        //call updatelist to populate listview
        Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
    }
}
