package cg.tcarespb.service.rate.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RateEditRequest {
    private String starQuantity;
    private String content;
    private String rateQuantity;
    private String employeeId;
}
