package cg.tcarespb.service.employee.response;

import cg.tcarespb.models.enums.EExperience;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeFilterResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String bio;
    private String descriptionAboutMySelf;
    private EExperience  eExperience;
    private String experience;
    private Double longitude;
    private Double latitude;
    private String nameLocation;
    private Double distanceToWork;
    private Float starAverage;
    private Integer rateQuantity;
    private String photoUrl;
    private List<String> skillName;
    private List<String> serviceName;
    private List<String> infoName;
    private String cartId;
    private String phone;

    public EmployeeFilterResponse(String id, String nameLocation, String firstName, String lastName, String bio, String descriptionAboutMySelf, EExperience experience, Double longitude, Double latitude) {
        this.id = id;
        this.nameLocation = nameLocation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.descriptionAboutMySelf = descriptionAboutMySelf;
        this.eExperience = experience;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
