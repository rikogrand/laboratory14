package ru.rikogrand.lab14;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberFormatter {

    private static TextWatcher phoneNumberWatcher;

    public static void formatPhoneNumber(final EditText editText) {
        final int maxLength = "+7 (999) 999-99-99".length();
        editText.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(maxLength)});

        phoneNumberWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                formatPhoneNumberText(editText);
            }
        };

        editText.addTextChangedListener(phoneNumberWatcher);
    }

    private static void formatPhoneNumberText(EditText editText) {
        String phoneNumber = editText.getText().toString().replaceAll("[^\\d]", "");

        StringBuilder formattedNumber = new StringBuilder("+");
        int index = 1;
        for (int i = 0; i < phoneNumber.length(); i++) {
            char digit = phoneNumber.charAt(i);
            if (index == 2) {
                formattedNumber.append(" (");
            } else if (index == 5) {
                formattedNumber.append(") ");
            } else if ((index == 8 || index == 10) && i < phoneNumber.length() - 1) {
                formattedNumber.append("-");
            }
            formattedNumber.append(digit);
            index++;
        }

        if (formattedNumber.length() == "+7 (999) 999-99-".length() && phoneNumber.length() > 11) {
            formattedNumber.append(phoneNumber.charAt(phoneNumber.length() - 1));
        }

        editText.removeTextChangedListener(phoneNumberWatcher);
        editText.setText(formattedNumber.toString());
        editText.setSelection(formattedNumber.length());
        editText.addTextChangedListener(phoneNumberWatcher);
    }
}