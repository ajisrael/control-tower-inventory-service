package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;

@Getter
@Builder
public class RemoveInventoryItemFromPickListCommand {

    @TargetAggregateIdentifier
    private String pickId;
    private String sku;
    private boolean ignoreInventoryItemValidation = false;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getPickId(), "Pick id cannot be empty");
        throwExceptionIfParameterIsEmpty(this.getSku(), "SKU cannot be empty");
    }
}
