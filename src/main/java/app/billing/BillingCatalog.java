package app.billing;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class BillingCatalog {
    private final List<Plan> plans = List.of(
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

}

