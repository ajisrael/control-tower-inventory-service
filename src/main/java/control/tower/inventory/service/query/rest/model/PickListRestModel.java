package control.tower.inventory.service.query.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

@Getter
@AllArgsConstructor
public class PickListRestModel {

    private String pickId;
    private Map<String, Boolean> skuMap;
    private Date pickByDate;
    private boolean isPicked;
}
