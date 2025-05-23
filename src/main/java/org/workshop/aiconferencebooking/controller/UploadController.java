package org.workshop.aiconferencebooking.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.workshop.aiconferencebooking.model.Person;
import org.workshop.aiconferencebooking.service.PersonService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class UploadController {

    private final PersonService personService;

    public UploadController(PersonService personService) {
        this.personService = personService;
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/uploadimage")
    public String displayUploadForm() {
        return "person/upload";
    }

    @PostMapping("/uploadimage")
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file,Principal principal) throws IOException {
        //get file name
        String fileName = file.getOriginalFilename();
        //save file to the UPLOAD_DIRECTORY
        File filePath = new File(UPLOAD_DIRECTORY + "/" + fileName);

        //get file bytes and write to dir
        byte[] bytes = file.getBytes();
        Files.write(Paths.get(filePath.getAbsolutePath()), bytes);
        //get person
        Person person = getPerson(model, principal);
        //send file name to person
        if (person != null) {
            person.setProfilePicture(fileName);
            personService.save(person);
            model.addAttribute("message", "File uploaded successfully");
        } else {
            model.addAttribute("message", "ERROR");
        }


        return "person/upload";
    }

    public Person getPerson(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("message", "ERROR");
            return null;
        }

        var user = principal.getName();
        var person = personService.findByUsername(user);
        return person;
    }
}