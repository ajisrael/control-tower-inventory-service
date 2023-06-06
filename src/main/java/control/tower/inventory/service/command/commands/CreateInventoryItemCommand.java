package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.isNullOrBlank;

@Getter
@Builder
public class CreateInventoryItemCommand {

    @TargetAggregateIdentifier
    private String sku;
    private String productId;
    private String locationId;
    private String binId;

}
