package control.tower.inventory.service.query.rest;

import control.tower.core.rest.PageResponseType;
import control.tower.core.rest.PaginationResponse;
import control.tower.core.utils.PaginationUtility;
import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.query.queries.FindAllInventoryItemHistoriesQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemHistoryQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemHistoryQueryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE;
import static control.tower.core.constants.DomainConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/inventoryHistory")
@Tag(name = "Inventory Item History Query API")
public class InventoryItemHistoriesQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all inventory item histories")
    public CompletableFuture<PaginationResponse<InventoryItemHistoryQueryModel>> getInventoryItemHistories(
            @RequestParam(defaultValue = DEFAULT_PAGE) int currentPage,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        FindAllInventoryItemHistoriesQuery findAllInventoryItemHistoriesQuery = FindAllInventoryItemHistoriesQuery.builder()
                .pageable(PaginationUtility.buildPageable(currentPage, pageSize))
                .build();

        return queryGateway.query(findAllInventoryItemHistoriesQuery,
                new PageResponseType<>(InventoryItemHistoryQueryModel.class))
                .thenApply(PaginationUtility::toPageResponse);
    }

    @GetMapping(params = "sku")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get inventory item history for sku")
    public InventoryItemHistoryQueryModel getInventoryItemHistory(String sku) {
        return queryGateway.query(new FindInventoryItemHistoryQuery(sku),
                ResponseTypes.instanceOf(InventoryItemHistoryQueryModel.class)).join();
    }

}
