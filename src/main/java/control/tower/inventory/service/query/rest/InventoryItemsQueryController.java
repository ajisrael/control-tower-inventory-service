package control.tower.inventory.service.query.rest;

import control.tower.core.rest.PageResponseType;
import control.tower.core.rest.PaginationResponse;
import control.tower.core.utils.PaginationUtility;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemHistoryQueryModel;
import control.tower.inventory.service.query.querymodels.InventoryItemQueryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE;
import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Item Query API")
public class InventoryItemsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all inventory items")
    public CompletableFuture<PaginationResponse<InventoryItemQueryModel>> getInventoryItems(
            @RequestParam(defaultValue = DEFAULT_PAGE) int currentPage,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        FindAllInventoryItemsQuery findAllInventoryItemsQuery = FindAllInventoryItemsQuery.builder()
                .pageable(PaginationUtility.buildPageable(currentPage, pageSize))
                .build();

        return queryGateway.query(findAllInventoryItemsQuery, new PageResponseType<>(InventoryItemQueryModel.class))
                .thenApply(PaginationUtility::toPageResponse);
    }

    @GetMapping(params = "sku")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get inventory item by sku")
    public CompletableFuture<InventoryItemQueryModel> getInventoryItem(String sku) {
        return queryGateway.query(new FindInventoryItemQuery(sku),
                ResponseTypes.instanceOf(InventoryItemQueryModel.class));
    }
}
