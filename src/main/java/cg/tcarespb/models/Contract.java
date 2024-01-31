package cg.tcarespb.models;

import cg.tcarespb.models.enums.EDecade;
import cg.tcarespb.models.enums.EGender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private LocalDate timeStart;
    private LocalDate timeEnd;
    private LocalDateTime createAt;
    private String namePatient;
    private String agePatient;
    private String content;
    @Column(length = 2000)
    @Length(max = 2000, message = "Description must not exceed 2000 characters")
    private String noteForPatient;
    @Column(length = 2000)
    @Length(max = 2000, message = "Description must not exceed 2000 characters")
    private String noteForEmployee;
    private Boolean deleted = false;
    private String nameService;
    private BigDecimal feeContact;
    private BigDecimal feePrice;
    private BigDecimal totalPrice;
    private BigDecimal priceService;
    private BigDecimal totalAmount;;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private String customerName;
    private String customerPhone;
    @Enumerated(EnumType.STRING)
    private EGender gender;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "contract")
    private List<HistoryWorking> historyWorking;
    @OneToOne
    private LocationPlace locationPlace;
}
