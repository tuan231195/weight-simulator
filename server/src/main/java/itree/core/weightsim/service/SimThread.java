package itree.core.weightsim.service;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;

import java.sql.SQLException;
import java.util.List;

class SimThread implements Runnable
{
    private String vtype;
    private PlateConfig plateConfig;
    private boolean isRunnning;
    private WeightConfigDao weightConfigDao;

    SimThread(PlateConfig plateConfig, String type, WeightConfigDao weightConfigDao)
    {
        this.weightConfigDao = weightConfigDao;
        this.plateConfig = plateConfig;
        this.vtype = type;
    }

    public void run()
    {
        isRunnning = true;
        List<WeightConfig> weightConfigList = null;
        try
        {
            weightConfigList = weightConfigDao.findAll(this.plateConfig.getNumPlates(), this.vtype);
            int currentStage = 0;
            boolean nextStep;
            for (WeightConfig weightConfig : weightConfigList)
            {
                nextStep = false;
                while (!nextStep)
                {
                    switch(currentStage)
                    {
                        case 0:
                            stage0(weightConfig);
                            break;
                        case 1:
                            stage1(weightConfig);
                            break;
                        case 2:
                            stage2(weightConfig);
                            break;
                        case 3:
                            nextStep = true;
                            stage3(weightConfig);
                            break;
                    }
                    currentStage = (currentStage + 1) % 3;
                }
            }
        }
        catch (SQLException e)
        {

        }

    }

    private void stage3(WeightConfig weightConfig)
    {

    }

    private void stage2(WeightConfig weightConfig)
    {

    }

    private void stage1(WeightConfig weightConfig)
    {

    }

    private void stage0(WeightConfig weightConfig)
    {

    }
}
