package control.tower.inventory.service.saga;

import control.tower.core.commands.IncreaseProductStockForNewInventoryCommand;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

@Saga
public class CreateInventoryItemSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInventoryItemSaga.class);

    @StartSaga
    @EndSaga
    @SagaEventHandler(associationProperty = "sku")
    public void handle(InventoryItemCreatedEvent inventoryItemCreatedEvent) {
        LOGGER.info(String.format("Processing InventoryItemCreatedEvent for sku: [%s]", inventoryItemCreatedEvent.getSku()));

        IncreaseProductStockForNewInventoryCommand increaseProductStockForNewInventoryCommand = IncreaseProductStockForNewInventoryCommand.builder()
                .productId(inventoryItemCreatedEvent.getProductId())
                .sku(inventoryItemCreatedEvent.getSku())
                .build();

        commandGateway.send(increaseProductStockForNewInventoryCommand, new CommandCallback<IncreaseProductStockForNewInventoryCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends IncreaseProductStockForNewInventoryCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                            .sku(inventoryItemCreatedEvent.getSku())
                            .build();

                    commandGateway.send(removeInventoryItemCommand);
                }
            }
        });
    }
}
