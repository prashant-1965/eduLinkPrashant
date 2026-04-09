package com.cts.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AttendanceCalculator {
    public static double calculateAttendance(Long totalAttendedDays,Long totalDaysToBeAttend){
        if (totalDaysToBeAttend <= 0) {
            return 0.0;
        }
        double rawPercentage = ((double) totalAttendedDays / totalDaysToBeAttend) * 100;
        double roundedPercentage = new BigDecimal(rawPercentage).setScale(2, RoundingMode.HALF_UP).doubleValue();

        if (roundedPercentage > 100.0) {
            roundedPercentage = 100.00;
        }
        return roundedPercentage;
    }
}
