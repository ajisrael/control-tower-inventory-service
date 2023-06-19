package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
import java.util.List;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;
import static control.tower.core.utils.Helper.throwExceptionIfParameterIsNull;

@Getter
@Builder
public class CreatePickListCommand {

    @TargetAggregateIdentifier
    private String pickId;
    private List<String> skuList;
    private Date pickByDate;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getPickId(), "Pick id cannot be empty");
        throwExceptionIfParameterIsNull(this.getSkuList(), "Sku list cannot be null");

        for (String sku : this.getSkuList()) {
            throwExceptionIfParameterIsEmpty(sku, "Sku in sku list cannot be empty");
        }

        throwExceptionIfParameterIsNull(this.getPickByDate(), "Pick by date cannot by null");
    }
}
