package edu.mssu.cis385.quoteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> quotes = new ArrayList<>();
    static ArrayAdapter<String> adapter;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_quote_menu, menu);
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu.findItem(R.id.night_mode).setTitle(R.string.light_mode);
            ListView theListView = (ListView) findViewById(R.id.quoteListView);
            theListView.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
        } else {
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_quote) {
            Intent intent = new Intent(this, QuotesActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.find_quote) {
            Intent intent = new Intent(this, GenerateQuotesActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.night_mode) {
        }
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode
                    (AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode
                    (AppCompatDelegate.MODE_NIGHT_YES);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.quoteListView);
        sharedPreferences = getApplicationContext().getSharedPreferences("myQuotes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("quotes", null);

        if (set == null) {
            quotes.add("add new quote");
        } else {
            quotes = new ArrayList<>(set);
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, quotes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, QuotesActivity.class);
                intent.putExtra("quoteId", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int itemToRemove = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .setTitle("Really?")
                        .setMessage("Are you sure you want to delete this quote?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quotes.remove(itemToRemove);
                                adapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myQuotes", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(MainActivity.quotes);
                                sharedPreferences.edit().putStringSet("quotes", set).apply();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            }
        });
    }
}