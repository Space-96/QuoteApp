package edu.mssu.cis385.quoteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class QuotesActivity extends AppCompatActivity {
    int quoteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        EditText editQuotes = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        quoteId = intent.getIntExtra("quoteId", -1);
        if (quoteId != -1) {

            editQuotes.setText(MainActivity.quotes.get(quoteId));
        }
        else {
            MainActivity.quotes.add("");
            MainActivity.adapter.notifyDataSetChanged();
            quoteId = MainActivity.quotes.size() -  1;
        }


        editQuotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.quotes.set(quoteId, String.valueOf(s));
                MainActivity.adapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myQuotes", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<String>(MainActivity.quotes);
                sharedPreferences.edit().putStringSet("quotes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
