package itree.core.weightsim.service;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.SimConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WeightGenerator
{
    private static final double NOISE_RATE = 0.1;
    private static final double OVERWEIGHT_PERCENTAGE = 0.1;
    private static final double A_DOUBLE = 9.99;
    private Random random = new Random();

    private final SimConfig simConfig;

    @Autowired
    public WeightGenerator(SimConfig simConfig)
    {
        this.simConfig = simConfig;
    }

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

    public List<Double> getWeight(WeightConfig weightConfig)
    {
        List<Double> weights = new ArrayList<Double>();
        int plateNum = weightConfig.getPlateNum();
        for (int i = 0; i < plateNum; i++)
        {
            weights.add(Double.valueOf(weightConfig.getScales()[i]));
        }
        for (int i = 0; i < plateNum - 1; i++)
        {
            if (weightConfig.getScaleJoin()[i] == Boolean.TRUE)
            {
                double weight = weights.get(i) / 2;
                weights.set(i, weight);
                weights.set(i + 1, weight);
            }
        }

        for (int i = 0; i < plateNum; i++)
        {
            if (weightConfig.getScaleActive()[i] != Boolean.TRUE)
                weights.set(i, A_DOUBLE);
        }

        for (int i = 0; i < plateNum; i++)
        {
            boolean[] overweightFlags = simConfig.getOverweight();
            if (overweightFlags.length > i && overweightFlags[i])
            {
                weights.set(i, weights.get(i) * (1 + OVERWEIGHT_PERCENTAGE));
            }
        }
        return weights;
    }
}
