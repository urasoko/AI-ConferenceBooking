package org.workshop.aiconferencebooking.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.workshop.aiconferencebooking.model.Talk;
import org.workshop.aiconferencebooking.repository.TalkRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TalkController {
    private ResourceLoader resourceLoader;

    private final TalkRepository talkRepository;

    public TalkController(TalkRepository talkRepository) {
        this.talkRepository = talkRepository;
    }

    @GetMapping("/talks")
    public void displayTalks(
            @RequestParam String username, HttpServletResponse response
    ) throws IOException {

        List<Talk> talks = talkRepository.findBySpeakerUsername(username);

        response.setContentType("text/html");
        var writer = response.getWriter();
        buildTalksPage(username, talks, writer);
        writer.flush();
    }

    private void buildTalksPage(String username, List<Talk> talks, PrintWriter writer) throws IOException {

        String talksStr = talks.stream().map(
                talk -> String.format(getTalksTemplate(), talk.getTitle(), talk.getDescription())
        ).collect(Collectors.joining());

        String usernameStr = "<h1>" + username + "'s talks<h1>";

        writer.write(String.format(getPageTemplate(), usernameStr, talksStr));
    }

    private String getPageTemplate() throws IOException {
        Resource resource = new ClassPathResource("templates/talks.html");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private String getTalksTemplate() {
        return """
                    <div class="panel panel-default">
                        <div class="panel-heading">%s</div>
                        <div class="panel-body">%s</div>
                    </div>
                """;
    }
}
