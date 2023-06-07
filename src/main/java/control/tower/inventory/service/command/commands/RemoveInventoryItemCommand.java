package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.isNullOrBlank;

@Getter
@Builder
public class RemoveInventoryItemCommand {

    @TargetAggregateIdentifier
    private String sku;

    public void validate() {
        if (isNullOrBlank(sku)) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }
    }
}
