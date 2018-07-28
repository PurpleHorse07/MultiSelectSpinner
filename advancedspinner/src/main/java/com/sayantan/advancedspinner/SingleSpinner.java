package com.sayantan.advancedspinner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class SingleSpinner extends AppCompatSpinner {
    CharSequence[] entries;
    ArrayAdapter<String> adapter;
    String spinnerText = "";
    private List<String> items = new ArrayList<>();
    private int selected;
    private String defaultText = "Not Selected";
    private String spinnerTitle = "Select An Item";
    private SpinnerListener listener;
    private int layout=R.layout.item_spinner_dropdown;

    public SingleSpinner(Context context) {
        super(context);
    }

    public SingleSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomSpinner_titleText) {
                spinnerTitle = a.getString(attr);
            }
            if (attr == R.styleable.CustomSpinner_entries) {
                entries = a.getTextArray(attr);
                for (CharSequence entry : entries)
                    items.add((String) entry);
                selected = -1;
            }
            if (attr == R.styleable.CustomSpinner_spinnerLayout) {
                layout=a.getResourceId(attr,R.layout.item_spinner_dropdown);
            }
        }
        adapter = new ArrayAdapter<>(getContext(), layout, new String[]{defaultText});
        a.recycle();
        setAdapter(adapter);
    }

    public SingleSpinner(Context context, AttributeSet attributeSet, int arg) {
        super(context, attributeSet, arg);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        selected = which;
        setItems();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(spinnerTitle);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton("Clear Selection", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectNone();
            }
        });
        builder.show();
        return true;
    }

    private void setItems() {
        spinnerText = defaultText;
        if (selected >= 0)
            spinnerText = items.get(selected);
        adapter = new ArrayAdapter<>(getContext(), layout, new String[]{spinnerText});
        setAdapter(adapter);
        if (listener != null) {
            if (selected > -1)
                listener.onItemChoosen(items.get(selected), selected);
            else
                listener.onItemChoosen(null, selected);
        }
    }

    public void selectNone() {
        selected = -1;
        setItems();
    }

    public void addOnItemChoosenListener(SpinnerListener listener) {
        this.listener = listener;
        adapter.notifyDataSetChanged();
    }

    public String getSelectedItem() {
        if (selected < 0)
            return null;
        return items.get(selected);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int position) {
        selected = position;
        setItems();
    }

    public void setSpinnerTitle(String title){
        spinnerTitle=title;
    }
    public void setSpinnerList(List<String> list) {
        items = new ArrayList<>();
        items.addAll(list);
        selected = -1;
    }
}
