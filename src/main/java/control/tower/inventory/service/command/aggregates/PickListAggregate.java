package control.tower.inventory.service.command.aggregates;

import control.tower.inventory.service.command.commands.AddInventoryItemToPickListCommand;
import control.tower.inventory.service.command.commands.CompletePickListCommand;
import control.tower.inventory.service.command.commands.CreatePickListCommand;
import control.tower.inventory.service.command.commands.PickInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemFromPickListCommand;
import control.tower.inventory.service.command.commands.RemovePickListCommand;
import control.tower.inventory.service.command.commands.UpdatePickListDateCommand;
import control.tower.inventory.service.core.events.InventoryItemAddedToPickListEvent;
import control.tower.inventory.service.core.events.InventoryItemPickedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedFromPickListEvent;
import control.tower.inventory.service.core.events.PickListCompletedEvent;
import control.tower.inventory.service.core.events.PickListCreatedEvent;
import control.tower.inventory.service.core.events.PickListDateUpdatedEvent;
import control.tower.inventory.service.core.events.PickListRemovedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aggregate
@NoArgsConstructor
@Getter
public class PickListAggregate {

    @AggregateIdentifier
    private String pickId;
    private Map<String, Boolean> skuPickedStateMap;
    private Date pickByDate;
    private boolean isComplete;

    @CommandHandler
    PickListAggregate(CreatePickListCommand command) {
        PickListCreatedEvent event = PickListCreatedEvent.builder()
                .pickId(command.getPickId())
                .pickByDate(command.getPickByDate())
                .skuList(command.getSkuList())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(AddInventoryItemToPickListCommand command) {
        InventoryItemAddedToPickListEvent event = InventoryItemAddedToPickListEvent.builder()
                .pickId(command.getPickId())
                .sku(command.getSku())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(PickInventoryItemCommand command) {
        InventoryItemPickedEvent event = InventoryItemPickedEvent.builder()
                .sku(command.getSku())
                .pickId(command.getPickId())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RemoveInventoryItemFromPickListCommand command) {
        InventoryItemRemovedFromPickListEvent event = InventoryItemRemovedFromPickListEvent.builder()
                .pickId(command.getPickId())
                .sku(command.getSku())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(UpdatePickListDateCommand command) {
        PickListDateUpdatedEvent event = PickListDateUpdatedEvent.builder()
                .pickId(command.getPickId())
                .pickByDate(command.getPickByDate())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(CompletePickListCommand command) {
        PickListCompletedEvent event = PickListCompletedEvent.builder()
                .pickId(command.getPickId())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RemovePickListCommand command) {
        PickListRemovedEvent event = PickListRemovedEvent.builder()
                .pickId(command.getPickId())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(PickListCreatedEvent event) {
        this.pickId = event.getPickId();
        this.pickByDate = event.getPickByDate();
        this.isComplete = false;

        this.skuPickedStateMap = new HashMap<>();
        for (String sku : event.getSkuList()) {
            skuPickedStateMap.put(sku, Boolean.FALSE);
        }
    }

    @EventSourcingHandler
    public void on(InventoryItemAddedToPickListEvent event) {
        this.skuPickedStateMap.put(event.getSku(), Boolean.FALSE);
    }

    @EventSourcingHandler
    public void on(InventoryItemPickedEvent event) {
        this.skuPickedStateMap.put(event.getSku(), Boolean.TRUE);
    }

    @EventSourcingHandler
    public void on(InventoryItemRemovedFromPickListEvent event) {
        this.skuPickedStateMap.remove(event.getSku());
    }

    @EventSourcingHandler
    public void on(PickListDateUpdatedEvent event) {
        this.pickByDate = event.getPickByDate();
    }

    @EventSourcingHandler
    public void on(PickListCompletedEvent event) {
        this.isComplete = true;
    }

    @EventSourcingHandler
    public void on(PickListRemovedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
