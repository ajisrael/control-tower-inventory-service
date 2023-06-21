package control.tower.inventory.service.query.querymodels;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PickListQueryModel {

    private String pickId;
    private List<PickItemQueryModel> pickItems;
    private Date pickByDate;
    private boolean isComplete;
}
