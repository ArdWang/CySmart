/*
 * Copyright Cypress Semiconductor Corporation, 2014-2015 All rights reserved.
 *
 * This software, associated documentation and materials ("Software") is
 * owned by Cypress Semiconductor Corporation ("Cypress") and is
 * protected by and subject to worldwide patent protection (UnitedStates and foreign), United States copyright laws and international
 * treaty provisions. Therefore, unless otherwise specified in a separate license agreement between you and Cypress, this Software
 * must be treated like any other copyrighted material. Reproduction,
 * modification, translation, compilation, or representation of this
 * Software in any other form (e.g., paper, magnetic, optical, silicon)
 * is prohibited without Cypress's express written permission.
 *
 * Disclaimer: THIS SOFTWARE IS PROVIDED AS-IS, WITH NO WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * NONINFRINGEMENT, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE. Cypress reserves the right to make changes
 * to the Software without notice. Cypress does not assume any liability
 * arising out of the application or use of Software or any product or
 * circuit described in the Software. Cypress does not authorize its
 * products for use as critical components in any products where a
 * malfunction or failure may reasonably be expected to result in
 * significant injury or death ("High Risk Product"). By including
 * Cypress's product in a High Risk Product, the manufacturer of such
 * system or application assumes all risk of such use and in doing so
 * indemnifies Cypress against all liability.
 *
 * Use of this Software may be limited by and subject to the applicable
 * Cypress software license agreement.
 *
 *
 */
package com.cypress.cysmart.CommonUtils;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

public class DecimalTextWatcher implements TextWatcher {
    private NumberFormat nf = NumberFormat.getNumberInstance();
    private EditText et;
    private String tmp = "";
    private int moveCaretTo;
    private static final int INTEGER_CONSTRAINT = 3;
    private static final int FRACTION_CONSTRAINT = 1;
    private static final int MAX_LENGTH = INTEGER_CONSTRAINT + FRACTION_CONSTRAINT + 1;

    public DecimalTextWatcher(EditText et) {
        this.et = et;
        nf.setMaximumIntegerDigits(INTEGER_CONSTRAINT);
        nf.setMaximumFractionDigits(FRACTION_CONSTRAINT);
        nf.setGroupingUsed(false);
    }

    public int countOccurrences(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this); // remove to prevent stackoverflow
        String ss = s.toString();
        int len = ss.length();
        int dots = countOccurrences(ss, '.');
        boolean shouldParse = dots <= 1 && (dots == 0 ? len != (INTEGER_CONSTRAINT + 1) : len < (MAX_LENGTH + 1));
        if (shouldParse) {
            if (len > 1 && ss.lastIndexOf(".") != len - 1) {
                try {
                    Double d = Double.parseDouble(ss);
                    if (d != null) {
                        et.setText(nf.format(d));
                    }
                } catch (NumberFormatException e) {
                }
            }
        } else {
            et.setText(tmp);
        }
        et.addTextChangedListener(this); // reset listener

        //tried to fix caret positioning after key type:
        if (et.getText().toString().length() > 0) {
            if (dots == 0 && len >= INTEGER_CONSTRAINT && moveCaretTo > INTEGER_CONSTRAINT) {
                moveCaretTo = INTEGER_CONSTRAINT;
            } else if (dots > 0 && len >= (MAX_LENGTH) && moveCaretTo > (MAX_LENGTH)) {
                moveCaretTo = MAX_LENGTH;
            }
            try {
                et.setSelection(et.getText().toString().length());
                // et.setSelection(moveCaretTo); <- almost had it :))
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        moveCaretTo = et.getSelectionEnd();
        tmp = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = et.getText().toString().length();
        if (length > 0) {
            moveCaretTo = start + count - before;
        }
    }
}