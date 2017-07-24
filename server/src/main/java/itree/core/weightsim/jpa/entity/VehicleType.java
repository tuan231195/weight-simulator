package itree.core.weightsim.jpa.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "VR_VEHICLE_TYPE")
public class VehicleType
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

    @Column(name = "gross")
    private long gross;

    @Column(name = "AXLEGROUP_MASS_1")
    private long axleGroupMass1;

    @Column(name = "AXLEGROUP_MASS_2")
    private long axleGroupMass2;

    @Column(name = "AXLEGROUP_MASS_3")
    private long axleGroupMass3;

    @Column(name = "AXLEGROUP_MASS_4")
    private long axleGroupMass4;

    @Column(name = "AXLEGROUP_MASS_5")
    private long axleGroupMass5;


    @Column(name = "GRP_MASS_TOL_1")
    private long grpMassTol1;

    @Column(name = "GRP_MASS_TOL_2")
    private long grpMassTol2;

    @Column(name = "GRP_MASS_TOL_3")
    private long grpMassTol3;

    @Column(name = "GRP_MASS_TOL_4")
    private long grpMassTol4;

    @Column(name = "GRP_MASS_TOL_5")
    private long grpMassTol5;


    @OneToMany
    @JoinColumn(name = "vehicle_type_code", referencedColumnName = "code")
    private List<WeightInstruction> weightInstructions;


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

    public long getGross()
    {
        return gross;
    }

    public void setGross(long gross)
    {
        this.gross = gross;
    }

    public List<WeightInstruction> getWeightInstructions()
    {
        return weightInstructions;
    }

    public void setWeightInstructions(List<WeightInstruction> weightInstructions)
    {
        this.weightInstructions = weightInstructions;
    }
}
