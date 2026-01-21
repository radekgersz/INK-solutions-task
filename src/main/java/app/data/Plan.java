package app.data;

public record Plan(
        String name,
        String price,
        BillingPeriod billingPeriod
) {}