package cg.tcarespb;


import cg.tcarespb.service.customMail.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class TCareSpbApplication {

    @Autowired
    private EmailSenderService emailSenderService;

    public static void main(String[] args) {
        SpringApplication.run(TCareSpbApplication.class, args);
    }
    @GetMapping("/getHttpsResponse")
    public String getHttpResponse(){
        return "Response received from HTTP Service";
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void sendEmail() {
//        emailSenderService.sendEmail("quochuy248@gmail.com","this is subject","this is body");
//    }
}

