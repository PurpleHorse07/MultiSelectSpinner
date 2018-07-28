package com.sayantan.advancedspinner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.sayantan.advancedspinner.R.*;

public class MultiSpinner extends AppCompatSpinner implements OnMultiChoiceClickListener, OnCancelListener {
    boolean[] change;
    CharSequence[] entries;
    ArrayAdapter<String> adapter;
    String spinnerText = "";
    private List<String> items = new ArrayList<>();
    private boolean[] selected;
    private String defaultText = "Nothing Choosen";
    private String spinnerTitle = "Choose From List";
    private MultiSpinnerListener listener;
    private int layout;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        layout=R.layout.item_spinner_dropdown;
        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attributeSet, styleable.CustomSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == styleable.CustomSpinner_titleText) {
                spinnerTitle = a.getString(attr);
            }
            if (attr == styleable.CustomSpinner_entries) {
                entries = a.getTextArray(attr);
                for (CharSequence entry : entries)
                    items.add((String) entry);
                initiate();
            }
            if (attr == styleable.CustomSpinner_spinnerLayout)
                layout = a.getResourceId(attr, R.layout.item_spinner_dropdown);
        }
        adapter = new ArrayAdapter<>(getContext(), layout, new String[]{defaultText});
        a.recycle();
        setAdapter(adapter);
    }

    public MultiSpinner(Context context, AttributeSet attributeSet, int arg) {
        super(context, attributeSet, arg);
    }

    void initiate() {
        selected = new boolean[items.size()];
        change = new boolean[selected.length];
        for (int j = 0; j < change.length; j++)
            change[j] = false;
    }

    @Override
    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
        selected[position] = isChecked;
        change[position] = !change[position];
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        for (int i = 1; i < change.length; i++) {
            if (change[i])
                selected[i] = !selected[i];
            change[i] = false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(spinnerTitle);
        builder.setCancelable(true);
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setItems();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    private void setItems() {
        for (int j = 0; j < change.length; j++)
            change[j] = false;
        StringBuilder spinnerBuffer = new StringBuilder();
        for (int i = 0; i < items.size(); i++)
            if (selected[i]) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            }
        spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        else
            spinnerText = defaultText;
        adapter = new ArrayAdapter<>(getContext(), layout, new String[]{spinnerText});
        setAdapter(adapter);
        if (listener != null) {
            if (selected.length > 0) {
                List<String> selectedItems = new ArrayList<>();
                for (int i = 0; i < selected.length; i++)
                    if (selected[i])
                        selectedItems.add(items.get(i));
                listener.onItemsSelected(selectedItems, selected);
            }
        }
    }

    public void addOnItemsSelectedListener(MultiSpinnerListener listener) {
        this.listener = listener;
        adapter.notifyDataSetChanged();
    }

    public String getAdapterText() {
        return spinnerText;
    }

    public ArrayList<String> getSelectedItems(){
        ArrayList<String> choice=new ArrayList<>();
        for(int i=0;i<selected.length;i++){
            if(selected[i])
                choice.add(items.get(i));
        }
        return choice;
    }

    public boolean[] getSelected() {
        return selected;
    }

    public void setSelected(boolean[] choice) {
        initiate();
        selected = choice;
        setItems();
    }

    public  void selectNone(){
        for(int i=0;i<selected.length;i++)
            selected[i]=false;
        for(int i=0;i<change.length;i++)
            change[i]=false;
    }

    public void setSpinnerList(List<String> list){
        items=new ArrayList<>();
        items.addAll(list);
        initiate();
    }

    public void setLayout(int layoutId){
        layout=layoutId;
        setItems();
    }
}
