package control.tower.inventory.service.saga;

import control.tower.core.commands.IncreaseProductStockForNewInventoryCommand;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
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
public class CreateInventoryItemSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInventoryItemSaga.class);

    @StartSaga
    @EndSaga
    @SagaEventHandler(associationProperty = "sku")
    public void handle(InventoryItemCreatedEvent inventoryItemCreatedEvent) {
        LOGGER.info(String.format(PROCESSING_INVENTORY_ITEM_CREATED_EVENT_FOR_SKU, inventoryItemCreatedEvent.getSku()));
        LOGGER.info(String.format(ISSUING_INCREASE_PRODUCT_STOCK_FOR_NEW_INVENTORY_ITEM_COMMAND, inventoryItemCreatedEvent.getProductId()));

        IncreaseProductStockForNewInventoryCommand increaseProductStockForNewInventoryCommand = IncreaseProductStockForNewInventoryCommand.builder()
                .productId(inventoryItemCreatedEvent.getProductId())
                .sku(inventoryItemCreatedEvent.getSku())
                .build();

        commandGateway.send(increaseProductStockForNewInventoryCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                LOGGER.error(String.format(EXCEPTION_ENCOUNTERED_WHEN_ISSUING_INCREASE_PRODUCT_STOCK_FOR_NEW_INVENTORY_ITEM_COMMAND,
                        commandResultMessage.exceptionResult().getLocalizedMessage()));

                LOGGER.info(String.format(ISSUING_COMPENSATING_TRANSACTION_WITH_REMOVE_INVENTORY_ITEM_COMMAND_FOR_SKU,
                        inventoryItemCreatedEvent.getSku()));

                RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                        .sku(inventoryItemCreatedEvent.getSku())
                        .isCompensatingTransaction(true)
                        .build();

                commandGateway.send(removeInventoryItemCommand);
            }
        });
    }
}
