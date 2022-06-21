package com.faislll.myapplication;

import android.widget.EditText;

public class Helper {
    public static boolean validateInput (EditText text){
        boolean isValid = true;

        if(text.getText().equals("")){
            isValid = false;
        }
        return isValid;
    }
}
