package swe.controllers;

public class PayrollReport {
    private String employeeName;
    private String periodStart;
    private String periodEnd;
    private double totalHours;
    private double hourlyRate;
    private double totalPay;

    public PayrollReport(String employeeName, String periodStart, String periodEnd,
                         double totalHours, double hourlyRate, double totalPay) {
        this.employeeName = employeeName;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalHours = totalHours;
        this.hourlyRate = hourlyRate;
        this.totalPay = totalPay;
    }

    public String getEmployeeName() { return employeeName; }
    public String getPeriodStart() { return periodStart; }
    public String getPeriodEnd() { return periodEnd; }
    public double getTotalHours() { return totalHours; }
    public double getHourlyRate() { return hourlyRate; }
    public double getTotalPay() { return totalPay; }
}
