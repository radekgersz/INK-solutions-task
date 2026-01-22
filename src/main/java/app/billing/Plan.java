package app.billing;

public record Plan(
        String name,
        String price,
        BillingPeriod billingPeriod
) {}