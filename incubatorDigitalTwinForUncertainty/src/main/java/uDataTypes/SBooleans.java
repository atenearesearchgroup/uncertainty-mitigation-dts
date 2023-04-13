package uDataTypes;

/** This class contains static versions of the operations for type SBoolean
 *  It is required because Esper (a CEP language) can only invoke static operations
 */

public class SBooleans {
 
    /**
     * Getters (no setters allowed in order to respect canonical form!)
     */
    public static double belief(SBoolean b) {
		return b.belief(); 
	}

    public static double disbelief(SBoolean b) {
		return b.disbelief(); 
	}
    public static double uncertainty(SBoolean b) {
 		return b.uncertainty(); 
 	}

     public static double baseRate(SBoolean b) {
 		return b.baseRate(); 
 	}

   /*********
     * 
     * STATIC Operations
     */
     
     public static double projection(SBoolean b) {
    	 return b.projection();
     }
     
     public SBoolean uncertaintyMaximized(SBoolean b) {
    	 return b.uncertaintyMaximized();
     }

    public static SBoolean uNot(SBoolean b) {
	   return b.not();
   }

    public static SBoolean uAnd(SBoolean b1, SBoolean b2) {
	   return b1.and(b2);
   }
    public static SBoolean uOr(SBoolean b1, SBoolean b2) {
	   return b1.or(b2);
   }
    public static SBoolean uImplies(SBoolean b1, SBoolean b2) {
	   return b1.implies(b2);
   }
    public static SBoolean uXor(SBoolean b1, SBoolean b2) {
	   return b1.xor(b2);
   }
    public static SBoolean uEquivalent(SBoolean b1, SBoolean b2) {
	   return b1.equivalent(b2);
   }

    public static boolean equals(SBoolean b1, SBoolean b2) {
	   return b1.equals(b2);
   }

	public static double projectiveDistance(SBoolean b1, SBoolean b2) { // projectiveDistance
		return b1.projectiveDistance(b2);
	}

	public double conjunctiveCertainty(SBoolean b1, SBoolean b2) {
		return b1.conjunctiveCertainty(b2);
	}
	
	public double degreeOfConflict(SBoolean b1, SBoolean b2) {
		return b1.degreeOfConflict(b2);
	}
	
	public SBoolean deduceY(SBoolean x, SBoolean yGivenX, SBoolean yGivenNotX) { // DEDUCTION
		return x.deduceY(yGivenX,yGivenNotX);
	}


	/******
	 * STATIC Conversions
	 */
	public static String toString(SBoolean b) {
        return b.toString();
	}

	public static UBoolean toUBoolean(SBoolean b) {
        return b.toUBoolean();
	}

}

