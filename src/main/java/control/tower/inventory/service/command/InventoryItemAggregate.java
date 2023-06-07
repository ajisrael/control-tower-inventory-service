package control.tower.inventory.service.command;

import control.tower.inventory.service.command.commands.CreateInventoryItemCommand;
import control.tower.inventory.service.command.commands.MoveInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemMovedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
@Getter
public class InventoryItemAggregate {

    @AggregateIdentifier
    private String sku;
    private String productId;
    private String locationId;
    private String binId;

    @CommandHandler
    public InventoryItemAggregate(CreateInventoryItemCommand command) {
        command.validate();

        InventoryItemCreatedEvent event = InventoryItemCreatedEvent.builder()
                .sku(command.getSku())
                .productId(command.getProductId())
                .locationId(command.getLocationId())
                .binId(command.getBinId())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(MoveInventoryItemCommand command) {
        command.validate();

        InventoryItemMovedEvent event = InventoryItemMovedEvent.builder()
                .sku(command.getSku())
                .productId(command.getProductId())
                .locationId(command.getLocationId())
                .binId(command.getBinId())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RemoveInventoryItemCommand command) {
        command.validate();

        InventoryItemRemovedEvent event = InventoryItemRemovedEvent.builder()
                .sku(command.getSku())
                .productId(productId)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventHandler
    public void on(InventoryItemCreatedEvent event) {
        this.sku = event.getSku();
        this.productId = event.getProductId();
        this.locationId = event.getLocationId();
        this.binId = event.getBinId();
    }

    @EventHandler
    public void on(InventoryItemMovedEvent event) {
        this.locationId = event.getLocationId();
        this.binId = event.getBinId();
    }

    @EventHandler
    public void on(InventoryItemRemovedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
