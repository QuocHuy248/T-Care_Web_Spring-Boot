package cg.tcarespb.controller.RESTcontroller;

import cg.tcarespb.models.User;
import cg.tcarespb.service.employee.request.EmployeeAvatarSaveRequest;
import cg.tcarespb.service.user.UserService;
import cg.tcarespb.service.user.request.UserFavoriteListSaveRequest;
import cg.tcarespb.service.user.request.UserPhotoSaveRequest;
import cg.tcarespb.service.user.request.UserSaveRequest;
import cg.tcarespb.service.user.response.UserListResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
public class UserRestController {
    private final UserService userService;
    @GetMapping()
    public ResponseEntity<List<UserListResponse>> getUserListResponse() {
        List<UserListResponse> userList = userService.getUserListResponse();
        return ResponseEntity.ok(userList);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        UserListResponse userList = userService.findById(id);
        return ResponseEntity.ok(userList);
    }
    @PutMapping("{id}")
    public ResponseEntity<?> edit(@PathVariable("id") String id,@RequestBody UserSaveRequest req) {
        userService.edit(req,id);
        return ResponseEntity.noContent().build();    }

    @PostMapping("/favorites/{idUser}")
    public ResponseEntity<?> addFavoriteEmployee(@PathVariable("idUser") String idUser, UserFavoriteListSaveRequest req) {
        userService.updateFavoriteList(req, idUser);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public void create(@RequestBody UserSaveRequest request){
        userService.create(request);
    }
    @PutMapping("/photo/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable("id") String id, @RequestBody UserPhotoSaveRequest req) {
        userService.updatePhotoUser(req, id);
        return ResponseEntity.noContent().build();
    }
}
