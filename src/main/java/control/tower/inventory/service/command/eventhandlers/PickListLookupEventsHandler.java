package control.tower.inventory.service.command.eventhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemAssignedToPickListLookupRepository;
import control.tower.inventory.service.core.data.entities.PickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.PickListLookupRepository;
import control.tower.inventory.service.core.events.InventoryItemAddedToPickListEvent;
import control.tower.inventory.service.core.events.InventoryItemPickedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedFromPickListEvent;
import control.tower.inventory.service.core.events.PickListCreatedEvent;
import control.tower.inventory.service.core.events.PickListRemovedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.*;

@Component
@AllArgsConstructor
@ProcessingGroup("pick-list-group")
public class PickListLookupEventsHandler {

    private PickListLookupRepository pickListLookupRepository;
    private InventoryItemAssignedToPickListLookupRepository inventoryItemAssignedToPickListLookupRepository;

    @EventHandler
    public void on(PickListCreatedEvent event) {
        PickListLookupEntity pickListLookupEntity = new PickListLookupEntity();
        pickListLookupEntity.setPickId(event.getPickId());

        pickListLookupRepository.save(pickListLookupEntity);

        List<InventoryItemAssignedToPickListLookupEntity> skuList = new ArrayList<>();

        for (String sku : event.getSkuList()) {
            InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                    new InventoryItemAssignedToPickListLookupEntity(sku, pickListLookupEntity);
            skuList.add(inventoryItemAssignedToPickListLookupEntity);
        }

        pickListLookupEntity.setSkuList(skuList);

        inventoryItemAssignedToPickListLookupRepository.saveAll(skuList);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemAddedToPickListEvent event) {
        PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, event.getPickId()));

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                new InventoryItemAssignedToPickListLookupEntity(event.getSku(), pickListLookupEntity);

        List<InventoryItemAssignedToPickListLookupEntity> skuList = pickListLookupEntity.getSkuList();
        skuList.add(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupEntity.setSkuList(skuList);

        inventoryItemAssignedToPickListLookupRepository.save(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemPickedEvent event) {
        PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, event.getPickId()));

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                inventoryItemAssignedToPickListLookupRepository.findBySku(event.getSku());

        throwExceptionIfEntityDoesNotExist(inventoryItemAssignedToPickListLookupEntity,
                String.format(INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, event.getSku()));

        String assignedPickId = inventoryItemAssignedToPickListLookupEntity.getPickListLookup().getPickId();

        if (!assignedPickId.equals(event.getPickId())) {
            throw new IllegalArgumentException(
                    String.format(INVENTORY_ITEM_IS_ASSIGNED_TO_DIFFERENT_PICK_LIST, event.getSku(), assignedPickId));
        }

        inventoryItemAssignedToPickListLookupEntity.setSkuPicked(true);

        inventoryItemAssignedToPickListLookupRepository.save(inventoryItemAssignedToPickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemRemovedFromPickListEvent event) {
        PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, event.getPickId()));

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                inventoryItemAssignedToPickListLookupRepository.findBySku(event.getSku());

        throwExceptionIfEntityDoesNotExist(inventoryItemAssignedToPickListLookupEntity,
                String.format(INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, event.getSku()));

        String assignedPickId = inventoryItemAssignedToPickListLookupEntity.getPickListLookup().getPickId();

        if (!assignedPickId.equals(event.getPickId())) {
            throw new IllegalArgumentException(
                    String.format(INVENTORY_ITEM_IS_ASSIGNED_TO_DIFFERENT_PICK_LIST, event.getSku(), assignedPickId));
        }

        List<InventoryItemAssignedToPickListLookupEntity> skuList = pickListLookupEntity.getSkuList();
        skuList.remove(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupEntity.setSkuList(skuList);

        inventoryItemAssignedToPickListLookupRepository.delete(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(PickListRemovedEvent event) {
        PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, event.getPickId()));

        pickListLookupRepository.delete(pickListLookupEntity);
    }
}
