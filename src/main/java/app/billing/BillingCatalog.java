package app.billing;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class BillingCatalog {
    private final List<Plan> plans = List.of(
            new Plan("Free", "$0", BillingPeriod.MONTHLY),
            new Plan("Pro", "$20", BillingPeriod.MONTHLY),
            new Plan("Custom", "Custom", BillingPeriod.CUSTOM),
            new Plan("Enterprise", "Custom", BillingPeriod.ANNUAL)
    );
}

