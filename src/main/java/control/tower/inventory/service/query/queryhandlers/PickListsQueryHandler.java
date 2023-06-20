package control.tower.inventory.service.query.queryhandlers;

import control.tower.inventory.service.core.data.converters.PickListEntityToPickListDtoConverter;
import control.tower.inventory.service.core.data.dtos.PickListDto;
import control.tower.inventory.service.core.data.entities.PickListEntity;
import control.tower.inventory.service.core.data.repositories.PickListRepository;
import control.tower.inventory.service.query.queries.FindAllPickListsQuery;
import control.tower.inventory.service.query.queries.FindPickListQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class PickListsQueryHandler {

    private final PickListRepository pickListRepository;
    private final PickListEntityToPickListDtoConverter pickListEntityToPickListDtoConverter;

    @QueryHandler
    public List<PickListDto> findAllPickLists(FindAllPickListsQuery query) {
        List<PickListEntity> pickListEntities = pickListRepository.findAll();

        List<PickListDto> pickListDtos = new ArrayList<>();

        for (PickListEntity pickListEntity: pickListEntities) {
            pickListDtos.add(pickListEntityToPickListDtoConverter.convert(pickListEntity));
        }

        return pickListDtos;
    }

    @QueryHandler
    public PickListDto findPickList(FindPickListQuery query) {
        PickListEntity pickListEntity = pickListRepository.findById(query.getPickId()).orElseThrow(
                () -> new IllegalArgumentException(
                        String.format(PICK_LIST_ENTITY_WITH_ID_DOES_NOT_EXIST, query.getPickId())));

        return pickListEntityToPickListDtoConverter.convert(pickListEntity);
    }
}
