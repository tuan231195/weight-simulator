package itree.core.weightsim.util;


import itree.core.weightsim.model.GedgeStruct;

public class PacketBuilder
{
    public static GedgeStruct newGedgeStruct(boolean stabled, double weightInKilos)
    {
        double weight = weightInKilos/1000.0;
        String weightStr = String.format("%07.2f", weight);
        char sign = (weight < 0) ? '-' : '+';
        char constS, constT;
        if (stabled)
        {
            constS = 'S';
            constT = 'T';
        }
        else
        {
            constS = 'U';
            constT = 'S';
        }
        return new GedgeStruct(constS, constT, 'G', 'S', sign, weightStr, 't');
    }
}
