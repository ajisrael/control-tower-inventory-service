package control.tower.inventory.service.query.querymodels;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PickItemQueryModel {

    private String sku;
    private String productId;
    private String locationId;
    private String binId;
    private boolean picked;
}
