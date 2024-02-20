package cg.tcarespb.controller.RESTcontroller;

import cg.tcarespb.service.employee.response.EmployeeListTop3Response;
import cg.tcarespb.service.rate.RateService;
import cg.tcarespb.service.rate.request.RateEditRequest;
import cg.tcarespb.service.rate.request.RateSaveRequest;
import cg.tcarespb.service.rate.response.RateDetailsResponse;
import cg.tcarespb.service.rate.response.RateListResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@AllArgsConstructor
@CrossOrigin("https://t-care-center.vercel.app")
public class RateRestController {
    private final RateService rateService;

    @GetMapping()
    public ResponseEntity<List<RateListResponse>> getRateListResponse() {
        List<RateListResponse> rateListResponses = rateService.getRateListResponse();
        return ResponseEntity.ok(rateListResponses);
    }

    @GetMapping("/top3")
    public ResponseEntity<List<EmployeeListTop3Response>> getTopEmployeeList(){
        List<EmployeeListTop3Response> listEmployee = rateService.get3Employee();
        return ResponseEntity.ok(listEmployee);
    }
    @PostMapping
    public void create(@RequestBody RateSaveRequest request){
        rateService.create(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> edit(@PathVariable("id") String id, @RequestBody RateEditRequest request) {
        rateService.edit(request, id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/id")
    public ResponseEntity<Void> delete(@PathVariable String id){
        rateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateDetailsResponse> getRateDetail(@PathVariable("id") String id){
        RateDetailsResponse rate = rateService.findRateById(id);
        return ResponseEntity.ok(rate);
    }

}
