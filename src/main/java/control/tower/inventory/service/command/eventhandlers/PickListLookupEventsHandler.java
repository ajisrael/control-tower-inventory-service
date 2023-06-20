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
            skuList.add(new InventoryItemAssignedToPickListLookupEntity(sku, pickListLookupEntity));
        }

        pickListLookupEntity.setSkuList(skuList);

        inventoryItemAssignedToPickListLookupRepository.saveAll(skuList);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemAddedToPickListEvent event) {
        PickListLookupEntity pickListLookupEntity = findPickListLookupEntityAndThrowExceptionIfItDoesNotExist(event.getPickId());

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
        String pickId = event.getPickId();

        throwExceptionIfEntityDoesNotExist(pickListLookupRepository.findByPickId(pickId),
                String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

        String sku = event.getSku();

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                findInventoryItemAssignedToPickListLookupEntityAndThrowExceptionIfItDoesNotExist(sku);

        String assignedPickId = inventoryItemAssignedToPickListLookupEntity.getPickListLookup().getPickId();

        throwExceptionIfInventoryItemIsAssignedToDifferentPickId(assignedPickId, pickId, sku);

        inventoryItemAssignedToPickListLookupEntity.setSkuPicked(true);

        inventoryItemAssignedToPickListLookupRepository.save(inventoryItemAssignedToPickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemRemovedFromPickListEvent event) {
        String pickId = event.getPickId();

        PickListLookupEntity pickListLookupEntity = findPickListLookupEntityAndThrowExceptionIfItDoesNotExist(pickId);

        String sku = event.getSku();

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                findInventoryItemAssignedToPickListLookupEntityAndThrowExceptionIfItDoesNotExist(sku);

        String assignedPickId = inventoryItemAssignedToPickListLookupEntity.getPickListLookup().getPickId();

        throwExceptionIfInventoryItemIsAssignedToDifferentPickId(assignedPickId, pickId, sku);

        List<InventoryItemAssignedToPickListLookupEntity> skuList = pickListLookupEntity.getSkuList();
        skuList.remove(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupEntity.setSkuList(skuList);

        inventoryItemAssignedToPickListLookupRepository.delete(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(PickListRemovedEvent event) {
        PickListLookupEntity pickListLookupEntity = findPickListLookupEntityAndThrowExceptionIfItDoesNotExist(event.getPickId());

        pickListLookupRepository.delete(pickListLookupEntity);
    }

    private PickListLookupEntity findPickListLookupEntityAndThrowExceptionIfItDoesNotExist(String pickId) {
        PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(pickId);

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

        return pickListLookupEntity;
    }

    private InventoryItemAssignedToPickListLookupEntity findInventoryItemAssignedToPickListLookupEntityAndThrowExceptionIfItDoesNotExist(String sku) {
        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                inventoryItemAssignedToPickListLookupRepository.findBySku(sku);

        throwExceptionIfEntityDoesNotExist(inventoryItemAssignedToPickListLookupEntity,
                String.format(INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, sku));

        return inventoryItemAssignedToPickListLookupEntity;
    }

    private void throwExceptionIfInventoryItemIsAssignedToDifferentPickId(String assignedPickId, String eventPickId, String sku) {
        if (!assignedPickId.equals(eventPickId)) {
            throw new IllegalArgumentException(
                    String.format(INVENTORY_ITEM_IS_ASSIGNED_TO_DIFFERENT_PICK_LIST, sku, assignedPickId));
        }
    }
}
