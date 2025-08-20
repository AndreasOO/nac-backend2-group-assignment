package org.josandlin.nacbackend2groupjosandlin.dto;

public class RatingDTO {
    public double rate;
    public int count;

    public RatingDTO() {}

    public RatingDTO(double rate, int count) {
        this.rate = rate;
        this.count = count;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
