package itree.core.weightsim.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WeightGenerator
{
    private static final double NOISE_RATE = 0.1;
    private Random random = new Random();

    /**
     * This method mutates the give value according to the specified stage:
     * <ul>
     * <li>0 - init stage: sending 0s</li>
     * <li>1 - unstable stage</li>
     * <li>2 - stable stage</li>
     * <li>3 - unstable stage</li>
     * </ul>
     *
     * @param stageNum: stage number
     * @param value:    given weight
     * @return the mutated weight
     */
    public double stage(int stageNum, double value, double delta)
    {
        double noise;
        boolean inc;
        switch (stageNum)
        {
            case 0:
                return 0;
            case 1:
                value += delta;
                noise = delta * NOISE_RATE * random.nextDouble();
                inc = random.nextInt(2) < 1;
                return inc ? noise + value : value - noise;
            case 2:
                return value;
            case 3:
                value -= delta;
                noise = delta * NOISE_RATE * random.nextDouble();
                inc = random.nextInt(2) < 1;
                return inc ? noise + value : value - noise;
            default:
                throw new IllegalArgumentException("Unsupported stage");
        }
    }
}
