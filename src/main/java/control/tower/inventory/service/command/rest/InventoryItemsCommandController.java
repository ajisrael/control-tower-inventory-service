package control.tower.inventory.service.command.rest;

import control.tower.inventory.service.command.CreateInventoryItemCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryItemsCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String createInventoryItem(@RequestBody CreateInventoryItemRestModel createInventoryItemRestModel) {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(UUID.randomUUID().toString())
                .productId(createInventoryItemRestModel.getProductId())
                .locationId(createInventoryItemRestModel.getLocationId())
                .binId(createInventoryItemRestModel.getBinId())
                .build();

        String returnValue;

        try {
            returnValue = commandGateway.sendAndWait(createInventoryItemCommand);
        } catch (Exception exception) {
            returnValue = exception.getLocalizedMessage();
        }

        return returnValue;
    }
}

