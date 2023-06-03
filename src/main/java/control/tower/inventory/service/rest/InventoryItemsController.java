package control.tower.inventory.service.rest;

import control.tower.inventory.service.command.CreateInventoryItemCommand;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryItemsController {

    private Environment environment;
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

    @GetMapping
    public String getInventoryItem() {
        return "HTTP GET Handled " + environment.getProperty("local.server.port");
    }

    @PutMapping
    public String updateInventoryItem() {
        return "HTTP PUT Handled";
    }

    @DeleteMapping
    public String deleteInventoryItem() {
        return "HTTP DELETE Handled";
    }
}


