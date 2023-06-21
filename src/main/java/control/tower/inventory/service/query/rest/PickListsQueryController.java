package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.dtos.PickListDto;
import control.tower.inventory.service.query.queries.FindAllPickListsQuery;
import control.tower.inventory.service.query.queries.FindPickListQuery;
import control.tower.inventory.service.query.querymodels.PickItemQueryModel;
import control.tower.inventory.service.query.querymodels.PickListQueryModel;
import control.tower.inventory.service.query.rest.model.PickItemRestModel;
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
        List<PickListQueryModel> pickListQueryModels = queryGateway.query(new FindAllPickListsQuery(),
                ResponseTypes.multipleInstancesOf(PickListQueryModel.class)).join();

        return convertPickListQueryModelsToPickListRestModels(pickListQueryModels);
    }

    @GetMapping(params = "pickId")
    public PickListRestModel getPickList(String pickId) {
        PickListQueryModel pickListQueryModel = queryGateway.query(new FindPickListQuery(pickId),
                ResponseTypes.instanceOf(PickListQueryModel.class)).join();

        return convertPickListQueryModelToPickListRestModel(pickListQueryModel);
    }

    private List<PickListRestModel> convertPickListQueryModelsToPickListRestModels(
            List<PickListQueryModel> pickListQueryModels) {
        List<PickListRestModel> pickListRestModels = new ArrayList<>();

        for (PickListQueryModel pickListQueryModel: pickListQueryModels) {
            pickListRestModels.add(convertPickListQueryModelToPickListRestModel(pickListQueryModel));
        }

        return pickListRestModels;
    }

    private PickListRestModel convertPickListQueryModelToPickListRestModel(PickListQueryModel pickListQueryModel) {
        List<PickItemRestModel> pickItemRestModels = new ArrayList<>();

        for (PickItemQueryModel pickItemQueryModel: pickListQueryModel.getPickItems()) {
            pickItemRestModels.add(convertPickItemQueryModelToPickItemRestModel(pickItemQueryModel));
        }

        return new PickListRestModel(
                pickListQueryModel.getPickId(),
                pickItemRestModels,
                pickListQueryModel.getPickByDate(),
                pickListQueryModel.isComplete()
        );
    }

    private PickItemRestModel convertPickItemQueryModelToPickItemRestModel(PickItemQueryModel pickItemQueryModel) {
        return new PickItemRestModel(
                pickItemQueryModel.getSku(),
                pickItemQueryModel.getProductId(),
                pickItemQueryModel.getLocationId(),
                pickItemQueryModel.getBinId(),
                pickItemQueryModel.isPicked()
        );
    }
}
