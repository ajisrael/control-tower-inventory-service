package control.tower.inventory.service.query.rest.model;

import control.tower.inventory.service.query.querymodels.PickItemQueryModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class PickListRestModel {

    private String pickId;
    private List<PickItemRestModel> pickItems;
    private Date pickByDate;
    private boolean isComplete;
}
