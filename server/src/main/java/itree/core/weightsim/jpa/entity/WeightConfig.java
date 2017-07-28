package itree.core.weightsim.jpa.entity;


public class WeightConfig
{
    private String code;
    private String typeName;
    private Double gross;
    private Integer plateNum;
    private Integer step;
    private Long[] scales;
    private Boolean[] scaleActive;
    private Boolean[] scaleJoin;

    public WeightConfig(String code, String typeName, Double gross, Integer plateNum, Integer step, Long[] scales, Boolean[] scaleActive, Boolean[] scaleJoin)
    {
        this.code = code;
        this.typeName = typeName;
        this.gross = gross;
        this.plateNum = plateNum;
        this.step = step;
        this.scales = scales;
        this.scaleActive = scaleActive;
        this.scaleJoin = scaleJoin;
    }

    public String getCode()
    {
        return code;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public Double getGross()
    {
        return gross;
    }

    public Integer getPlateNum()
    {
        return plateNum;
    }

    public Integer getStep()
    {
        return step;
    }

    public Long[] getScales()
    {
        return scales;
    }

    public Boolean[] getScaleActive()
    {
        return scaleActive;
    }

    public Boolean[] getScaleJoin()
    {
        return scaleJoin;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public void setGross(Double gross)
    {
        this.gross = gross;
    }

    public void setPlateNum(Integer plateNum)
    {
        this.plateNum = plateNum;
    }

    public void setStep(Integer step)
    {
        this.step = step;
    }

    public void setScales(Long[] scales)
    {
        this.scales = scales;
    }

    public void setScaleActive(Boolean[] scaleActive)
    {
        this.scaleActive = scaleActive;
    }

    public void setScaleJoin(Boolean[] scaleJoin)
    {
        this.scaleJoin = scaleJoin;
    }
}
