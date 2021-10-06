package com.cruzny.neopsal.registration;

import com.cruzny.Pingged.common.GenericService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping(path = "api/v1/registration")
@Controller
@AllArgsConstructor
@Log4j2
public class RegistrationController{

    private RegistrationService registrationService;

//    @PostMapping
    @RequestMapping(value = "/signup/registration" ,method = RequestMethod.POST)
    public String register(@ModelAttribute(value = "registrationRequest") RegistrationRequest registrationRequest){
        if(registrationService.registrationPreCheck(registrationRequest.getEmail())){
            throw new IllegalStateException("Email Not Valid");
        }
        registrationService.register(registrationRequest);
        return "fragments/registrationEmailSent";
    }

    @GetMapping(path = "/signup/registration/confirm")
    public String confirm(@RequestParam("token") String token){
//        return registrationService.confirmToken(token);
        log.info(registrationService.confirmToken(token));
        return "fragments/login";
    }

}