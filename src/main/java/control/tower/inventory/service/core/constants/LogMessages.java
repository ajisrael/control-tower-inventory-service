package control.tower.inventory.service.core.constants;

import static control.tower.core.constants.LogMessages.*;
import static control.tower.inventory.service.core.constants.DomainConstants.*;

public class LogMessages {

    private LogMessages() {
        throw new IllegalStateException("Constants class");
    }

    public static final String PROCESSING_INVENTORY_ITEM_CREATED_EVENT_FOR_SKU =
            String.format(PROCESSING_EVENT_FOR_ID, INVENTORY_ITEM_CREATED_EVENT, SKU, "%s");
    public static final String PROCESSING_INVENTORY_ITEM_REMOVED_EVENT_FOR_SKU =
            String.format(PROCESSING_EVENT_FOR_ID, INVENTORY_ITEM_REMOVED_EVENT, SKU, "%s");
    public static final String ISSUING_INCREASE_PRODUCT_STOCK_FOR_NEW_INVENTORY_ITEM_COMMAND =
            String.format(ISSUING_COMMAND_FOR_ID, INCREASE_PRODUCT_STOCK_FOR_NEW_INVENTORY_ITEM_COMMAND, PRODUCT_ID, "%s");
    public static final String ISSUING_DECREASE_PRODUCT_STOCK_FOR_REMOVE_INVENTORY_ITEM_COMMAND =
            String.format(ISSUING_COMMAND_FOR_ID, DECREASE_PRODUCT_STOCK_FOR_REMOVED_INVENTORY_ITEM_COMMAND, PRODUCT_ID, "%s");
    public static final String EXCEPTION_ENCOUNTERED_WHEN_ISSUING_INCREASE_PRODUCT_STOCK_FOR_NEW_INVENTORY_ITEM_COMMAND =
            String.format(EXCEPTION_ENCOUNTERED_WHEN_ISSUING_COMMAND, INCREASE_PRODUCT_STOCK_FOR_NEW_INVENTORY_ITEM_COMMAND, "%s");
    public static final String ISSUING_COMPENSATING_TRANSACTION_WITH_REMOVE_INVENTORY_ITEM_COMMAND_FOR_SKU =
            String.format(ISSUING_COMPENSATING_TRANSACTION_WITH_COMMAND_FOR_ID, REMOVE_INVENTORY_ITEM_COMMAND, SKU, "%s");
    public static final String EXCEPTION_ENCOUNTERED_WHEN_ISSUING_DECREASE_PRODUCT_STOCK_FOR_REMOVED_INVENTORY_ITEM_COMMAND =
            String.format(EXCEPTION_ENCOUNTERED_WHEN_ISSUING_COMMAND, DECREASE_PRODUCT_STOCK_FOR_REMOVED_INVENTORY_ITEM_COMMAND, "%s");
    public static final String SENDING_NOTIFICATION_TO_DECREASE_STOCK_LEVEL_BY_1_FOR_PRODUCT_ID =
            "Sending notification to decrease stock level by 1 for productId [%s]";
    public static final String NOT_DECREMENTING_FROM_PRODUCT_STOCK_DUE_TO_COMPENSATING_TRANSACTION_FROM_EXCEPTION =
            "Not decrementing from product stock due to compensating transaction from exception";

}
