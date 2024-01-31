package cg.tcarespb.service.admin.response;

import cg.tcarespb.models.enums.EGender;
import cg.tcarespb.models.enums.EStatus;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminEmployeeResponse {
    private String id;
    private String personID;
    private String firstName;
    private String lastName;
    private EGender gender;
    private EStatus status;
}
