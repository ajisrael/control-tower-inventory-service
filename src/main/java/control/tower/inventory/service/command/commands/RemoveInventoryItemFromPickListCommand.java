package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;
import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_ID_CANNOT_BE_EMPTY;
import static control.tower.inventory.service.core.constants.ExceptionMessages.SKU_CANNOT_BE_EMPTY;

@Getter
@Builder
public class RemoveInventoryItemFromPickListCommand {

    @TargetAggregateIdentifier
    private String pickId;
    private String sku;
    private boolean ignoreInventoryItemValidation = false;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getPickId(), PICK_ID_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsEmpty(this.getSku(), SKU_CANNOT_BE_EMPTY);
    }
}
