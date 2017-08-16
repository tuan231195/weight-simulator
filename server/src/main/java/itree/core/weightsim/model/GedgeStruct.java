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

        return String.valueOf(begin) + constS +
                constT + "," +
                display + motion +
                "," + sign_flag +
                weight + " " +
                unit + String.valueOf(end);
    }

    public String getWeight()
    {
        return weight;
    }
}
