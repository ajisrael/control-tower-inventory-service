package control.tower.inventory.service.core.constants;

public class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String INVENTORY_ITEM = "Inventory item";
    public static final String INVENTORY_ITEM_LOOKUP_ENTITY = "Inventory item lookup entity";
    public static final String PICK_LIST_LOOKUP_ENTITY = "Pick list lookup entity";
    public static final String PICK_LIST_ENTITY = "Pick list entity";
    public static final String INVENTORY_ITEM_HISTORY = "Inventory item history";
    public static final String INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_LOOKUP_ENTITY = "Inventory item assigned to pick list lookup entity";
    public static final String INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY = "Inventory item assigned to pick list entity";
    public static final String SKU = "sku";
    public static final String PRODUCT_ID = "productId";
    public static final String INVENTORY_ITEM_CREATED_EVENT = "InventoryItemCreatedEvent";
    public static final String INVENTORY_ITEM_REMOVED_EVENT = "InventoryItemRemovedEvent";
    public static final String INCREASE_PRODUCT_STOCK_FOR_NEW_INVENTORY_ITEM_COMMAND = "IncreaseProductStockForNewInventoryCommand";
    public static final String DECREASE_PRODUCT_STOCK_FOR_REMOVED_INVENTORY_ITEM_COMMAND = "DecreaseProductStockForRemovedInventoryCommand";
    public static final String REMOVE_INVENTORY_ITEM_COMMAND = "RemoveInventoryItemCommand";
}
