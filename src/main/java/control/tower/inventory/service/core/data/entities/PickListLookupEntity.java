package control.tower.inventory.service.core.data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "picklistlookup")
public class PickListLookupEntity implements Serializable {

    private static final long serialVersionUID = -4787108556148621725L;

    @Id
    @Column(unique = true)
    private String pickId;
    @OneToMany(mappedBy = "pickListLookup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickItemLookupEntity> skuList;

    public boolean isSkuInSkuList(String sku) {
        return this.getSkuList().stream()
                .map(PickItemLookupEntity::getSku)
                .anyMatch(matchingSku -> matchingSku.equals(sku));
    }
}
