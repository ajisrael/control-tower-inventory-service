package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
import java.util.List;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;
import static control.tower.core.utils.Helper.throwExceptionIfParameterIsNull;
import static control.tower.inventory.service.core.constants.ExceptionMessages.*;

@Getter
@Builder
public class CreatePickListCommand {

    @TargetAggregateIdentifier
    private String pickId;
    private List<String> skuList;
    private Date pickByDate;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getPickId(), PICK_ID_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsNull(this.getSkuList(), SKU_LIST_CANNOT_BE_NULL);

        for (String sku : this.getSkuList()) {
            throwExceptionIfParameterIsEmpty(sku, SKU_IN_LIST_CANNOT_BE_EMPTY);
        }

        throwExceptionIfParameterIsNull(this.getPickByDate(), PICK_BY_DATE_CANNOT_BE_NULL);
    }
}
