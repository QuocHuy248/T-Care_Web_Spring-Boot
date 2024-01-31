package cg.tcarespb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_infos")
@Getter
@Setter
public class CartInfo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private Boolean deleted = false;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private AddInfo addInfo;

    public CartInfo(Cart cart, AddInfo addInfo) {
        this.cart = cart;
        this.addInfo = addInfo;
    }
}
