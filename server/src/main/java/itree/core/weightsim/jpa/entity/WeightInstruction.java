package itree.core.weightsim.jpa.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TBS_WEIGH_INSTRUCTION")
public class WeightInstruction implements Serializable
{
    @Id
    @Column(name = "CODE")
    private String code;

    @Id
    @Column(name = "PLATE_NUM")
    private Integer plateNum;

    @Id
    @Column(name = "STEP")
    private Integer step;

    @Column(name = "SCALE_1_ACTIVE")
    private Character scale1Active;

    @Column(name = "SCALE_2_ACTIVE")
    private Character scale2Active;

    @Column(name = "SCALE_3_ACTIVE")
    private Character scale3Active;

    @Column(name = "SCALE_4_ACTIVE")
    private Character scale4Active;

    @Column(name = "SCALE_5_ACTIVE")
    private Character scale5Active;

    @Column(name = "SCALE_JOIN_1_2")
    private Character scaleJoin12;

    @Column(name = "SCALE_JOIN_2_3")
    private Character scaleJoin23;

    @Column(name = "SCALE_JOIN_3_4")
    private Character scaleJoin34;

    @Column(name = "SCALE_JOIN_4_5")
    private Character scaleJoin45;

    @ManyToOne
    @JoinColumn(name = "CODE", referencedColumnName = "VEHICLE_TYPE_CODE", updatable = false, insertable = false)
    private VehicleType vehicleType;

    @JsonIgnore
    public VehicleType getVehicleType()
    {
        return vehicleType;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        WeightInstruction that = (WeightInstruction) o;

        if (code != null ? !code.equals(that.code) : that.code != null)
        {
            return false;
        }
        if (plateNum != null ? !plateNum.equals(that.plateNum) : that.plateNum != null)
        {
            return false;
        }
        return step != null ? step.equals(that.step) : that.step == null;
    }

    @Override
    public int hashCode()
    {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (plateNum != null ? plateNum.hashCode() : 0);
        result = 31 * result + (step != null ? step.hashCode() : 0);
        return result;
    }
}
