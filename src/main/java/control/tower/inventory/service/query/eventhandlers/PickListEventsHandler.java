package control.tower.inventory.service.query.eventhandlers;

import control.tower.inventory.service.core.data.converters.PickListDtoToPickListEntityConverter;
import control.tower.inventory.service.core.data.converters.PickListEntityToPickListDtoConverter;
import control.tower.inventory.service.core.data.dtos.PickListDto;
import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemAssignedToPickListRepository;
import control.tower.inventory.service.core.data.entities.PickListEntity;
import control.tower.inventory.service.core.data.repositories.PickListRepository;
import control.tower.inventory.service.core.events.InventoryItemAddedToPickListEvent;
import control.tower.inventory.service.core.events.InventoryItemPickedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedFromPickListEvent;
import control.tower.inventory.service.core.events.PickListCompletedEvent;
import control.tower.inventory.service.core.events.PickListCreatedEvent;
import control.tower.inventory.service.core.events.PickListDateUpdatedEvent;
import control.tower.inventory.service.core.events.PickListRemovedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesExist;
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.*;

@Component
@ProcessingGroup("pick-list-group")
public class PickListEventsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PickListEventsHandler.class);

    private final PickListRepository pickListRepository;
    private final InventoryItemAssignedToPickListRepository inventoryItemAssignedToPickListRepository;
    private final PickListDtoToPickListEntityConverter pickListDtoToPickListEntityConverter;
    private final PickListEntityToPickListDtoConverter pickListEntityToPickListDtoConverter;

    public PickListEventsHandler(PickListRepository pickListRepository,
                                 InventoryItemAssignedToPickListRepository inventoryItemAssignedToPickListRepository,
                                 PickListDtoToPickListEntityConverter pickListDtoToPickListEntityConverter,
                                 PickListEntityToPickListDtoConverter pickListEntityToPickListDtoConverter) {
        this.pickListRepository = pickListRepository;
        this.inventoryItemAssignedToPickListRepository = inventoryItemAssignedToPickListRepository;
        this.pickListDtoToPickListEntityConverter = pickListDtoToPickListEntityConverter;
        this.pickListEntityToPickListDtoConverter = pickListEntityToPickListDtoConverter;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        LOGGER.error(exception.getLocalizedMessage());
    }

    @EventHandler
    public void on(PickListCreatedEvent event) {
        Map<String, Boolean> skuMap = new HashMap<>();

        for (String sku : event.getSkuList()) {
            skuMap.put(sku, Boolean.FALSE);
        }

        PickListDto pickListDto = new PickListDto(
                event.getPickId(),
                skuMap,
                event.getPickByDate(),
                false
        );

        PickListEntity pickListEntity = pickListDtoToPickListEntityConverter.convert(pickListDto);

        pickListRepository.save(pickListEntity);
        inventoryItemAssignedToPickListRepository.saveAll(pickListEntity.getSkuList());
    }

    @EventHandler
    public void on(InventoryItemAddedToPickListEvent event) {
        String pickId = event.getPickId();

        PickListEntity pickListEntity = findPickListEntityAndThrowExceptionIfItDoesNotExist(pickId);

        String sku = event.getSku();

        InventoryItemAssignedToPickListEntity inventoryItemAssignedToPickListEntity =
                inventoryItemAssignedToPickListRepository.findBySku(sku);

        throwExceptionIfEntityDoesExist(inventoryItemAssignedToPickListEntity,
                String.format(INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY_WITH_ID_ALREADY_EXISTS, sku));

        PickListDto pickListDto = pickListEntityToPickListDtoConverter.convert(pickListEntity);

        if (pickListDto.getSkuMap().containsKey(sku)) {
            throw new IllegalArgumentException(
                    String.format(INVENTORY_ITEM_ALREADY_ASSIGNED_TO_PICK_LIST, sku, pickId));
        }

        inventoryItemAssignedToPickListRepository.save(
                new InventoryItemAssignedToPickListEntity(sku, pickListEntity, Boolean.FALSE));
    }

    @EventHandler
    public void on(InventoryItemPickedEvent event) {
        String pickId = event.getPickId();

        PickListEntity pickListEntity = findPickListEntityAndThrowExceptionIfItDoesNotExist(pickId);

        String sku = event.getSku();

        InventoryItemAssignedToPickListEntity inventoryItemAssignedToPickListEntity =
                inventoryItemAssignedToPickListRepository.findBySku(sku);

        throwExceptionIfEntityDoesNotExist(inventoryItemAssignedToPickListEntity,
                String.format(INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST, sku));

        PickListDto pickListDto = pickListEntityToPickListDtoConverter.convert(pickListEntity);

        if (!pickListDto.getSkuMap().containsKey(sku)) {
            throw new IllegalArgumentException(String.format(SKU_IS_NOT_IN_PICK_LIST, sku, pickId));
        }

        inventoryItemAssignedToPickListEntity.setSkuPicked(true);
        inventoryItemAssignedToPickListRepository.save(inventoryItemAssignedToPickListEntity);
    }

    @EventHandler
    public void on(InventoryItemRemovedFromPickListEvent event) {
        String pickId = event.getPickId();

        PickListEntity pickListEntity = findPickListEntityAndThrowExceptionIfItDoesNotExist(pickId);

        String sku = event.getSku();

        InventoryItemAssignedToPickListEntity inventoryItemAssignedToPickListEntity =
                inventoryItemAssignedToPickListRepository.findBySku(sku);

        throwExceptionIfEntityDoesNotExist(inventoryItemAssignedToPickListEntity,
                String.format(INVENTORY_ITEM_ASSIGNED_TO_PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST, sku));

        PickListDto pickListDto = pickListEntityToPickListDtoConverter.convert(pickListEntity);

        if (!pickListDto.getSkuMap().containsKey(event.getSku())) {
            throw new IllegalArgumentException(String.format(SKU_IS_NOT_IN_PICK_LIST, sku, pickId));
        }

        List<InventoryItemAssignedToPickListEntity> skuList = pickListEntity.getSkuList();
        skuList.remove(inventoryItemAssignedToPickListEntity);
        pickListEntity.setSkuList(skuList);

        pickListRepository.save(pickListEntity);
        inventoryItemAssignedToPickListRepository.delete(inventoryItemAssignedToPickListEntity);
    }

    @EventHandler
    public void on(PickListDateUpdatedEvent event) {
        PickListEntity pickListEntity = findPickListEntityAndThrowExceptionIfItDoesNotExist(event.getPickId());

        pickListEntity.setPickByDate(event.getPickByDate());

        pickListRepository.save(pickListEntity);
    }

    @EventHandler
    public void on(PickListCompletedEvent event) {
        PickListEntity pickListEntity = findPickListEntityAndThrowExceptionIfItDoesNotExist(event.getPickId());

        pickListEntity.setComplete(true);

        pickListRepository.save(pickListEntity);
    }

    @EventHandler
    public void on(PickListRemovedEvent event) {
        PickListEntity pickListEntity = findPickListEntityAndThrowExceptionIfItDoesNotExist(event.getPickId());

        pickListRepository.delete(pickListEntity);
    }

    private PickListEntity findPickListEntityAndThrowExceptionIfItDoesNotExist(String pickId) {
        PickListEntity pickListEntity = pickListRepository.findByPickId(pickId);

        throwExceptionIfEntityDoesNotExist(pickListEntity,
                String.format(PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

        return pickListEntity;
    }
}
