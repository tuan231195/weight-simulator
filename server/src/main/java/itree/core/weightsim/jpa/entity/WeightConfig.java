package itree.core.weightsim.jpa.entity;


public class WeightConfig
{
    private String code;
    private String typeName;
    private Double gross;
    private Integer plateNum;
    private Integer step;

    private Long scale1;
    private Long scale2;
    private Long scale3;
    private Long scale4;
    private Long scale5;

    private Character scale1Active;

    private Character scale2Active;

    private Character scale3Active;

    private Character scale4Active;

    private Character scale5Active;

    private Character scaleJoin12;

    private Character scaleJoin23;

    private Character scaleJoin34;

    private Character scaleJoin45;

    public WeightConfig(String code, String typeName, Double gross, Integer plateNum, Integer step, Long scale1, Long scale2, Long scale3, Long scale4, Long scale5, Character scale1Active, Character scale2Active, Character scale3Active, Character scale4Active, Character scale5Active, Character scaleJoin12, Character scaleJoin23, Character scaleJoin34, Character scaleJoin45)
    {
        this.code = code;
        this.typeName = typeName;
        this.gross = gross;
        this.plateNum = plateNum;
        this.step = step;
        this.scale1 = scale1;
        this.scale2 = scale2;
        this.scale3 = scale3;
        this.scale4 = scale4;
        this.scale5 = scale5;
        this.scale1Active = scale1Active;
        this.scale2Active = scale2Active;
        this.scale3Active = scale3Active;
        this.scale4Active = scale4Active;
        this.scale5Active = scale5Active;
        this.scaleJoin12 = scaleJoin12;
        this.scaleJoin23 = scaleJoin23;
        this.scaleJoin34 = scaleJoin34;
        this.scaleJoin45 = scaleJoin45;
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

    public Long getScale1()
    {
        return scale1;
    }

    public Long getScale2()
    {
        return scale2;
    }

    public Long getScale3()
    {
        return scale3;
    }

    public Long getScale4()
    {
        return scale4;
    }

    public Long getScale5()
    {
        return scale5;
    }

    public Character getScale1Active()
    {
        return scale1Active;
    }

    public Character getScale2Active()
    {
        return scale2Active;
    }

    public Character getScale3Active()
    {
        return scale3Active;
    }

    public Character getScale4Active()
    {
        return scale4Active;
    }

    public Character getScale5Active()
    {
        return scale5Active;
    }

    public Character getScaleJoin12()
    {
        return scaleJoin12;
    }

    public Character getScaleJoin23()
    {
        return scaleJoin23;
    }

    public Character getScaleJoin34()
    {
        return scaleJoin34;
    }

    public Character getScaleJoin45()
    {
        return scaleJoin45;
    }

    @Override
    public String toString()
    {
        return "WeightConfig{" +
                "code='" + code + '\'' +
                ", typeName='" + typeName + '\'' +
                ", gross=" + gross +
                ", plateNum=" + plateNum +
                ", step=" + step +
                ", scale1=" + scale1 +
                ", scale2=" + scale2 +
                ", scale3=" + scale3 +
                ", scale4=" + scale4 +
                ", scale5=" + scale5 +
                ", scale1Active=" + scale1Active +
                ", scale2Active=" + scale2Active +
                ", scale3Active=" + scale3Active +
                ", scale4Active=" + scale4Active +
                ", scale5Active=" + scale5Active +
                ", scaleJoin12=" + scaleJoin12 +
                ", scaleJoin23=" + scaleJoin23 +
                ", scaleJoin34=" + scaleJoin34 +
                ", scaleJoin45=" + scaleJoin45 +
                '}';
    }
}
