package control.tower.inventory.service.core.constants;

import static control.tower.core.constants.ExceptionMessages.*;
import static control.tower.inventory.service.core.constants.DomainConstants.INVENTORY_ITEM;
import static control.tower.inventory.service.core.constants.DomainConstants.INVENTORY_ITEM_LOOKUP_ENTITY;

public class ExceptionMessages {

    private ExceptionMessages() {
        throw new IllegalStateException("Constants class");
    }

    public static final String SKU_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "sku");
    public static final String PRODUCT_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "productId");
    public static final String LOCATION_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "locationId");
    public static final String BIN_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "binId");

    public static final String INVENTORY_ITEM_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST = String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM_LOOKUP_ENTITY, "%s");
    public static final String INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST = String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM, "%s");
    public static final String INVENTORY_ITEM_WITH_ID_ALREADY_EXISTS = String.format(ENTITY_WITH_ID_ALREADY_EXISTS, INVENTORY_ITEM, "%s");
}
