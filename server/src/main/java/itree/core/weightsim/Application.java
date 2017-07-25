package itree.core.weightsim;

import itree.core.weightsim.config.WebSocketConfig;
import itree.core.weightsim.model.SimConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(new Object[]{Application.class, WebSocketConfig.class}, args);
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService(SimConfig simConfig) {
        return Executors.newScheduledThreadPool(simConfig.getNumThreads());
    }
}
