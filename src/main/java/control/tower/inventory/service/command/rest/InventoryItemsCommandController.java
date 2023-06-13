package control.tower.inventory.service.command.rest;

import control.tower.inventory.service.command.commands.CreateInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
import control.tower.inventory.service.command.rest.models.CreateInventoryItemRestModel;
import control.tower.inventory.service.command.rest.models.RemoveInventoryItemRestModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryItemsCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String createInventoryItem(@Valid @RequestBody CreateInventoryItemRestModel createInventoryItemRestModel) {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(UUID.randomUUID().toString())
                .productId(createInventoryItemRestModel.getProductId())
                .locationId(createInventoryItemRestModel.getLocationId())
                .binId(createInventoryItemRestModel.getBinId())
                .build();

        return commandGateway.sendAndWait(createInventoryItemCommand);
    }

    @DeleteMapping
    public String removeInventoryItem(@Valid @RequestBody RemoveInventoryItemRestModel removeInventoryItemRestModel) {
        RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                .sku(removeInventoryItemRestModel.getSku())
                .build();

        return commandGateway.sendAndWait(removeInventoryItemCommand);
    }
}


