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
public class HomeController {
    @Autowired
    EntityManager em;

    private final EventRepository eventRepository;
    private final SearchRepository searchRepository;

    public HomeController(EventRepository eventRepository, SearchRepository searchRepository) {
        this.eventRepository = eventRepository;
        this.searchRepository = searchRepository;
    }

    @GetMapping({"/", "/index", "/home"})
    public String homePage(Model model) {
        List<Event> events = eventRepository.findAll();
        if (!events.isEmpty()) {
            model.addAttribute("event", events.get(0));
            model.addAttribute("talks", events.get(0).getTalks());
        }
        return "index";
    }

    @PostMapping("/")
    public String searchTalks(Model model, @RequestParam String input) {
        List<Event> events = eventRepository.findAll();
        if (!events.isEmpty()) {
            model.addAttribute("event", events.get(0));
            model.addAttribute("talks", searchTalk(input));
        }
        return "index";
    }
    public List<Talk> searchTalk(String input) {
        return null;
    }
}
