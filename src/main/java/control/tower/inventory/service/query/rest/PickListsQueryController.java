package control.tower.inventory.service.query.rest;

import control.tower.core.rest.PageResponseType;
import control.tower.core.rest.PaginationResponse;
import control.tower.core.utils.PaginationUtility;
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
import java.util.concurrent.CompletableFuture;

import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE;
import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE_SIZE;

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
    public CompletableFuture<PaginationResponse<PickListQueryModel>> getPickLists(
            @RequestParam(defaultValue = DEFAULT_PAGE) int currentPage,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        FindAllPickListsQuery findAllPickListsQuery = FindAllPickListsQuery.builder()
                .pageable(PaginationUtility.buildPageable(currentPage, pageSize))
                .build();

        return queryGateway.query(findAllPickListsQuery, new PageResponseType<>(PickListQueryModel.class))
                .thenApply(PaginationUtility::toPageResponse);
    }

    @GetMapping(params = "pickId")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get pick list by pickId")
    public CompletableFuture<PickListQueryModel> getPickList(String pickId) {
        return queryGateway.query(new FindPickListQuery(pickId),
                ResponseTypes.instanceOf(PickListQueryModel.class));
    }
}
