package itree.core.weightsim.jpa.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(schema = "SHARED", name = "VR_VEHICLE_TYPE")
public class VehicleType implements Serializable
{
    @Id
    @Column(name = "VEHICLE_TYPE_CODE")
    private String code;

    @Column(name = "VEHICLE_TYPE_NAME")
    private String typeName;

    @Column(name = "VEHICLE_CAT_CODE")
    private String catCode;

    @Column(name = "BITMAP_NAME")
    private String bitmapName;

    @Column(name = "GROSS")
    private Double gross;

    @Column(name = "AXLEGROUP_MASS_1")
    private Long axleGroupMass1;

    @Column(name = "AXLEGROUP_MASS_2")
    private Long axleGroupMass2;

    @Column(name = "AXLEGROUP_MASS_3")
    private Long axleGroupMass3;

    @Column(name = "AXLEGROUP_MASS_4")
    private Long axleGroupMass4;

    @Column(name = "AXLEGROUP_MASS_5")
    private Long axleGroupMass5;

    @Column(name = "GRP_MASS_TOL_1")
    private Long grpMassTol1;

    @Column(name = "GRP_MASS_TOL_2")
    private Long grpMassTol2;

    @Column(name = "GRP_MASS_TOL_3")
    private Long grpMassTol3;

    @Column(name = "GRP_MASS_TOL_4")
    private Long grpMassTol4;

    @Column(name = "GRP_MASS_TOL_5")
    private Long grpMassTol5;

    @OneToMany(mappedBy = "vehicleType")
    private Set<WeightInstruction> weightInstructions;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getCatCode()
    {
        return catCode;
    }

    public void setCatCode(String catCode)
    {
        this.catCode = catCode;
    }

    public String getBitmapName()
    {
        return bitmapName;
    }

    public void setBitmapName(String bitmapName)
    {
        this.bitmapName = bitmapName;
    }

    public Double getGross()
    {
        return gross;
    }

    public void setGross(Double gross)
    {
        this.gross = gross;
    }

    public Set<WeightInstruction> getWeightInstructions()
    {
        return weightInstructions;
    }

    public void setWeightInstructions(Set<WeightInstruction> weightInstructions)
    {
        this.weightInstructions = weightInstructions;
    }

    @Override
    public String toString()
    {
        return "VehicleType{" +
                "code='" + code + '\'' +
                ", typeName='" + typeName + '\'' +
                ", catCode='" + catCode + '\'' +
                ", bitmapName='" + bitmapName + '\'' +
                ", gross=" + gross +
                ", axleGroupMass1=" + axleGroupMass1 +
                ", axleGroupMass2=" + axleGroupMass2 +
                ", axleGroupMass3=" + axleGroupMass3 +
                ", axleGroupMass4=" + axleGroupMass4 +
                ", axleGroupMass5=" + axleGroupMass5 +
                ", grpMassTol1=" + grpMassTol1 +
                ", grpMassTol2=" + grpMassTol2 +
                ", grpMassTol3=" + grpMassTol3 +
                ", grpMassTol4=" + grpMassTol4 +
                ", grpMassTol5=" + grpMassTol5 +
                '}';
    }
}
