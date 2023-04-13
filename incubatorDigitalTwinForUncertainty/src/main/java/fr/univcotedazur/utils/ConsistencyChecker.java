package fr.univcotedazur.utils;

import uDataTypes.UBoolean;
import uDataTypes.UReal;

public class ConsistencyChecker {

    public static UBoolean areConsistent(UReal r1, UReal r2, ConfidenceLevel cl){
        //are included one in another
        double r1max = r1.getX() + cl.value * r1.getU();
        double r2max = r2.getX() + cl.value * r2.getU();
        double r1min = r1.getX() - cl.value * r1.getU();
        double r2min = r2.getX() - cl.value * r2.getU();
        if(r1max >= r2max  &&  r1min <= r2min){
            return new UBoolean(true, 1);
        }
        if(r2max >= r1max  &&  r2min <= r1min){
            return new UBoolean(true, 1);
        }

        //are disjoint
        if(r1min >= r2max  ||  r2min >= r1max){
            return new UBoolean(true, 0);
        }

        //otherwise return overlapping percentage
        double union = Math.max(r1max, r2max) - Math.min(r1min, r2min);
        double inter = Math.min(r1max, r2max) - Math.max(r1min, r2min);
        return new UBoolean(true, (inter/union));
    }


    public static UBoolean areConsistent(UReal r1, UReal r2){
        return areConsistent(r1,r2,ConfidenceLevel.NinetyFivePercent);
    }

}
