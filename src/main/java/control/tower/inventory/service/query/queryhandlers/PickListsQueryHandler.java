package control.tower.inventory.service.query.queryhandlers;

import control.tower.inventory.service.core.data.converters.PickListEntityToPickListDtoConverter;
import control.tower.inventory.service.core.data.dtos.PickListDto;
import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.core.data.entities.PickItemEntity;
import control.tower.inventory.service.core.data.entities.PickListEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemRepository;
import control.tower.inventory.service.core.data.repositories.PickListRepository;
import control.tower.inventory.service.query.queries.FindAllPickListsQuery;
import control.tower.inventory.service.query.queries.FindPickListQuery;
import control.tower.inventory.service.query.querymodels.PickItemQueryModel;
import control.tower.inventory.service.query.querymodels.PickListQueryModel;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST;
import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class PickListsQueryHandler {

    private final PickListRepository pickListRepository;
    private final PickListEntityToPickListDtoConverter pickListEntityToPickListDtoConverter;
    private final InventoryItemRepository inventoryItemRepository;

    @QueryHandler
    public List<PickListQueryModel> findAllPickLists(FindAllPickListsQuery query) {
        List<PickListEntity> pickListEntities = pickListRepository.findAll();

        List<PickListQueryModel> pickListQueryModels = new ArrayList<>();

        for (PickListEntity pickListEntity: pickListEntities) {
            pickListQueryModels.add(
                    convertPickListDtoToPickListQueryModel(
                            pickListEntityToPickListDtoConverter.convert(
                                    pickListEntity)));
        }

        return pickListQueryModels;
    }

    @QueryHandler
    public PickListQueryModel findPickList(FindPickListQuery query) {
        PickListEntity pickListEntity = pickListRepository.findById(query.getPickId()).orElseThrow(
                () -> new IllegalArgumentException(
                        String.format(PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST, query.getPickId())));

        PickListDto pickListDto = pickListEntityToPickListDtoConverter.convert(pickListEntity);

        return convertPickListDtoToPickListQueryModel(pickListDto);
    }

    private PickListQueryModel convertPickListDtoToPickListQueryModel(PickListDto pickListDto) {
        PickListQueryModel pickListQueryModel = new PickListQueryModel();

        pickListQueryModel.setPickId(pickListDto.getPickId());
        pickListQueryModel.setPickByDate(pickListDto.getPickByDate());
        pickListQueryModel.setComplete(pickListDto.isComplete());

        List<PickItemQueryModel> pickItemQueryModels = new ArrayList<>();

        for (String sku: pickListDto.getSkuMap().keySet()) {
            PickItemQueryModel pickItemQueryModel = new PickItemQueryModel();
            pickItemQueryModel.setSku(sku);
            pickItemQueryModel.setPicked(pickListDto.getSkuMap().get(sku));

            InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(sku).orElseThrow(
                    () -> new IllegalArgumentException(String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, sku)));

            pickItemQueryModel.setProductId(inventoryItemEntity.getProductId());
            pickItemQueryModel.setLocationId(inventoryItemEntity.getLocationId());
            pickItemQueryModel.setBinId(inventoryItemEntity.getBinId());

            pickItemQueryModels.add(pickItemQueryModel);
        }

        pickListQueryModel.setPickItems(pickItemQueryModels);

        return pickListQueryModel;
    }
}
