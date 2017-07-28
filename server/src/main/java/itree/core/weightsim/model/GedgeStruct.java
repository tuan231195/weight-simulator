package itree.core.weightsim.model;


public class GedgeStruct
{
    private static final char begin = 10;
    private char constS;
    private char constT;
    private char display;
    private char motion;
    private char sign_flag;             // + or -
    private String weight;
    private char unit;
    private static final char end = 13;

    public GedgeStruct(char constS, char constT, char display, char motion, char sign_flag, String weight, char unit)
    {
        this.constS = constS;
        this.constT = constT;
        this.display = display;
        this.motion = motion;
        this.sign_flag = sign_flag;
        this.weight = weight;
        this.unit = unit;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(begin).append(constS)
                .append(constT).append(",")
                .append(display).append(motion)
                .append(",").append(sign_flag)
                .append(weight).append(" ")
                .append(unit).append(end);

        return sb.toString();
    }

    public String getWeight()
    {
        return weight;
    }
}
