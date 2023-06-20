package control.tower.inventory.service.core.data.entities;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="pickList")
public class PickListEntity implements Serializable {

    private static final long serialVersionUID = 889654123987456321L;

    @Id
    @Column(unique = true)
    private String pickId;
    @OneToMany(mappedBy = "pickList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickItemEntity> skuList;
    private Date pickByDate;
    private boolean isComplete;

    public boolean isSkuInSkuList(String sku) {
        return this.getSkuList().stream()
                .map(PickItemEntity::getSku)
                .anyMatch(matchingSku -> matchingSku.equals(sku));
    }
}
