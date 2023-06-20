package control.tower.inventory.service.core.constants;

import static control.tower.core.constants.ExceptionMessages.*;
import static control.tower.inventory.service.core.constants.DomainConstants.*;

public class ExceptionMessages {

    private ExceptionMessages() {
        throw new IllegalStateException("Constants class");
    }

    public static final String SKU_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "sku");
    public static final String PRODUCT_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "productId");
    public static final String LOCATION_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "locationId");
    public static final String BIN_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "binId");
    public static final String PICK_ID_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "pickId");
    public static final String SKU_LIST_CANNOT_BE_NULL = String.format(PARAMETER_CANNOT_BE_NULL, "skuList");
    public static final String SKU_IN_LIST_CANNOT_BE_EMPTY = String.format(PARAMETER_CANNOT_BE_EMPTY, "sku in skuList");
    public static final String PICK_BY_DATE_CANNOT_BE_NULL = String.format(PARAMETER_CANNOT_BE_NULL, "pickByDate");

    public static final String INVENTORY_ITEM_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST =
            String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM_LOOKUP_ENTITY, "%s");
    public static final String INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST =
            String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM, "%s");
    public static final String INVENTORY_ITEM_WITH_ID_ALREADY_EXISTS =
            String.format(ENTITY_WITH_ID_ALREADY_EXISTS, INVENTORY_ITEM, "%s");
    public static final String PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST =
            String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, PICK_LIST_LOOKUP_ENTITY, "%s");
    public static final String PICK_LIST_LOOKUP_ENTITY_WITH_ID_ALREADY_EXISTS =
            String.format(ENTITY_WITH_ID_ALREADY_EXISTS, PICK_LIST_LOOKUP_ENTITY, "%s");
    public static final String INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST =
            String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_LOOKUP_ENTITY, "%s");
    public static final String PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST =
            String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, PICK_LIST_ENTITY, "%s");
    public static final String INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY_WITH_ID_ALREADY_EXISTS =
            String.format(ENTITY_WITH_ID_ALREADY_EXISTS, INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY, "%s");
    public static final String INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST =
            String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY, "%s");
    public static final String INVENTORY_ITEM_HISTORY_WITH_ID_DOES_NOT_EXIST =
            String.format(ENTITY_WITH_ID_DOES_NOT_EXIST, INVENTORY_ITEM_HISTORY, "%s");

    public static final String INVENTORY_ITEM_IS_ASSIGNED_TO_DIFFERENT_PICK_LIST =
            "Inventory item %s is assigned to different pick list %s";
    public static final String INVENTORY_ITEM_ALREADY_ASSIGNED_TO_PICK_LIST =
            "Inventory item %s is already in pick list %s";
    public static final String AT_LEAST_ONE_SKU_IS_NOT_PICKED_CANNOT_COMPLETE_PICK_LIST =
            "At least one sku is not picked, cannot mark pick list as complete";
    public static final String SKU_IS_NOT_IN_PICK_LIST = "Sku %s is not in pick list %s";
}
