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
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/pickList")
public class PickListCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String createPickList(@Valid @RequestBody CreatePickListRestModel createPickListRestModel) {
        CreatePickListCommand createPickListCommand = CreatePickListCommand.builder()
                .pickId(UUID.randomUUID().toString())
                .skuList(createPickListRestModel.getSkuList())
                .pickByDate(createPickListRestModel.getPickByDate())
                .build();

        return commandGateway.sendAndWait(createPickListCommand);
    }

    @PutMapping(path = "/pick")
    public String pickInventoryItem(@Valid @RequestBody PickInventoryItemRestModel pickInventoryItemRestModel) {
        PickInventoryItemCommand pickInventoryItemCommand = PickInventoryItemCommand.builder()
                .sku(pickInventoryItemRestModel.getSku())
                .pickId(pickInventoryItemRestModel.getPickId())
                .build();

        return commandGateway.sendAndWait(pickInventoryItemCommand);

    }

    @PutMapping(path = "/addInventoryItem")
    public String addInventoryItemToPickList(@Valid @RequestBody AddInventoryItemToPickListRestModel addInventoryItemToPickListRestModel) {
        AddInventoryItemToPickListCommand addInventoryItemToPickListCommand = AddInventoryItemToPickListCommand.builder()
                .pickId(addInventoryItemToPickListRestModel.getPickId())
                .sku(addInventoryItemToPickListRestModel.getSku())
                .build();

        return commandGateway.sendAndWait(addInventoryItemToPickListCommand);
    }

    @PutMapping(path = "/removeInventoryItem")
    public String removeInventoryItemToPickList(@Valid @RequestBody RemoveInventoryItemFromPickListRestModel removeInventoryItemFromPickListRestModel) {
        RemoveInventoryItemFromPickListCommand removeInventoryItemFromPickListCommand = RemoveInventoryItemFromPickListCommand.builder()
                .pickId(removeInventoryItemFromPickListRestModel.getPickId())
                .sku(removeInventoryItemFromPickListRestModel.getSku())
                .build();

        return commandGateway.sendAndWait(removeInventoryItemFromPickListCommand);
    }

    @PutMapping(path = "/updateDate")
    public String updatePickByDate(@Valid @RequestBody UpdatePickListDateRestModel updatePickListDateRestModel) {
        UpdatePickListDateCommand updatePickListDateCommand = UpdatePickListDateCommand.builder()
                .pickId(updatePickListDateRestModel.getPickId())
                .pickByDate(updatePickListDateRestModel.getPickByDate())
                .build();

        return commandGateway.sendAndWait(updatePickListDateCommand);
    }

    @PutMapping(path = "/complete")
    public String completePickList(@Valid @RequestBody CompletePickListRestModel completePickListRestModel) {
        CompletePickListCommand completePickListCommand = CompletePickListCommand.builder()
                .pickId(completePickListRestModel.getPickId())
                .build();

        return commandGateway.sendAndWait(completePickListCommand);
    }

    @DeleteMapping
    public String removePickList(@Valid @RequestBody RemovePickListRestModel removePickListRestModel) {
        RemovePickListCommand removePickListCommand = RemovePickListCommand.builder()
                .pickId(removePickListRestModel.getPickId())
                .build();

        return commandGateway.sendAndWait(removePickListCommand);
    }
}
