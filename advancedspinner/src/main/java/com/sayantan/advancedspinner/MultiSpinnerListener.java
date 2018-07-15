package com.sayantan.advancedspinner;

import java.util.List;

public interface MultiSpinnerListener {
    void onItemsSelected(List<String> choices, boolean[] selected);
}
