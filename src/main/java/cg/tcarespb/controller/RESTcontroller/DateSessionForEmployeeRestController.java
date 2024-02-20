package cg.tcarespb.controller.RESTcontroller;

import cg.tcarespb.service.dateSession.DateSessionService;
import cg.tcarespb.service.dateSession.request.DateSessionSaveRequestForEmployee;
import cg.tcarespb.service.dateSession.response.DateSessionListResponseForEmployee;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/date-session-for-employees")
@AllArgsConstructor
@CrossOrigin("https://t-care-center.vercel.app")
public class DateSessionForEmployeeRestController {
    private final DateSessionService dateSessionService;

    @GetMapping
    public ResponseEntity<List<DateSessionListResponseForEmployee>> getDateSessionForEmployee() {
        List<DateSessionListResponseForEmployee> dateSessionListResponseForEmployees = dateSessionService.getDateSessionListResponseForEmployee();
        return ResponseEntity.ok(dateSessionListResponseForEmployees);
    }

    @PostMapping
    public void create(@RequestBody DateSessionSaveRequestForEmployee request){
        dateSessionService.createDateSessionForEmployee(request);
    }

}
