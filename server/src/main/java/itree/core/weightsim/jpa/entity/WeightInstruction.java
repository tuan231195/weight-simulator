package itree.core.weightsim.jpa.entity;

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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Integer getPlateNum()
    {
        return plateNum;
    }

    public void setPlateNum(Integer plateNum)
    {
        this.plateNum = plateNum;
    }

    public Integer getStep()
    {
        return step;
    }

    public void setStep(Integer step)
    {
        this.step = step;
    }

    public Character getScale1Active()
    {
        return scale1Active;
    }

    public void setScale1Active(Character scale1Active)
    {
        this.scale1Active = scale1Active;
    }

    public Character getScale2Active()
    {
        return scale2Active;
    }

    public void setScale2Active(Character scale2Active)
    {
        this.scale2Active = scale2Active;
    }

    public Character getScale3Active()
    {
        return scale3Active;
    }

    public void setScale3Active(Character scale3Active)
    {
        this.scale3Active = scale3Active;
    }

    public Character getScale4Active()
    {
        return scale4Active;
    }

    public void setScale4Active(Character scale4Active)
    {
        this.scale4Active = scale4Active;
    }

    public Character getScale5Active()
    {
        return scale5Active;
    }

    public void setScale5Active(Character scale5Active)
    {
        this.scale5Active = scale5Active;
    }

    public Character getScaleJoin12()
    {
        return scaleJoin12;
    }

    public void setScaleJoin12(Character scaleJoin12)
    {
        this.scaleJoin12 = scaleJoin12;
    }

    public Character getScaleJoin23()
    {
        return scaleJoin23;
    }

    public void setScaleJoin23(Character scaleJoin23)
    {
        this.scaleJoin23 = scaleJoin23;
    }

    public Character getScaleJoin34()
    {
        return scaleJoin34;
    }

    public void setScaleJoin34(Character scaleJoin34)
    {
        this.scaleJoin34 = scaleJoin34;
    }

    public Character getScaleJoin45()
    {
        return scaleJoin45;
    }

    public void setScaleJoin45(Character scaleJoin45)
    {
        this.scaleJoin45 = scaleJoin45;
    }

    public VehicleType getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType)
    {
        this.vehicleType = vehicleType;
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
