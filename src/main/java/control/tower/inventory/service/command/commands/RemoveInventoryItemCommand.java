package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Builder
public class RemoveInventoryItemCommand {

    @TargetAggregateIdentifier
    private String sku;
}
