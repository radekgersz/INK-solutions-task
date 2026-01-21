package app.billing;

import lombok.ToString;


public record Plan(
        String name,
        String price,
        BillingPeriod billingPeriod
) {}