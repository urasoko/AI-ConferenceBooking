package org.workshop.aiconferencebooking.controller;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.workshop.aiconferencebooking.model.Event;
import org.workshop.aiconferencebooking.model.Talk;
import org.workshop.aiconferencebooking.repository.EventRepository;
import org.workshop.aiconferencebooking.repository.SearchRepository;

import java.util.List;

@Controller
public class LogoutController {
    @Autowired
    EntityManager em;

    private final EventRepository eventRepository;
    private final SearchRepository searchRepository;

    public LogoutController(EventRepository eventRepository, SearchRepository searchRepository) {
        this.eventRepository = eventRepository;
        this.searchRepository = searchRepository;
    }

    @GetMapping({"/logout"})
    public String logoutPage(Model model) {
        List<Event> events = eventRepository.findAll();
        if (!events.isEmpty()) {
            model.addAttribute("event", events.get(0));
            model.addAttribute("talks", events.get(0).getTalks());
        }
        return "index";
    }

    @PostMapping("/search")
    public String searchTalks(Model model, @RequestParam String input) {
        List<Event> events = eventRepository.findAll();
        if (!events.isEmpty()) {
            model.addAttribute("event", events.get(0));
            model.addAttribute("talks", searchTalk(input));
        }
        return "index";
    }

    public List<Talk> searchTalk(String input) {
       //convert input to lowercase
       var lowerInput = input.toLowerCase();
       //create sql query
       var query = em.createNativeQuery("select * from Talk t where lower(description) like '%" + lowerInput + "%' OR lower(title) like '%" + lowerInput + "%'", Talk.class);
       var talks = (List<Talk>) query.getResultList();
       return talks;
       

    }


}
