package org.workshop.aiconferencebooking.controller;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.workshop.aiconferencebooking.service.PersonService;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class DownloadController {

    private PersonService personService;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";


    public String uploadImage(Model model, MultipartFile file, Principal principal) throws IOException {
        //get file name
        var name = file.getOriginalFilename();
        //get path
        var path = Paths.get(UPLOAD_DIRECTORY + File.separator + name);
        //write file to path
        Files.write(path, file.getBytes());
        //get person
        var user = principal.getName();
        var person = personService.findByUsername(user);
        //set image path
        person.setProfilePicture(name);
        //save person
        personService.save(person);
        //add person to model
        model.addAttribute("person", person);
        //return view
        model.addAttribute("msg", "Image uploaded successfully");


        return "person/upload";
    }
}