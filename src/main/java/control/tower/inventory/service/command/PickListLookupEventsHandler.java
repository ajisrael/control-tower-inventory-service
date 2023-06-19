package control.tower.inventory.service.command;

import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemAssignedToPickListLookupRepository;
import control.tower.inventory.service.core.data.entities.PickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.PickListLookupRepository;
import control.tower.inventory.service.core.events.InventoryItemAddedToPickListEvent;
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

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity, "Pick list lookup entity not found for given pick id");

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                new InventoryItemAssignedToPickListLookupEntity(event.getSku(), pickListLookupEntity);

        List<InventoryItemAssignedToPickListLookupEntity> skuList = pickListLookupEntity.getSkuList();
        skuList.add(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupEntity.setSkuList(skuList);

        inventoryItemAssignedToPickListLookupRepository.save(inventoryItemAssignedToPickListLookupEntity);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemRemovedFromPickListEvent event) {
        PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity, "Pick list lookup entity not found for given pick id");

        InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                inventoryItemAssignedToPickListLookupRepository.findBySku(event.getSku());

        throwExceptionIfEntityDoesNotExist(inventoryItemAssignedToPickListLookupEntity,
                "inventoryItemAssignedToPickListLookupEntity does not exist");

        if (!inventoryItemAssignedToPickListLookupEntity.getPickListLookup().getPickId().equals(event.getPickId())) {
            throw new IllegalArgumentException("Inventory item is assigned to different pick list");
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

        throwExceptionIfEntityDoesNotExist(pickListLookupEntity, "Pick list lookup entity not found for given pick id");

        pickListLookupRepository.delete(pickListLookupEntity);
    }
}
