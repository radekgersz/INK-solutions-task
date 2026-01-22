package app.billing;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class BillingCatalog {
    private static final List<Plan> plans = List.of(
            new Plan("Free", "$0", BillingPeriod.MONTHLY),
            new Plan("Plus", "$20", BillingPeriod.MONTHLY),
            new Plan("Pro", "100", BillingPeriod.MONTHLY),
            new Plan("Enterprise", "Custom", BillingPeriod.ANNUAL)
    );
    public static String formatBillingPeriod(BillingPeriod period) {
        return switch (period) {
            case MONTHLY -> "billed monthly";
            case ANNUAL -> "billed annually";
            case CUSTOM -> "custom billing";
        };
    }
    public static String listAvailablePlans() {
    StringBuilder sb = new StringBuilder("Here are our available plans:\n");
        for (Plan plan : plans) {
        sb.append("- ")
                .append(plan.name())
                .append(": ")
                .append(plan.price())
                .append(" (")
                .append(formatBillingPeriod(plan.billingPeriod()))
                .append(")\n");
    }
        return sb.toString();

    }
}

