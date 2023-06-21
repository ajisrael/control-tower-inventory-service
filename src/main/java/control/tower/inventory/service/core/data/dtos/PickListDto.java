package control.tower.inventory.service.core.data.dtos;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class PickListDto {
    private String pickId;
    private Map<String, Boolean> skuMap;
    private Date pickByDate;
    private boolean isComplete;

    public PickListDto(String pickId, Map<String, Boolean> skuMap, Date pickByDate, boolean isComplete) {
        this.pickId = pickId;
        this.skuMap = skuMap;
        this.pickByDate = pickByDate;
        this.isComplete = isComplete;
    }
}



