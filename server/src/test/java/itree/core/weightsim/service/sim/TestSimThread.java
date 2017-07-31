package itree.core.weightsim.service.sim;


import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.GedgeStruct;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.stages.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.Assert.*;
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

    private WeightConfig weightConfig1;

    private WeightConfig weightConfig2;

    @Mock
    private SocketGroup socketGroup;

    @Mock
    private WeightCalculator weightCalculator;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Mock
    private SimService simService;

    private static final int STEPS = 2;

    private Stage0 stage0;

    private Stage1 stage1;

    private Stage2 stage2;

    private Stage3 stage3;

    private PlateConfig plateConfig;

    @Before
    public void setup() throws SQLException
    {
        weightConfig1 = new WeightConfig(CODE, "Testing 1", 20.0, 2, 1, new Long[]{10L, 10L}, new Boolean[]{true, true}, new Boolean[]{false});
        weightConfig2 = new WeightConfig(CODE, "Testing 2", 20.0, 2, 2, new Long[]{8L, 12L}, new Boolean[]{true, true}, new Boolean[]{false});
        plateConfig = new PlateConfig("MICK", 2002, 2, 2);
        when(simConfig.getNumInitPackets()).thenReturn(1);
        when(simConfig.getTickRates()).thenReturn(1);
        when(simConfig.getNumRamPackets()).thenReturn(1);
        when(weightConfigDao.findAll(2, CODE)).thenReturn(createWeightConfigList());
        simThread = spy(new SimThread(plateConfig, CODE, simService, simConfig, weightConfigDao, scheduledExecutorService));
        List<Stage> stages = new ArrayList<Stage>();
        stage0 = spy(new Stage0(simThread, socketGroup));
        stages.add(stage0);
        stage1 = spy(new Stage1(simThread, socketGroup, weightCalculator));
        stages.add(stage1);
        stage2 = spy(new Stage2(simThread, socketGroup, weightCalculator));
        stages.add(stage2);
        stage3 = spy(new Stage3(simThread, socketGroup, weightCalculator));
        stages.add(stage3);
        simThread.setStages(stages);
    }

    private List<WeightConfig> createWeightConfigList()
    {
        List<WeightConfig> weightConfigList = new ArrayList<WeightConfig>();
        weightConfigList.add(weightConfig1);
        weightConfigList.add(weightConfig2);
        return weightConfigList;
    }

    @Test
    public void testTransition() throws InterruptedException
    {
        //step 1
        when(weightCalculator.getWeight(weightConfig1)).thenReturn(Arrays.asList(15.0, 20.0));
        when(weightCalculator.getWeight(weightConfig2)).thenReturn(Arrays.asList(15.0, 20.0));
        simThread.run();
        assertTrue(simThread.isRunning());
        verify(stage0).run(weightConfig1);
        simThread.run();
        verify(stage1).run(weightConfig1);
        assertTrue(stage2.isStabled());
        simThread.run();
        verify(stage2).run(weightConfig1);
        simThread.nextStage();
        assertFalse(stage2.isStabled());
        simThread.run();
        verify(stage3).run(weightConfig1);
        reset(stage0);

        //step 2
        simThread.run();
        verify(stage0).run(weightConfig2);
        simThread.run();
        verify(stage1).run(weightConfig2);
        simThread.run();
        verify(stage2).run(weightConfig2);
        simThread.nextStage();
        simThread.run();
        verify(stage3).run(weightConfig1);
        assertFalse(simThread.isRunning());
    }

    @Test
    public void testStage0() throws InterruptedException
    {
        stage0.run(weightConfig1);
        ArgumentCaptor<GedgeStruct> argumentCaptor = ArgumentCaptor.forClass(GedgeStruct.class);
        verify(socketGroup).send(eq(0), argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getWeight(), "0000.00");
    }

    @Test
    public void testStage1() throws InterruptedException
    {
        when(weightCalculator.mutate(eq(5.0), anyDouble())).thenReturn(5.0);
        when(weightCalculator.getWeight(weightConfig1)).thenReturn(Arrays.asList(10.0, 10.0));
        stage1.run(weightConfig1);
        verify(weightCalculator, times(2)).mutate(eq(5.0), eq(5.0));
        ArgumentCaptor<GedgeStruct> argumentCaptor = ArgumentCaptor.forClass(GedgeStruct.class);
        verify(socketGroup).send(eq(0), argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getWeight(), "0005.00");
        verify(socketGroup).send(eq(1), argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getWeight(), "0005.00");
    }

    @Test
    public void testStage2() throws InterruptedException
    {
        when(weightCalculator.getWeight(weightConfig1)).thenReturn(Arrays.asList(15.0, 20.0));
        stage2.setStabled(true);
        stage2.run(weightConfig1);
        ArgumentCaptor<GedgeStruct> argumentCaptor = ArgumentCaptor.forClass(GedgeStruct.class);
        verify(socketGroup).send(eq(0), argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getWeight(), "0015.00");
        verify(socketGroup).send(eq(1), argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getWeight(), "0020.00");
    }

    @Test
    public void testStage3() throws InterruptedException
    {
        when(weightCalculator.mutate(eq(6.0), anyDouble())).thenReturn(6.0);
        when(weightCalculator.mutate(eq(7.0), anyDouble())).thenReturn(7.0);
        when(weightCalculator.getWeight(weightConfig1)).thenReturn(Arrays.asList(12.0, 14.0));
        stage3.run(weightConfig1);
        ArgumentCaptor<GedgeStruct> argumentCaptor = ArgumentCaptor.forClass(GedgeStruct.class);
        verify(weightCalculator).mutate(eq(6.0), eq(6.0));
        verify(weightCalculator).mutate(eq(7.0), eq(7.0));
        verify(socketGroup).send(eq(0), argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getWeight(), "0006.00");
        verify(socketGroup).send(eq(1), argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getWeight(), "0007.00");
    }
}
