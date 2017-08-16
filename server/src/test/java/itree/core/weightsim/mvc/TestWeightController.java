package itree.core.weightsim.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import itree.core.weightsim.Application;
import itree.core.weightsim.controllers.WeightController;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.model.SimRequest;
import itree.core.weightsim.service.sim.SimService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class TestWeightController
{
    private MockMvc mockMvc;

    @InjectMocks
    private WeightController weightController;

    @Mock
    private SimConfig simConfig;

    private SimRequest simRequest;

    @Mock
    private SimService simService;


    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException
    {
        simRequest = new SimRequest();
        simRequest.setCode("AB");
        simRequest.setSessionId("1234");
        simRequest.setVersion(4);
        simRequest.setNumPlate(4);
        MockitoAnnotations.initMocks(this);
        weightController.init();
        mockMvc = MockMvcBuilders.standaloneSetup(weightController).build();
    }

    @Test
    public void testFindAllPlates() throws Exception
    {
        mockMvc.perform(get("/api/plates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void testSimulate() throws Exception
    {
        mockMvc.perform(post("/api/simulate").content(asJsonString(simRequest)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(simService).simulate(eq(simRequest.getSessionId()), any(PlateConfig.class), eq(simRequest.getCode()));
    }

    @Test
    public void testStop() throws Exception
    {
        mockMvc.perform(post("/api/stop").content(asJsonString(simRequest)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(simService).stop(eq(simRequest.getSessionId()));
    }

    @Test
    public void testNext() throws Exception
    {
        mockMvc.perform(post("/api/next").content(asJsonString(simRequest)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(simService).nextStep(eq(simRequest.getSessionId()));
    }

    @Test
    public void testGetState() throws Exception
    {
        mockMvc.perform(get("/api/state/{sessionId}", simRequest.getSessionId())).andExpect(status().isOk());
        verify(simService).getState(eq(simRequest.getSessionId()));
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
