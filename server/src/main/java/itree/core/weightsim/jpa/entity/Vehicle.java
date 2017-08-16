package itree.core.weightsim.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "VR_VEHICLE")
public class Vehicle
{
    @JsonProperty("code")
    @Id
    @Column(name = "VEHICLE_CODE")
    private String vehicleCode;

    @JsonProperty("type")
    @Column(name = "VEHICLE_TYPE_CODE")
    private String vehicleTypeCode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_TYPE_CODE", insertable = false, updatable = false)
    private VehicleType vehicleType;

    @JsonProperty("name")
    @Column(name = "VEHICLE_NAME")
    private String vehicleName;

    @Column(name = "AXLE1")
    private Long axle1;

    @Column(name = "AXLE2")
    private Long axle2;

    @Column(name = "AXLE3")
    private Long axle3;

    @Column(name = "AXLE4")
    private Long axle4;

    @Column(name = "AXLE5")
    private Long axle5;

    public String getVehicleCode()
    {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode)
    {
        this.vehicleCode = vehicleCode;
    }

    public String getVehicleTypeCode()
    {
        return vehicleTypeCode;
    }

    public void setVehicleTypeCode(String vehicleTypeCode)
    {
        this.vehicleTypeCode = vehicleTypeCode;
    }

    public String getVehicleName()
    {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName)
    {
        this.vehicleName = vehicleName;
    }

    public VehicleType getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public Long getAxle1()
    {
        return axle1;
    }

    public void setAxle1(Long axle1)
    {
        this.axle1 = axle1;
    }

    public Long getAxle2()
    {
        return axle2;
    }

    public void setAxle2(Long axle2)
    {
        this.axle2 = axle2;
    }

    public Long getAxle3()
    {
        return axle3;
    }

    public void setAxle3(Long axle3)
    {
        this.axle3 = axle3;
    }

    public Long getAxle4()
    {
        return axle4;
    }

    public void setAxle4(Long axle4)
    {
        this.axle4 = axle4;
    }

    public Long getAxle5()
    {
        return axle5;
    }

    public void setAxle5(Long axle5)
    {
        this.axle5 = axle5;
    }
}
