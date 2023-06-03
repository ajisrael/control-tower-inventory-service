package control.tower.inventory.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryItemsController {

    @Autowired
    private Environment environment;

    @PostMapping
    public String createInventoryItem(@RequestBody CreateInventoryItemRestModel createInventoryItemRestModel) {
        return "HTTP POST Handled " + createInventoryItemRestModel.getProductId();
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


