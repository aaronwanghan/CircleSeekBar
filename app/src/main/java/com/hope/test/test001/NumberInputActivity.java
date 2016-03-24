package com.hope.test.test001;

import android.app.Activity;
import android.os.Bundle;

public class NumberInputActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_input);

        NumberInput view = (NumberInput)this.findViewById(R.id.number_input_view);
        view.setLeftMaxCount(3);
        view.setRightCount(2);
    }
}
