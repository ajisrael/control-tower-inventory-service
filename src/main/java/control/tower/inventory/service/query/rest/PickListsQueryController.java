package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.query.queries.FindAllPickListsQuery;
import control.tower.inventory.service.query.queries.FindPickListQuery;
import control.tower.inventory.service.query.querymodels.PickListQueryModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pickList")
public class PickListsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<PickListQueryModel> getPickLists() {
        return queryGateway.query(new FindAllPickListsQuery(),
                ResponseTypes.multipleInstancesOf(PickListQueryModel.class)).join();
    }

    @GetMapping(params = "pickId")
    public PickListQueryModel getPickList(String pickId) {
        return queryGateway.query(new FindPickListQuery(pickId),
                ResponseTypes.instanceOf(PickListQueryModel.class)).join();
    }
}
