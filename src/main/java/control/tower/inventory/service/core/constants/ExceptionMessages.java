package control.tower.inventory.service.core.constants;

import static control.tower.core.constants.ExceptionMessages.ENTITY_WITH_ID_DOES_NOT_EXIST;
import static control.tower.core.constants.ExceptionMessages.PARAMETER_CANNOT_BE_EMPTY;
import static control.tower.inventory.service.core.constants.DomainConstants.INVENTORY_ITEM;

public class ExceptionMessages {

    private ExceptionMessages() {
        throw new IllegalStateException("Constants class");
    }

    public static final String SKU_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "sku");
    public static final String PRODUCT_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "productId");
    public static final String LOCATION_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "locationId");
    public static final String BIN_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "binId");

    public static final String INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST = String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM, "%s");
}
