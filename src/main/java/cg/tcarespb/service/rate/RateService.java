package cg.tcarespb.service.rate;


import cg.tcarespb.models.Employee;
import cg.tcarespb.models.Rate;
import cg.tcarespb.models.User;
import cg.tcarespb.repository.EmployeeRepository;
import cg.tcarespb.repository.RateRepository;
import cg.tcarespb.repository.UserRepository;
import cg.tcarespb.service.employee.response.EmployeeListTop3Response;
import cg.tcarespb.service.rate.request.RateEditRequest;
import cg.tcarespb.service.rate.request.RateSaveRequest;
import cg.tcarespb.service.rate.response.RateDetailsResponse;
import cg.tcarespb.service.rate.response.RateListResponse;
import cg.tcarespb.util.AppMessage;
import cg.tcarespb.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RateService {
    private final RateRepository rateRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public List<RateListResponse> getRateListResponse(){
        return rateRepository.findAll()
                .stream()
                .map(service ->RateListResponse.builder()
                        .id(service.getId())
                        .starQuantity(service.getStarQuantity())
                        .content(service.getContent())
                        .rateQuantity(service.getRateQuantity())
                        .employeeName(service.getEmployee().getFirstName())
                        .build())
                .collect(Collectors.toList());
    }

    public void create(RateSaveRequest request){
        var rate = AppUtil.mapper.map(request, Rate.class);
        Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
        rate.setEmployee(employee.get());
        Optional<User> user = userRepository.findById(request.getUserId());
        rate.setUser(user.get());
        rate = rateRepository.save(rate);
    }

    public void edit(RateEditRequest request, String id){
        Rate rate = rateRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format(AppMessage.ID_NOT_FOUND, "Rate", id)));
        rate.setRateQuantity(Integer.valueOf(request.getRateQuantity()));
        rate.setStarQuantity(Float.valueOf(request.getStarQuantity()));
        rate.setContent(request.getContent());
        rateRepository.save(rate);
    }

    public Rate findById(String id) {
        return rateRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format(AppMessage.ID_NOT_FOUND, "Rate", id)));
    }

    public RateDetailsResponse findRateById(String id){
        var rate = rateRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format(AppMessage.ID_NOT_FOUND, "Rate", id)));
        var result = AppUtil.mapper.map(rate,RateDetailsResponse.class);
        result.setEmployeeId(rate.getEmployee().getFirstName());
        return result;
    }

    public List<EmployeeListTop3Response> get3Employee(){
        List<String> rateIdList = rateRepository.findTop3EmployeesWithHighestRate();

        List<Rate> rateList =  rateIdList.stream()
                .map(e ->  findById(e))
                .collect(Collectors.toList());
        return rateList.stream()
                .map(service -> EmployeeListTop3Response.builder()
                                .employeeId(service.getEmployee().getId())
                                .employeeName(service.getEmployee().getFirstName())
                                .employeeRateQuantity(String.valueOf(service.getRateQuantity()))
                                .employeeLocation(service.getEmployee().getLocationPlace().getName())
                                .photoURL(service.getEmployee().getPhoto().getUrl())
                                .content(service.getContent())
                                .userName(service.getUser().getFirstName())
                                .employeeStar(String.valueOf(service.getStarQuantity()))
                                .build())
                .collect(Collectors.toList());

//
//
//                        )


    }

    public void delete(String id){
        rateRepository.deleteById(id);
    }



}
