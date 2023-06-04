package control.tower.inventory.service.core.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="inventory")
public class InventoryItemEntity implements Serializable {

    private static final long serialVersionUID = 789654123987456321L;

    @Id
    @Column(unique = true)
    private String sku;
    private String productId;
    private String locationId;
    private String binId;
}
