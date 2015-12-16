package com.example.rank.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.rank.R;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 21/07/2015
 * Time: 09:39
 */
public class AddNewBalanceValue extends Activity implements View.OnClickListener {

    private EditText editTextTypedValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_balance_value);

        if (getIntent().getStringExtra("SelectedInput").equals("in")) {
            setTitle(getString(R.string.title_in_value));
        } else if (getIntent().getStringExtra("SelectedInput").equals("out")) {
            setTitle(getString(R.string.title_out_value));
        }

        editTextTypedValue = (EditText) findViewById(R.id.edit_new_user_balance_value);
        findViewById(R.id.button_confirm_positive).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_confirm_positive) {
            treat(editTextTypedValue.getText().toString());
        }
        finish();
    }

    private void treat(String value) {
        double treatedValue = 0.0;
        if (!value.isEmpty()) {
            treatedValue = Double.parseDouble(value);
        }
        Intent intent = new Intent(this, AddNewBalanceValue.class);
        intent.putExtra("typedValue", treatedValue);
        setResult(RESULT_OK, intent);
    }

}