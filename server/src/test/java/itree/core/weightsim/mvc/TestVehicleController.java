package itree.core.weightsim.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import itree.core.weightsim.Application;
import itree.core.weightsim.controllers.VehicleController;
import itree.core.weightsim.jpa.dao.VehicleDao;
import itree.core.weightsim.jpa.dao.VehicleTypeDao;
import itree.core.weightsim.jpa.entity.Vehicle;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class TestVehicleController
{
    @Mock
    private VehicleDao vehicleDao;

    @Mock
    private VehicleTypeDao vehicleTypeDao;

    @InjectMocks
    private VehicleController vehicleController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
    }

    @Test
    public void testFindAllPlates() throws Exception
    {
        mockMvc.perform(get("/api/plates/{plate}/vtypes", 4));
        verify(vehicleTypeDao).findAllForPlate(4);
    }

    @Test
    public void testFindAllVehicles() throws Exception
    {
        mockMvc.perform(get("/api/vehicle/{code}", "AB"));
        verify(vehicleDao).findForType("AB");
    }

    @Test
    public void testCreateVehicle() throws Exception
    {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleName("AB");
        vehicle.setVehicleTypeCode("TEST");
        mockMvc.perform(post("/api/vehicle").content(asJsonString(vehicle)).contentType(MediaType.APPLICATION_JSON));
        verify(vehicleDao).create(any(Vehicle.class));
    }

    @Test
    public void testUpdate() throws Exception
    {
        Vehicle vehicle = new Vehicle();
        mockMvc.perform(put("/api/vehicle").content(asJsonString(vehicle)).contentType(MediaType.APPLICATION_JSON));
        verify(vehicleDao).update(any(Vehicle.class));
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
