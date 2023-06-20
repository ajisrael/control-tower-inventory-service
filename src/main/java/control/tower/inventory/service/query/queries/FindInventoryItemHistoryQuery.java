package control.tower.inventory.service.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindInventoryItemHistoryQuery {

    private String sku;
}
