package itree.core.weightsim.service;


import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.util.TimerWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TestSimThread
{
    private SimThread simThread;
    private static final String CODE = "Bus";

    @Mock
    private WeightConfigDao weightConfigDao;

    @Mock
    private SimConfig simConfig;

    private WeightConfig weightConfig;

    @Mock
    private SocketGroup socketGroup;

    @Mock
    private TimerWrapper timerWrapper;

    @Mock
    private WeightGenerator weightGenerator;

    private static final int STEPS = 2;

    @Before
    public void setup() throws SQLException
    {
        weightConfig = new WeightConfig(CODE, "Testing", 20.0, 2, 2, new Long[]{10L, 10L}, new Boolean[]{true, true}, new Boolean[]{false});
        PlateConfig plateConfig = new PlateConfig("MICK", 2002, 2, 2);
        when(simConfig.getNumInitPackets()).thenReturn(4);
        when(simConfig.getTickRates()).thenReturn(1);
        when(simConfig.getRampPackets()).thenReturn(1);
        when(weightConfigDao.findAll(4, CODE)).thenReturn(createWeightConfigList(STEPS));
        simThread = spy(new SimThread(plateConfig, CODE, weightConfigDao, simConfig, socketGroup, timerWrapper, weightGenerator));
    }

    private List<WeightConfig> createWeightConfigList(int numSteps)
    {

        List<WeightConfig> weightConfigList = new ArrayList<WeightConfig>();
        for (int i = 0; i < numSteps; i++)
        {
            weightConfigList.add(mock(WeightConfig.class));
        }
        return weightConfigList;
    }

    @Test
    public void testStage0() throws InterruptedException
    {
        simThread.stage0();
        verify(socketGroup, times(simConfig.getNumInitPackets())).send(0, 0);
    }

    @Test
    public void testStage2() throws InterruptedException
    {
        when(weightGenerator.getWeight(weightConfig)).thenReturn(Arrays.asList(15.0, 20.0));
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(new Runnable()
        {
            @Override
            public void run()
            {
                simThread.setStabled(false);
                verify(socketGroup).send(0, 15);
                verify(socketGroup).send(1, 20);
            }
        }, 2, TimeUnit.MILLISECONDS);
        simThread.stage2(weightConfig);

    }

    @Test
    public void testStage1() throws InterruptedException
    {
        when(weightGenerator.getWeight(weightConfig)).thenReturn(Arrays.asList(10.0, 10.0));
        when(weightGenerator.stage(eq(1), anyDouble(), anyDouble())).thenReturn(5.0);
        simThread.stage1(weightConfig);
        verify(weightGenerator, times(2)).stage(1, 5.0, 5.0);
        verify(socketGroup).send(0, 5);
        verify(socketGroup).send(1, 5);
    }

    @Test
    public void testStage3() throws InterruptedException
    {
        when(weightGenerator.getWeight(weightConfig)).thenReturn(Arrays.asList(10.0, 10.0));
        when(weightGenerator.stage(eq(3), anyDouble(), anyDouble())).thenReturn(5.0);
        simThread.stage3(weightConfig);
        verify(weightGenerator, times(2)).stage(3, 5.0, 5.0);
        verify(socketGroup).send(0, 5);
        verify(socketGroup).send(1, 5);
    }


}
