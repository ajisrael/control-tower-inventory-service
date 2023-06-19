package control.tower.inventory.service.saga;

import control.tower.core.commands.DecreaseProductStockForRemovedInventoryCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemFromPickListCommand;
import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemAssignedToPickListLookupRepository;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static control.tower.inventory.service.core.constants.LogMessages.*;

@Saga
public class RemoveInventoryItemSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient InventoryItemAssignedToPickListLookupRepository inventoryItemAssignedToPickListLookupRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveInventoryItemSaga.class);

    @StartSaga
    @EndSaga
    @SagaEventHandler(associationProperty = "sku")
    public void on(InventoryItemRemovedEvent inventoryItemRemovedEvent) {
        LOGGER.info(String.format(PROCESSING_INVENTORY_ITEM_REMOVED_EVENT_FOR_SKU, inventoryItemRemovedEvent.getSku()));

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                inventoryItemAssignedToPickListLookupRepository.findBySku(inventoryItemRemovedEvent.getSku());

        if (inventoryItemAssignedToPickListLookupEntity != null) {
            LOGGER.info("Removing inventory item from pick list");
            RemoveInventoryItemFromPickListCommand removeInventoryItemFromPickListCommand = RemoveInventoryItemFromPickListCommand.builder()
                            .pickId(inventoryItemAssignedToPickListLookupEntity.getPickListLookup().getPickId())
                            .sku(inventoryItemRemovedEvent.getSku())
                            .ignoreInventoryItemValidation(true)
                            .build();

            commandGateway.sendAndWait(removeInventoryItemFromPickListCommand);
        }

        if (inventoryItemRemovedEvent.isCompensatingTransaction()) {
            LOGGER.info(NOT_DECREMENTING_FROM_PRODUCT_STOCK_DUE_TO_COMPENSATING_TRANSACTION_FROM_EXCEPTION);
        } else {
            LOGGER.info(String.format(ISSUING_DECREASE_PRODUCT_STOCK_FOR_REMOVE_INVENTORY_ITEM_COMMAND, inventoryItemRemovedEvent.getProductId()));

            DecreaseProductStockForRemovedInventoryCommand decreaseProductStockForRemovedInventoryCommand = DecreaseProductStockForRemovedInventoryCommand.builder()
                    .productId(inventoryItemRemovedEvent.getProductId())
                    .sku(inventoryItemRemovedEvent.getSku())
                    .build();

            commandGateway.send(decreaseProductStockForRemovedInventoryCommand, (commandMessage, commandResultMessage) -> {
                if (commandResultMessage.isExceptional()) {
                    LOGGER.error(String.format(EXCEPTION_ENCOUNTERED_WHEN_ISSUING_DECREASE_PRODUCT_STOCK_FOR_REMOVED_INVENTORY_ITEM_COMMAND,
                            commandResultMessage.exceptionResult().getLocalizedMessage()));

                    LOGGER.info(String.format(SENDING_NOTIFICATION_TO_DECREASE_STOCK_LEVEL_BY_1_FOR_PRODUCT_ID,
                            inventoryItemRemovedEvent.getProductId()));
                }
            });
        }
    }
}
