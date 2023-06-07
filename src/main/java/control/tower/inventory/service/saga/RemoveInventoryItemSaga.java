package control.tower.inventory.service.saga;

import control.tower.core.commands.DecreaseProductStockForRemovedInventoryCommand;
import control.tower.core.commands.IncreaseProductStockForNewInventoryCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

@Saga
public class RemoveInventoryItemSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveInventoryItemSaga.class);

    @StartSaga
    @EndSaga
    @SagaEventHandler(associationProperty = "sku")
    public void on(InventoryItemRemovedEvent inventoryItemRemovedEvent) {
        DecreaseProductStockForRemovedInventoryCommand decreaseProductStockForRemovedInventoryCommand = DecreaseProductStockForRemovedInventoryCommand.builder()
                .productId(inventoryItemRemovedEvent.getProductId())
                .sku(inventoryItemRemovedEvent.getSku())
                .build();

        commandGateway.send(decreaseProductStockForRemovedInventoryCommand, new CommandCallback<DecreaseProductStockForRemovedInventoryCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends DecreaseProductStockForRemovedInventoryCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    LOGGER.error("Failed to decrement stock level for product " + inventoryItemRemovedEvent.getProductId());
                }
            }
        });
    }
}
