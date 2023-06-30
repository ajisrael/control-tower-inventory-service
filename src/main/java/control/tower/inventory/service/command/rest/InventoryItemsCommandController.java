package control.tower.inventory.service.command.rest;

import control.tower.inventory.service.command.commands.CreateInventoryItemCommand;
import control.tower.inventory.service.command.commands.MoveInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
import control.tower.inventory.service.command.rest.requests.CreateInventoryItemRestModel;
import control.tower.inventory.service.command.rest.requests.MoveInventoryItemRestModel;
import control.tower.inventory.service.command.rest.requests.RemoveInventoryItemRestModel;
import control.tower.inventory.service.command.rest.responses.InventoryItemCreatedResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Command API")
public class InventoryItemsCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create inventory item")
    public InventoryItemCreatedResponseModel createInventoryItem(
            @Valid @RequestBody CreateInventoryItemRestModel createInventoryItemRestModel) {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(UUID.randomUUID().toString())
                .productId(createInventoryItemRestModel.getProductId())
                .locationId(createInventoryItemRestModel.getLocationId())
                .binId(createInventoryItemRestModel.getBinId())
                .build();

        String sku = commandGateway.sendAndWait(createInventoryItemCommand);

        return InventoryItemCreatedResponseModel.builder().sku(sku).build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Move inventory item")
    public void moveInventoryItem(@Valid @RequestBody MoveInventoryItemRestModel moveInventoryItemRestModel) {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku(moveInventoryItemRestModel.getSku())
                .locationId(moveInventoryItemRestModel.getLocationId())
                .binId(moveInventoryItemRestModel.getBinId())
                .build();

        commandGateway.sendAndWait(moveInventoryItemCommand);
    }

    @DeleteMapping(params = "sku")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove inventory item")
    public void removeInventoryItem(@Valid @RequestBody RemoveInventoryItemRestModel removeInventoryItemRestModel) {
        RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                .sku(removeInventoryItemRestModel.getSku())
                .build();

        commandGateway.sendAndWait(removeInventoryItemCommand);
    }
}


