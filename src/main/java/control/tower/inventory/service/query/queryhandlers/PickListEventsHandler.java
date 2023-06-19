package control.tower.inventory.service.query.queryhandlers;

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
        PickListEntity pickListEntity = pickListRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListEntity, "Pick list entity does not exist");

        InventoryItemAssignedToPickListEntity inventoryItemAssignedToPickListEntity =
                inventoryItemAssignedToPickListRepository.findBySku(event.getSku());

        throwExceptionIfEntityDoesExist(
                inventoryItemAssignedToPickListEntity, "InventoryItemAssignedToPickListEntity already exists");

        PickListDto pickListDto = pickListEntityToPickListDtoConverter.convert(pickListEntity);

        if (pickListDto.getSkuMap().containsKey(event.getSku())) {
            throw new IllegalArgumentException("Sku already in pick list entity sku list");
        }

        inventoryItemAssignedToPickListRepository.save(
                new InventoryItemAssignedToPickListEntity(event.getSku(), pickListEntity, Boolean.FALSE));
    }

    @EventHandler
    public void on(InventoryItemPickedEvent event) {
        PickListEntity pickListEntity = pickListRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListEntity, "Pick list entity does not exist");

        InventoryItemAssignedToPickListEntity inventoryItemAssignedToPickListEntity =
                inventoryItemAssignedToPickListRepository.findBySku(event.getSku());

        throwExceptionIfEntityDoesNotExist(
                inventoryItemAssignedToPickListEntity, "InventoryItemAssignedToPickListEntity does not exist");

        PickListDto pickListDto = pickListEntityToPickListDtoConverter.convert(pickListEntity);

        if (!pickListDto.getSkuMap().containsKey(event.getSku())) {
            throw new IllegalArgumentException("Sku not in pick list entity sku list");
        }

        inventoryItemAssignedToPickListEntity.setSkuPicked(true);
        inventoryItemAssignedToPickListRepository.save(inventoryItemAssignedToPickListEntity);
    }

    @EventHandler
    public void on(InventoryItemRemovedFromPickListEvent event) {
        PickListEntity pickListEntity = pickListRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListEntity, "Pick list entity does not exist");

        InventoryItemAssignedToPickListEntity inventoryItemAssignedToPickListEntity =
                inventoryItemAssignedToPickListRepository.findBySku(event.getSku());

        throwExceptionIfEntityDoesNotExist(
                inventoryItemAssignedToPickListEntity, "InventoryItemAssignedToPickListEntity does not exist");

        PickListDto pickListDto = pickListEntityToPickListDtoConverter.convert(pickListEntity);

        if (!pickListDto.getSkuMap().containsKey(event.getSku())) {
            throw new IllegalArgumentException("Sku is not in pick list entity sku list");
        }

        List<InventoryItemAssignedToPickListEntity> skuList = pickListEntity.getSkuList();
        skuList.remove(inventoryItemAssignedToPickListEntity);
        pickListEntity.setSkuList(skuList);

        pickListRepository.save(pickListEntity);
        inventoryItemAssignedToPickListRepository.delete(inventoryItemAssignedToPickListEntity);
    }

    @EventHandler
    public void on(PickListDateUpdatedEvent event) {
        PickListEntity pickListEntity = pickListRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListEntity, "Pick list entity does not exist");

        pickListEntity.setPickByDate(event.getPickByDate());

        pickListRepository.save(pickListEntity);
    }

    @EventHandler
    public void on(PickListCompletedEvent event) {
        PickListEntity pickListEntity = pickListRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListEntity, "Pick list entity does not exist");

        pickListEntity.setComplete(true);

        pickListRepository.save(pickListEntity);
    }

    @EventHandler
    public void on(PickListRemovedEvent event) {
        PickListEntity pickListEntity = pickListRepository.findByPickId(event.getPickId());

        throwExceptionIfEntityDoesNotExist(pickListEntity, "Pick list entity does not exist");

        pickListRepository.delete(pickListEntity);
    }
}
