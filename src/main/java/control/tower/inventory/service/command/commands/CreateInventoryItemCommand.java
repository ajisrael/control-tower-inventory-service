package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;
import static control.tower.inventory.service.core.constants.ExceptionMessages.SKU_CANNOT_BE_EMPTY;
import static control.tower.inventory.service.core.constants.ExceptionMessages.PRODUCT_ID_CANNOT_BE_EMPTY;
import static control.tower.inventory.service.core.constants.ExceptionMessages.LOCATION_ID_CANNOT_BE_EMPTY;
import static control.tower.inventory.service.core.constants.ExceptionMessages.BIN_ID_CANNOT_BE_EMPTY;

@Getter
@Builder
public class CreateInventoryItemCommand {

    @TargetAggregateIdentifier
    private String sku;
    private String productId;
    private String locationId;
    private String binId;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getSku(), SKU_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsEmpty(this.getProductId(), PRODUCT_ID_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsEmpty(this.getLocationId(), LOCATION_ID_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsEmpty(this.getBinId(), BIN_ID_CANNOT_BE_EMPTY);
    }
}
