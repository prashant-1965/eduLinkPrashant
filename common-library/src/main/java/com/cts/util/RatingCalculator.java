package com.cts.util;

public class RatingCalculator {
    public static double calculateRating(double currentRating, double newRating, long totalRatingCount){
        double updatedRating = ((currentRating * totalRatingCount) + newRating) / (totalRatingCount + 1);
        return Math.round(updatedRating * 10.0) / 10.0;
    }
}
