package control.tower.inventory.service.command.rest;

import control.tower.inventory.service.command.commands.AddInventoryItemToPickListCommand;
import control.tower.inventory.service.command.commands.CompletePickListCommand;
import control.tower.inventory.service.command.commands.CreatePickListCommand;
import control.tower.inventory.service.command.commands.PickInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemFromPickListCommand;
import control.tower.inventory.service.command.commands.RemovePickListCommand;
import control.tower.inventory.service.command.commands.UpdatePickListDateCommand;
import control.tower.inventory.service.command.rest.requests.AddInventoryItemToPickListRestModel;
import control.tower.inventory.service.command.rest.requests.CompletePickListRestModel;
import control.tower.inventory.service.command.rest.requests.CreatePickListRestModel;
import control.tower.inventory.service.command.rest.requests.PickInventoryItemRestModel;
import control.tower.inventory.service.command.rest.requests.RemoveInventoryItemFromPickListRestModel;
import control.tower.inventory.service.command.rest.requests.RemovePickListRestModel;
import control.tower.inventory.service.command.rest.requests.UpdatePickListDateRestModel;
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
    public PickListCreatedResponseModel createPickList(@Valid @RequestBody CreatePickListRestModel createPickListRestModel) {
        CreatePickListCommand createPickListCommand = CreatePickListCommand.builder()
                .pickId(UUID.randomUUID().toString())
                .skuList(createPickListRestModel.getSkuList())
                .pickByDate(createPickListRestModel.getPickByDate())
                .build();

        String pickId = commandGateway.sendAndWait(createPickListCommand);

        return PickListCreatedResponseModel.builder().pickId(pickId).build();
    }

    @PutMapping(path = "/pick")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Pick inventory item")
    public void pickInventoryItem(@Valid @RequestBody PickInventoryItemRestModel pickInventoryItemRestModel) {
        PickInventoryItemCommand pickInventoryItemCommand = PickInventoryItemCommand.builder()
                .sku(pickInventoryItemRestModel.getSku())
                .pickId(pickInventoryItemRestModel.getPickId())
                .build();

        commandGateway.sendAndWait(pickInventoryItemCommand);
    }

    @PutMapping(path = "/addInventoryItem")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add inventory item to pick list")
    public void addInventoryItemToPickList(@Valid @RequestBody AddInventoryItemToPickListRestModel addInventoryItemToPickListRestModel) {
        AddInventoryItemToPickListCommand addInventoryItemToPickListCommand = AddInventoryItemToPickListCommand.builder()
                .pickId(addInventoryItemToPickListRestModel.getPickId())
                .sku(addInventoryItemToPickListRestModel.getSku())
                .build();

        commandGateway.sendAndWait(addInventoryItemToPickListCommand);
    }

    @PutMapping(path = "/removeInventoryItem")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove inventory item from pick list")
    public void removeInventoryItemToPickList(@Valid @RequestBody RemoveInventoryItemFromPickListRestModel removeInventoryItemFromPickListRestModel) {
        RemoveInventoryItemFromPickListCommand removeInventoryItemFromPickListCommand = RemoveInventoryItemFromPickListCommand.builder()
                .pickId(removeInventoryItemFromPickListRestModel.getPickId())
                .sku(removeInventoryItemFromPickListRestModel.getSku())
                .build();

        commandGateway.sendAndWait(removeInventoryItemFromPickListCommand);
    }

    @PutMapping(path = "/updateDate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update pick by date")
    public void updatePickByDate(@Valid @RequestBody UpdatePickListDateRestModel updatePickListDateRestModel) {
        UpdatePickListDateCommand updatePickListDateCommand = UpdatePickListDateCommand.builder()
                .pickId(updatePickListDateRestModel.getPickId())
                .pickByDate(updatePickListDateRestModel.getPickByDate())
                .build();

        commandGateway.sendAndWait(updatePickListDateCommand);
    }

    @PutMapping(path = "/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Complete pick list")
    public void completePickList(@Valid @RequestBody CompletePickListRestModel completePickListRestModel) {
        CompletePickListCommand completePickListCommand = CompletePickListCommand.builder()
                .pickId(completePickListRestModel.getPickId())
                .build();

        commandGateway.sendAndWait(completePickListCommand);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove pick list")
    public void removePickList(@Valid @RequestBody RemovePickListRestModel removePickListRestModel) {
        RemovePickListCommand removePickListCommand = RemovePickListCommand.builder()
                .pickId(removePickListRestModel.getPickId())
                .build();

        commandGateway.sendAndWait(removePickListCommand);
    }
}
