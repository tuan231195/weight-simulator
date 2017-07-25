package itree.core.weightsim.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WeightService {
    private SimpMessagingTemplate template;

    public WeightService(SimpMessagingTemplate simpMessagingTemplate) {
        this.template = simpMessagingTemplate;
    }

    @MessageMapping("/state")
    public void getGreeting(Object data) {
        System.out.println(data);
        template.convertAndSend("/topic/test", "Hello");
    }
}
