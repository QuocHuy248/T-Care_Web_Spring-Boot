package cg.tcarespb.controller.RESTcontroller;



import cg.tcarespb.models.Photo;
import cg.tcarespb.service.photo.UploadPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/photos")
@AllArgsConstructor
@CrossOrigin("https://t-care-center.vercel.app")
public class PhotoRestController {
    private final UploadPhotoService uploadPhotoService;

    @PostMapping
    public Photo upload(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        return uploadPhotoService.saveAvatar(avatar);
    }

}