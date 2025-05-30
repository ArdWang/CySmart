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

package com.cypress.cysmart.BLEProfileDataParserClasses;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.cypress.cysmart.CommonUtils.Constants;
import com.cypress.cysmart.CommonUtils.Logger;
import com.cypress.cysmart.R;

/**
 * Class used for parsing Blood pressure related information
 */
public class BloodPressureParser {

    /**
     * Get the Blood Pressure
     *
     * @param characteristic
     * @return string
     */
    public static String getSystolicBloodPressure(
            BluetoothGattCharacteristic characteristic) {
        String pressure;
        float valueSYS = characteristic.getFloatValue(
                BluetoothGattCharacteristic.FORMAT_SFLOAT, 1);
        pressure = String.format("%3.3f", valueSYS);
        Logger.i("Systolic Pressure>>>>" + valueSYS);

        return pressure;
    }

    /**
     * Returns the Systolic pressure
     * @param characteristic
     * @return
     */
    public static String getSystolicBloodPressureUnit(
            BluetoothGattCharacteristic characteristic, Context ctx) {

        String unit;
        if (BloodPressureUnitsFlagSet(characteristic.getValue()[0])) {
            unit = ctx.getResources().getString(R.string.blood_pressure_kPa);

        } else {
            unit = ctx.getResources().getString(R.string.blood_pressure_mmHg);

        }
        return unit;
    }

    /**
     * Returns the  Diastolic pressure
     * @param characteristic
     * @return
     */
    public static String getDiaStolicBloodPressure(
            BluetoothGattCharacteristic characteristic) {
        float valueDIA = characteristic.getFloatValue(
                BluetoothGattCharacteristic.FORMAT_SFLOAT, 3);
        String pressure = String.format("%3.3f", valueDIA);
        Logger.i("Diastolic Pressure>>>>" + valueDIA);
        return pressure;
    }

    public static String getDiaStolicBloodPressureUnit(
            BluetoothGattCharacteristic characteristic, Context ctx) {

        String unit;
        if (BloodPressureUnitsFlagSet(characteristic.getValue()[0])) {
            unit = ctx.getResources().getString(R.string.blood_pressure_kPa);

        } else {
            unit = ctx.getResources().getString(R.string.blood_pressure_mmHg);

        }
        return unit;
    }


    /**
     * Checking the unitsFlag of blood Pressure
     *
     * @param flags
     * @return
     */
    private static boolean BloodPressureUnitsFlagSet(byte flags) {
        if ((flags & Constants.FIRST_BITMASK) != 0){
            return true;
        }else{
            return false;
        }

    }

}
