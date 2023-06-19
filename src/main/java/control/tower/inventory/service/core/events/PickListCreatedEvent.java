package control.tower.inventory.service.core.events;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class PickListCreatedEvent {

    private String pickId;
    private List<String> skuList;
    private Date pickByDate;
}
