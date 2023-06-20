package control.tower.inventory.service.command.eventhandlers;

import control.tower.inventory.service.core.data.entities.PickItemLookupEntity;
import control.tower.inventory.service.core.data.repositories.PickItemLookupRepository;
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
    private PickItemLookupRepository pickItemLookupRepository;

    @EventHandler
    public void on(PickListCreatedEvent event) {
        PickListLookupEntity pickListLookupEntity = new PickListLookupEntity();
        pickListLookupEntity.setPickId(event.getPickId());

        pickListLookupRepository.save(pickListLookupEntity);

        List<PickItemLookupEntity> skuList = new ArrayList<>();

        for (String sku : event.getSkuList()) {
            skuList.add(new PickItemLookupEntity(sku, pickListLookupEntity));
        }

        pickListLookupEntity.setSkuList(skuList);

        pickItemLookupRepository.saveAll(skuList);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemAddedToPickListEvent event) {
        PickListLookupEntity pickListLookupEntity = findPickListLookupEntityAndThrowExceptionIfItDoesNotExist(event.getPickId());

        PickItemLookupEntity pickItemLookupEntity = new PickItemLookupEntity(event.getSku(), pickListLookupEntity);

        List<PickItemLookupEntity> skuList = pickListLookupEntity.getSkuList();
        skuList.add(pickItemLookupEntity);
        pickListLookupEntity.setSkuList(skuList);

        pickItemLookupRepository.save(pickItemLookupEntity);
        pickListLookupRepository.save(pickListLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemPickedEvent event) {
        String pickId = event.getPickId();

        throwExceptionIfEntityDoesNotExist(pickListLookupRepository.findByPickId(pickId),
                String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

        String sku = event.getSku();

        PickItemLookupEntity pickItemLookupEntity = findPickItemLookupEntityAndThrowExceptionIfItDoesNotExist(sku);

        String assignedPickId = pickItemLookupEntity.getPickListLookup().getPickId();

        throwExceptionIfInventoryItemIsAssignedToDifferentPickId(assignedPickId, pickId, sku);

        pickItemLookupEntity.setSkuPicked(true);

        pickItemLookupRepository.save(pickItemLookupEntity);
    }

    @EventHandler
    public void on(InventoryItemRemovedFromPickListEvent event) {
        String pickId = event.getPickId();

        PickListLookupEntity pickListLookupEntity = findPickListLookupEntityAndThrowExceptionIfItDoesNotExist(pickId);

        String sku = event.getSku();

        PickItemLookupEntity pickItemLookupEntity = findPickItemLookupEntityAndThrowExceptionIfItDoesNotExist(sku);

        String assignedPickId = pickItemLookupEntity.getPickListLookup().getPickId();

        throwExceptionIfInventoryItemIsAssignedToDifferentPickId(assignedPickId, pickId, sku);

        List<PickItemLookupEntity> skuList = pickListLookupEntity.getSkuList();
        skuList.remove(pickItemLookupEntity);
        pickListLookupEntity.setSkuList(skuList);

        pickItemLookupRepository.delete(pickItemLookupEntity);
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

    private PickItemLookupEntity findPickItemLookupEntityAndThrowExceptionIfItDoesNotExist(String sku) {
        PickItemLookupEntity pickItemLookupEntity =
                pickItemLookupRepository.findBySku(sku);

        throwExceptionIfEntityDoesNotExist(pickItemLookupEntity,
                String.format(INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, sku));

        return pickItemLookupEntity;
    }

    private void throwExceptionIfInventoryItemIsAssignedToDifferentPickId(String assignedPickId, String eventPickId, String sku) {
        if (!assignedPickId.equals(eventPickId)) {
            throw new IllegalArgumentException(
                    String.format(INVENTORY_ITEM_IS_ASSIGNED_TO_DIFFERENT_PICK_LIST, sku, assignedPickId));
        }
    }
}
