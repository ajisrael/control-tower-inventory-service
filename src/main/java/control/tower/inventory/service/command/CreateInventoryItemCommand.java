package control.tower.inventory.service.command;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Builder
public class CreateInventoryItemCommand {

    @TargetAggregateIdentifier
    private String sku;
    private String productId;
    private String locationId;
    private String binId;
}
