package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.dtos.PickListDto;
import control.tower.inventory.service.query.queries.FindAllPickListsQuery;
import control.tower.inventory.service.query.queries.FindPickListQuery;
import control.tower.inventory.service.query.rest.model.PickListRestModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pickList")
public class PickListsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<PickListRestModel> getPickLists() {
        List<PickListDto> pickListDtos = queryGateway.query(new FindAllPickListsQuery(),
                ResponseTypes.multipleInstancesOf(PickListDto.class)).join();

        return convertPickListEntitiesToPickListRestModels(pickListDtos);
    }

    @GetMapping(params = "pickId")
    public PickListRestModel getPickList(String pickId) {
        PickListDto pickListDto = queryGateway.query(new FindPickListQuery(pickId),
                ResponseTypes.instanceOf(PickListDto.class)).join();

        return convertPickListEntityToPickListRestModel(pickListDto);
    }

    private List<PickListRestModel> convertPickListEntitiesToPickListRestModels(
            List<PickListDto> pickListDtos) {
        List<PickListRestModel> inventoryItemRestModels = new ArrayList<>();

        for (PickListDto pickListDto: pickListDtos) {
            inventoryItemRestModels.add(convertPickListEntityToPickListRestModel(pickListDto));
        }

        return inventoryItemRestModels;
    }

    private PickListRestModel convertPickListEntityToPickListRestModel(PickListDto pickListDto) {
        return new PickListRestModel(
                pickListDto.getPickId(),
                pickListDto.getSkuMap(),
                pickListDto.getPickByDate(),
                pickListDto.isPicked()
        );
    }
}
