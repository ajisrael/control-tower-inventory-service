package control.tower.inventory.service.command.rest;

import control.tower.inventory.service.command.commands.AddInventoryItemToPickListCommand;
import control.tower.inventory.service.command.commands.CompletePickListCommand;
import control.tower.inventory.service.command.commands.CreatePickListCommand;
import control.tower.inventory.service.command.commands.PickInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemFromPickListCommand;
import control.tower.inventory.service.command.commands.RemovePickListCommand;
import control.tower.inventory.service.command.commands.UpdatePickListDateCommand;
import control.tower.inventory.service.command.rest.requests.AddInventoryItemToPickListRequestModel;
import control.tower.inventory.service.command.rest.requests.CompletePickListRequestModel;
import control.tower.inventory.service.command.rest.requests.CreatePickListRequestModel;
import control.tower.inventory.service.command.rest.requests.PickInventoryItemRequestModel;
import control.tower.inventory.service.command.rest.requests.RemoveInventoryItemFromPickListRequestModel;
import control.tower.inventory.service.command.rest.requests.RemovePickListRequestModel;
import control.tower.inventory.service.command.rest.requests.UpdatePickListDateRequestModel;
import control.tower.inventory.service.command.rest.responses.PickListCreatedResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/pickList")
@Tag(name = "Pick List Command API")
public class PickListCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create pick list")
    public PickListCreatedResponseModel createPickList(@Valid @RequestBody CreatePickListRequestModel createPickListRequestModel) {
        CreatePickListCommand createPickListCommand = CreatePickListCommand.builder()
                .pickId(UUID.randomUUID().toString())
                .skuList(createPickListRequestModel.getSkuList())
                .pickByDate(createPickListRequestModel.getPickByDate())
                .build();

        String pickId = commandGateway.sendAndWait(createPickListCommand);

        return PickListCreatedResponseModel.builder().pickId(pickId).build();
    }

    @PutMapping(path = "/pick")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Pick inventory item")
    public void pickInventoryItem(@Valid @RequestBody PickInventoryItemRequestModel pickInventoryItemRequestModel) {
        PickInventoryItemCommand pickInventoryItemCommand = PickInventoryItemCommand.builder()
                .sku(pickInventoryItemRequestModel.getSku())
                .pickId(pickInventoryItemRequestModel.getPickId())
                .build();

        commandGateway.sendAndWait(pickInventoryItemCommand);
    }

    @PutMapping(path = "/addInventoryItem")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add inventory item to pick list")
    public void addInventoryItemToPickList(@Valid @RequestBody AddInventoryItemToPickListRequestModel addInventoryItemToPickListRequestModel) {
        AddInventoryItemToPickListCommand addInventoryItemToPickListCommand = AddInventoryItemToPickListCommand.builder()
                .pickId(addInventoryItemToPickListRequestModel.getPickId())
                .sku(addInventoryItemToPickListRequestModel.getSku())
                .build();

        commandGateway.sendAndWait(addInventoryItemToPickListCommand);
    }

    @PutMapping(path = "/removeInventoryItem")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove inventory item from pick list")
    public void removeInventoryItemToPickList(@Valid @RequestBody RemoveInventoryItemFromPickListRequestModel removeInventoryItemFromPickListRequestModel) {
        RemoveInventoryItemFromPickListCommand removeInventoryItemFromPickListCommand = RemoveInventoryItemFromPickListCommand.builder()
                .pickId(removeInventoryItemFromPickListRequestModel.getPickId())
                .sku(removeInventoryItemFromPickListRequestModel.getSku())
                .build();

        commandGateway.sendAndWait(removeInventoryItemFromPickListCommand);
    }

    @PutMapping(path = "/updateDate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update pick by date")
    public void updatePickByDate(@Valid @RequestBody UpdatePickListDateRequestModel updatePickListDateRequestModel) {
        UpdatePickListDateCommand updatePickListDateCommand = UpdatePickListDateCommand.builder()
                .pickId(updatePickListDateRequestModel.getPickId())
                .pickByDate(updatePickListDateRequestModel.getPickByDate())
                .build();

        commandGateway.sendAndWait(updatePickListDateCommand);
    }

    @PutMapping(path = "/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Complete pick list")
    public void completePickList(@Valid @RequestBody CompletePickListRequestModel completePickListRequestModel) {
        CompletePickListCommand completePickListCommand = CompletePickListCommand.builder()
                .pickId(completePickListRequestModel.getPickId())
                .build();

        commandGateway.sendAndWait(completePickListCommand);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove pick list")
    public void removePickList(@Valid @RequestBody RemovePickListRequestModel removePickListRequestModel) {
        RemovePickListCommand removePickListCommand = RemovePickListCommand.builder()
                .pickId(removePickListRequestModel.getPickId())
                .build();

        commandGateway.sendAndWait(removePickListCommand);
    }
}
