package swe.model;

import java.time.LocalDate;

public class WorkSummary {
    private LocalDate startDate;
    private LocalDate endDate;
    private float totalHours;
    private float totalPay;

    public WorkSummary(LocalDate start, LocalDate end, float hours, float pay) {
        setStartDate(start);
        setEndDate(end);
        setTotalHours(hours);
        setTotalPay(pay);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalHours(float totalHours) {
        this.totalHours = totalHours;
    }

    public void setTotalPay(float totalPay) {
        this.totalPay = totalPay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public float getTotalHours() {
        return totalHours;
    }

    public float getTotalPay() {
        return totalPay;
    }

    @Override
    public String toString() {
        return "CurentWeekPayDetails [startDate=" + startDate + ", endDate=" + endDate + ", totalHours=" + totalHours
                + ", totalPay=" + totalPay + "]";
    }
}
