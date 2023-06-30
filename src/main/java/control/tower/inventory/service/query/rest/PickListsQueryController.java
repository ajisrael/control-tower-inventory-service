package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.query.queries.FindAllPickListsQuery;
import control.tower.inventory.service.query.queries.FindPickListQuery;
import control.tower.inventory.service.query.querymodels.PickListQueryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickList")
@Tag(name = "Pick List Query API")
public class PickListsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all pick lists")
    public List<PickListQueryModel> getPickLists() {
        return queryGateway.query(new FindAllPickListsQuery(),
                ResponseTypes.multipleInstancesOf(PickListQueryModel.class)).join();
    }

    @GetMapping(params = "pickId")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get pick list by pickId")
    public PickListQueryModel getPickList(String pickId) {
        return queryGateway.query(new FindPickListQuery(pickId),
                ResponseTypes.instanceOf(PickListQueryModel.class)).join();
    }
}
