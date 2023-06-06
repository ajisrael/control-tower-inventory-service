package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.isNullOrBlank;

@Getter
@Builder
public class MoveInventoryItemCommand {

    @TargetAggregateIdentifier
    private String sku;
    private String productId;
    private String locationId;
    private String binId;

    public void validate() {
        if (isNullOrBlank(sku)) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }

        if (isNullOrBlank(productId)) {
            throw new IllegalArgumentException("ProductId cannot be empty");
        }

        if (isNullOrBlank(locationId)) {
            throw new IllegalArgumentException("LocationId cannot be empty");
        }

        if (isNullOrBlank(binId)) {
            throw new IllegalArgumentException("BinId cannot be empty");
        }
    }
}
