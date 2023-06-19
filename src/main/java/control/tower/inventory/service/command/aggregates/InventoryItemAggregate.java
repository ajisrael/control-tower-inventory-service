package control.tower.inventory.service.command.aggregates;

import control.tower.inventory.service.command.commands.CreateInventoryItemCommand;
import control.tower.inventory.service.command.commands.MoveInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemMovedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
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
        InventoryItemMovedEvent event = InventoryItemMovedEvent.builder()
                .sku(command.getSku())
                .productId(productId)
                .locationId(command.getLocationId())
                .binId(command.getBinId())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RemoveInventoryItemCommand command) {
        InventoryItemRemovedEvent event = InventoryItemRemovedEvent.builder()
                .sku(command.getSku())
                .productId(productId)
                .isCompensatingTransaction(command.isCompensatingTransaction())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(InventoryItemCreatedEvent event) {
        this.sku = event.getSku();
        this.productId = event.getProductId();
        this.locationId = event.getLocationId();
        this.binId = event.getBinId();
    }

    @EventSourcingHandler
    public void on(InventoryItemMovedEvent event) {
        this.locationId = event.getLocationId();
        this.binId = event.getBinId();
    }

    @EventSourcingHandler
    public void on(InventoryItemRemovedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
