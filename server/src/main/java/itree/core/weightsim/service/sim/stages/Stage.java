package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.StageState;

public interface Stage
{
    /**
     * Run this stage
     *
     * @param weightConfig: the current weight config for this step
     * @return
     */
     StageState run(WeightConfig weightConfig);
}
