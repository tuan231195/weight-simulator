package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;

public interface Stage
{
    void run(PlateConfig plateConfig, WeightConfig weightConfig) throws InterruptedException;
}
