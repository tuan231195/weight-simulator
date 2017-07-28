package itree.core.weightsim.util;

import org.springframework.stereotype.Component;

@Component
public class TimerWrapper
{
    public void sleep(long seconds) throws InterruptedException
    {
        Thread.sleep(seconds * 1000);
    }
}
