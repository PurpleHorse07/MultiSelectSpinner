package com.sayantan.sayantan.advancedspinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sayantan.advancedspinner.MultiSpinner;
import com.sayantan.advancedspinner.MultiSpinnerListener;
import com.sayantan.advancedspinner.SingleSpinner;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity {

    MultiSpinner mOne,mTwo;
    SingleSpinner sOne,sTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mOne=findViewById(R.id.multi_one);
        mTwo=findViewById(R.id.multi_two);
        sOne=findViewById(R.id.single_one);
        sTwo=findViewById(R.id.single_two);
        ArrayList<String> list=new ArrayList<>();
        list.add("item10");
        list.add("item20");
        mTwo.setSpinnerList(list);

        mTwo.addOnItemsSelectedListener(new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<String> choices, boolean[] selected) {
                for(int i=0;i<choices.size();i++)
                    Toast.makeText(SampleActivity.this, choices.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        mTwo.setLayout(R.layout.item);
    }
}
