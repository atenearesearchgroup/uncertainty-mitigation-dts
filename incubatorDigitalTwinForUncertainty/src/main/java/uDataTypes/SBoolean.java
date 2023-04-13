package uDataTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


// BINOMIAL OPINIONS
public class SBoolean implements Cloneable, Comparable<SBoolean> {
	
    public static final SBoolean UNCERTAIN = new SBoolean(0.0D, 0.0D, 1.0D, 0.5D);

    protected double b; // belief mass: degree of belief that self is TRUE
	protected double d; //  disbelief mass: degree of belief that self is FALSE
	protected double u; //  uncertainty mass: amount of uncommitted belief  
	protected double a; //  base rate: prior probability of self being TRUE

	protected double relativeWeight = 1.0D; // For fusion operations

	
    /**
     * Constructors 
     */
    public SBoolean() {
        this.b = 1.0D; this.d = 0.0D; this.u = 0.0D; this.a = 1.0D; // Dogmatic TRUE
        this.relativeWeight = 1.0D;
    }

    public SBoolean(boolean b) {
        if (b) {this.b = 1.0D; this.d = 0.0D; this.u = 0.0D; this.a = 1.0D;} // Dogmatic TRUE
        else {this.b = 0.0D; this.d = 1.0D; this.u = 0.0D; this.a = 0.0D;} // Dogmatic FALSE
        this.relativeWeight = 1.0D;

    }
    
    private double adjust(double value) {
    	return (double)Math.round(value * 1000000.0D) / 1000000.0D;
    }

    public SBoolean(double b, double d, double u, double a) {
    	this.b = adjust(b); 
    	this.d = adjust(d); 
    	this.u = adjust(u); 
    	this.a = adjust(a);
        this.relativeWeight = 1.0D;
    	if (Math.abs(this.b+this.d+this.u-1.0D)>0.001D ||
    		this.b<0.0D || this.d<0.0D || this.u<0.0D || this.a<0.0D || 
    		this.b>1.0D || this.d>1.0D || this.u>1.0D || this.a>1.0D ) 
    		throw new IllegalArgumentException("SBoolean constructor: Invalid parameters. b:"+this.b+",d:"+this.d+",u:"+this.u+",a:"+this.a);
    }
	
    private SBoolean(double b, double d, double u, double a, double relativeWeight) {
    	this.b = adjust(b); 
    	this.d = adjust(d); 
    	this.u = adjust(u); 
    	this.a = adjust(a);
    	this.relativeWeight = adjust(relativeWeight);
    	if (Math.abs(this.b+this.d+this.u-1.0)>0.001 ||
        		this.b<0.0D || this.d<0.0D || this.u<0.0D || this.a<0.0D || 
        		this.b>1.0D || this.d>1.0D || this.u>1.0D || this.a>1.0D ) 
    		throw new IllegalArgumentException("SBoolean constructor with relative weight: Invalid parameters. b:"+this.b+",d:"+this.d+",u:"+this.u+",a:"+this.a);
    }
	
    public SBoolean(String b, String d, String u, String a) { //creates an UBoolean from two strings representing (x,u).
       	this.b = adjust(Double.parseDouble(b)); 
    	this.d = adjust(Double.parseDouble(d)); 
    	this.u = adjust(Double.parseDouble(u)); 
    	this.a = adjust(Double.parseDouble(a));
        this.relativeWeight = 1.0D;

    	if (Math.abs(this.b+this.d+this.u-1.0)>0.001 ||
        		this.b<0.0D || this.d<0.0D || this.u<0.0D || this.a<0.0D || 
        		this.b>1.0D || this.d>1.0D || this.u>1.0D || this.a>1.0D ) 
    		throw new IllegalArgumentException("SBoolean constructor with strings: Invalid parameters. b:"+this.b+",d:"+this.d+",u:"+this.u+",a:"+this.a);
    }
    
    public SBoolean(UBoolean b) { // type embedding -- without uncertainty
       	this.b = adjust(b.getC()); 
    	this.d = 1.0D - this.b; 
    	this.u = 0.0D; 
    	this.a = this.b;
        this.relativeWeight = 1.0D;
    }
   
    /**
     * getters (no public setters in order to respect well-formed rules)
     */
    public double belief() {return this.b;} 
    public double disbelief() {return this.d;} 
    public double uncertainty() {return this.u;} 
    public double baseRate() {return this.a;} 
    
    
    public double getRelativeWeight() {
        return this.isDogmatic()? this.relativeWeight : 0.0D;
    }

    private double getRelativeWeight(SBoolean opinion) {
           return adjust(this.relativeWeight / opinion.relativeWeight);
    }
    
    private void setRelativeWeight(double weight) { this.relativeWeight = adjust(weight); }

 
    /*********
     * Auxiliary operations
     */

    public double projection() { // projected probability
    	return adjust(this.b + this.a*this.u);
    }
    
	public double projectiveDistance(SBoolean s) { // projectiveDistance
		return adjust(Math.abs(this.projection()-s.projection())); // /2
	}

	public double conjunctiveCertainty(SBoolean s) {
		return adjust((1.0D-this.u)*(1.0D-s.u));
	}
	
	public double degreeOfConflict(SBoolean s) {
		return adjust(this.projectiveDistance(s)*this.conjunctiveCertainty(s));
	}

	public SBoolean increasedUncertainty() {
	    if (this.isVacuous()) return this.clone();
	    
		double sqrt_u = Math.sqrt(this.uncertainty());
	    double k = 1.0D - (sqrt_u - this.uncertainty()) / (this.belief() + this.disbelief());
        double brBelief = this.belief() * k;
	    double brUncertainty = sqrt_u;
	    double brDisbelief = this.disbelief() * k;
        return new SBoolean(brBelief, brDisbelief, brUncertainty, this.baseRate());
	}
	
	public boolean isAbsolute() {
	    return (this.belief() == 1.0D) || (this.disbelief() == 1.0D);
	}

	public boolean isVacuous() {
	        return this.uncertainty() == 1.0D;
	}

	public boolean isCertain(double threshold) {
	    return !isUncertain(threshold);
    }

	public boolean isDogmatic() {
	    return this.uncertainty() == 0.0D;
	}

	public boolean isMaximizedUncertainty() {
	    return (this.disbelief() == 0.0D) || (this.belief() == 0.0D);
	}

	public boolean isUncertain(double threshold) {
	    return 1.0D - this.uncertainty() < threshold;
	}

	public SBoolean uncertainOpinion() {
	    return this.uncertaintyMaximized();
	}
	
    public final double certainty()
    {
        if (this.uncertainty() == (0.0D / 0.0D)) return (0.0D / 0.0D);
        return 1.0D - this.uncertainty();
    }

    
	/**
	 * Returns the subjective opinion that results from adjusting the base rate to be the one given in the
	 * parameter. This operation is useful when we need to apply an opinion on a UBoolean value, whose
	 * confidence will become the new base rate of the resulting opinion. 
	 * @param x UBoolean, whose confidence specifies the new baseRate
	 * @return A SBoolean value whose base rate is the one given in the parameter, the uncertainty is 
	 * maintained, and the degree of belief is adjusted proportionally to the ratio (b/a) of the 
	 * original SBoolean. If the new base rate is the same, the SBoolean does not change. If the new 
	 * baseRate is 0 (false), the degree of belief of the new SBoolean is 0 too, and the previous belief is 
	 * transferred to the degree of disbelief.
	 */
	public SBoolean applyOn(UBoolean x) {
	   double baseRate = x.getC();
	   if ((baseRate < 0.0D) || (baseRate > 1.0D)) {
	       throw new IllegalArgumentException("applyOn(): baseRate must be between 0 and 1");
	   }
	   if (this.baseRate()==baseRate) return this.clone();
	   double uT = this.uncertainty();
	   if (uT==1.0D) return new SBoolean(0.0,0.0,1.0D,baseRate); // we only change the base rate...
	   
	   double bT;
	   if (this.baseRate()==0.0D) { //then baseRate != 0.0D
           bT  = this.belief() + this.disbelief()*baseRate; //OK
       } else { //this.baseRate() != 0.0D
    	   bT  = Math.min(baseRate*this.belief()/this.baseRate(),(1.0D-uT));
       }
       return new SBoolean(bT,1.0D-bT-uT,uT,baseRate);
	}

	/**
	 * Dogmatic opinions are opinions with complete certainty (i.e., uncertainty = 0).
	 *
	 * @param projection
	 * @param baseRate a-priori probability
	 * @return
	 */
	public static SBoolean createDogmaticOpinion(double projection, double baseRate) {
	   if ((projection < 0.0D) || (projection > 1.0D) || (baseRate < 0.0D) || (baseRate > 1.0D)) {
	       throw new IllegalArgumentException("Create Dogmatic Opinion: Projections and baseRates should be between 0 and 1");
	   }
	   return new SBoolean(projection, 1.0D - projection, 0.0D, baseRate);
	}

	/**
	 * Vacuous opinions have an uncertainty of 1.
	 * @param projection
	 * @return
	 */
	public static SBoolean createVacuousOpinion(double projection) {
	   if ((projection < 0.0D) || (projection > 1.0D)) {
	       throw new IllegalArgumentException("CreateVacuousOpinion: Projection must be between 0 and 1. p="+projection);
	   }
	   return new SBoolean(0.0D, 0.0D, 1.0D, projection);
	}
	
	
    /*********
     * Type Operations
     */

    public SBoolean not() {
	   SBoolean result = new SBoolean(
			   this.d,
			   this.b,
			   this.u,
			   1.0D-this.a,
			   this.relativeWeight);
	   return result;
    }

	public SBoolean and(SBoolean s) { // assumes independent variables

		if (this==s) return this.clone(); // x and x = x
		
		double b = this.b*s.b + (this.a*s.a==1.0D? 0.0D: ((1.0D-this.a)*s.a*this.b*s.u+this.a*(1.0D-s.a)*this.u*s.b)/(1.0D-this.a*s.a));
		double d = this.d + s.d- this.d*s.d;
		SBoolean result = new SBoolean(
				b, //this.b*s.b + (this.a*s.a==1.0D? 0.0D: ((1.0D-this.a)*s.a*this.b*s.u+this.a*(1.0D-s.a)*this.u*s.b)/(1.0D-this.a*s.a)),
				d, //this.d + s.d- this.d*s.d,
				1-d-b, //this.u*s.u + (this.a*s.a==1.0D? 0.0D: ((1.0D-s.a)*this.b*s.u+(1.0D-this.a)*this.u*s.b)/(1.0D-this.a*s.a)),
				this.a*s.a,
				this.getRelativeWeight() + s.getRelativeWeight()
		);
		return result;
	}
	
	public SBoolean or(SBoolean s) {// assumes independent variables

		if (this==s) return this.clone(); // x or x
		
		double b = this.b + s.b - this.b*s.b;
		double d = this.d*s.d + (this.a + s.a == this.a*s.a? 0.0D: (this.a*(1-s.a)*this.d*s.u+s.a*(1-this.a)*this.u*s.d)/(this.a + s.a - this.a*s.a));
		SBoolean result = new SBoolean(
				b, //this.b + s.b - this.b*s.b,
				d, //this.d*s.d + (this.a + s.a == this.a*s.a? 0: (this.a*(1-s.a)*this.d*s.u+s.a*(1-this.a)*this.u*s.d)/(this.a + s.a - this.a*s.a)),
				1-b-d, //this.u*s.u + (this.a + s.a == this.a*s.a? 0: (s.a*this.d*s.u+this.a*this.u*s.d)/(this.a + s.a - this.a*s.a)),
				this.a + s.a - this.a*s.a,
				this.getRelativeWeight() + s.getRelativeWeight() 
		);
		return result;
	}

	public SBoolean implies(SBoolean s) {

		return this.not().or(s); // this is to be consistent with UBoolean, because in Subjective Logic this is not the case...
	}

    public SBoolean equivalent(SBoolean s) {
		// return this.implies(b).and(b.implies(this));
		 return this.xor(s).not(); 	
	}

	public SBoolean xor(SBoolean s) {
		SBoolean result = new SBoolean(
				Math.abs(this.b - s.b), //this.b*(1.0-s.b)+(1.0-this.b)*s.b,
				1.0D - Math.abs(this.b - s.b) - this.u*s.u, //1.0-(this.b*(1.0-s.b)+(1.0-this.b)*s.b)-this.u*s.u,
				this.u*s.u,
				Math.abs(this.a - s.a), // this.a*(1.0-s.a)+(1.0-this.a)*s.a
				this.getRelativeWeight() + s.getRelativeWeight()
		);
		 return result;
		//return this.equivalent(b).not();
	}

	public SBoolean uncertaintyMaximized() { // Returns the equivalent SBoolean with maximum uncertainty. 
		 // The dual operation is toUBoolean, which returns the equivalent SBoolean, with u==0
		//return this.increasedUncertainty();
		// Replaced by another version
		
		double p = this.projection();
		// Extreme cases
		if ((this.a == 1.0D) && (p==1.0D)) return new SBoolean(0.0D,0.0D,1.0D,this.a,this.getRelativeWeight());
		if ((this.a == 1.0D) && (this.u==1.0D)) return new SBoolean(0.0D,0.0D,1.0D,this.a,this.getRelativeWeight());
		if (this.a == 0.0D && (this.b==0.0D)) return  new SBoolean(0.0D,0.0D,1.0D,this.a,this.getRelativeWeight());
		// Normal cases
		if (p < this.a) 
			return new SBoolean(0.0D, 1.0D - (p/this.a), p/this.a, this.a,this.getRelativeWeight());
		return new SBoolean((p-this.a)/(1.0D-this.a), 0.0D, (1.0D-p)/(1.0D-this.a), this.a,this.getRelativeWeight());	
		
		}

	public SBoolean deduceY(SBoolean yGivenX, SBoolean yGivenNotX) { // DEDUCTION: returns Y, acting "this" as X
		SBoolean y = new SBoolean();
		double px = this.projection();
		double K;
		y.a = (yGivenX.u+yGivenNotX.u < 2.0D)? (this.a*yGivenX.b+(1.0D-this.a)*yGivenNotX.b)/(1.0D-this.a*yGivenX.u-(1.0D-this.a)*yGivenNotX.u) : yGivenX.a;
		double pyxhat = yGivenX.b*this.a + yGivenNotX.b*(1-this.a)+ y.a*(yGivenX.u*this.a+yGivenNotX.u*(1-this.a));
		double bIy = this.b*yGivenX.b+this.d*yGivenNotX.b+this.u*(yGivenX.b*this.a+yGivenNotX.b*(1.0D-this.a));
		double dIy = this.b*yGivenX.d+this.d*yGivenNotX.d+this.u*(yGivenX.d*this.a+yGivenNotX.d*(1.0D-this.a));
		double uIy = this.b*yGivenX.u+this.d*yGivenNotX.u+this.u*(yGivenX.u*this.a+yGivenNotX.u*(1.0D-this.a));
		// case I
		//if (((yGivenX.b>yGivenNotX.b)&&(yGivenX.d>yGivenNotX.d))||((yGivenX.b<=yGivenNotX.b)&&(yGivenX.d<=yGivenNotX.d))) 
		K=0.0D;

		// case II.A.1
		if ((yGivenX.b>yGivenNotX.b)&&(yGivenX.d<=yGivenNotX.d)&&
				(pyxhat <= (yGivenNotX.b+y.a*(1.0D-yGivenNotX.b-yGivenX.d))) &&
				(px<=this.a) ) 
			{K=(this.a*this.u*(bIy-yGivenNotX.b))/((this.b+this.a*this.u)*y.a);}
		// case II.A.2
		if ((yGivenX.b>yGivenNotX.b)&&(yGivenX.d<=yGivenNotX.d)&&
				(pyxhat <= (yGivenNotX.b+y.a*(1.0D-yGivenNotX.b-yGivenX.d))) &&
				(px>this.a) ) 
			{K=(this.a*this.u*(dIy-yGivenX.d)*(yGivenX.b-yGivenNotX.b))/((this.d+(1.0D-this.a)*this.u)*y.a*(yGivenNotX.d-yGivenX.d));}
		// case II.B.1
		if ((yGivenX.b>yGivenNotX.b)&&(yGivenX.d<=yGivenNotX.d)&&
				(pyxhat > (yGivenNotX.b+y.a*(1.0D-yGivenNotX.b-yGivenX.d))) &&
				(px<=this.a) ) 
			{K=((1.0D-this.a)*this.u*(bIy-yGivenNotX.b)*(yGivenNotX.d-yGivenX.d))/((this.b+this.a*this.u)*(1.0D-y.a)*(yGivenX.b-yGivenNotX.b));}
		// case II.B.2
		if ((yGivenX.b>yGivenNotX.b)&&(yGivenX.d<=yGivenNotX.d)&&
				(pyxhat > (yGivenNotX.b+y.a*(1.0D-yGivenNotX.b-yGivenX.d))) &&
				(px>this.a) ) 
			{K=((1.0D-this.a)*this.u*(dIy-yGivenX.d))/((this.d+(1.0D-this.a)*this.u)*(1.0D-y.a));}

		// case III.A.1
		if ((yGivenX.b<=yGivenNotX.b)&&(yGivenX.d>yGivenNotX.d)&&
				(pyxhat <= (yGivenX.b+y.a*(1.0D-yGivenNotX.b-yGivenX.d))) &&
				(px<=this.a) ) 
			{K=((1.0D-this.a)*this.u*(dIy-yGivenNotX.d)*(yGivenNotX.b-yGivenX.b))/((this.b+this.a*this.u)*y.a*(yGivenX.d-yGivenNotX.d));}
		
		// case III.A.2
		if ((yGivenX.b<=yGivenNotX.b)&&(yGivenX.d>yGivenNotX.d)&&
				(pyxhat <= (yGivenX.b+y.a*(1.0D-yGivenX.b-yGivenNotX.d))) &&
				(px>this.a) ) 
			{K=((1.0D-this.a)*this.u*(bIy-yGivenX.d))/((this.d+(1.0D-this.a)*this.u)*y.a);}

		// case III.B.1
		if ((yGivenX.b<=yGivenNotX.b)&&(yGivenX.d>yGivenNotX.d)&&
				(pyxhat > (yGivenX.b+y.a*(1.0D-yGivenX.b-yGivenNotX.d))) &&
				(px<=this.a) ) 
			{K=(this.a*this.u*(dIy-yGivenNotX.b))/((this.b+this.a*this.u)*(1.0D-y.a));}

		// case III.B.2
		if ((yGivenX.b<=yGivenNotX.b)&&(yGivenX.d>yGivenNotX.d)&&
				(pyxhat > (yGivenX.b+y.a*(1.0D-yGivenX.b-yGivenNotX.d))) &&
				(px>this.a) ) 
			{K=(this.a*this.u*(bIy-yGivenX.b)*(yGivenX.d-yGivenNotX.d))/((this.d+(1.0D-this.a)*this.u)*(1.0D-y.a)*(yGivenNotX.b-yGivenX.b));}
		
		y.b = adjust(bIy - y.a*K);
		y.d = adjust(dIy - (1.0D-y.a)*K);
		y.u = adjust(uIy + K);
        y.setRelativeWeight(yGivenX.getRelativeWeight() + yGivenNotX.getRelativeWeight());
		return y;
	}
	
	/************************************
	 *  FUSION OPERATIONS 
	 *  These implementations are based in those given in https://github.com/vs-uulm/subjective-logic-java
	 *  */
	
	/**
	 * This method implements constraint belief fusion (CBF). It uses the binary operation and iterates 
	 * over the collection of opinions. This operation is associative if the base rate is the same for all 
	 * opinions, otherwise the fused base rate distribution could be the confidence-weighted
	 * average base rate (see Josang's book). The neutral element is the vacuous opinion.
     *
     * @param opinions a collection of opinions from different sources.
     * @return a new SBoolean that represents the fused evidence.
     * @throws IllegalArgumentException
	 */
    public static SBoolean beliefConstraintFusion(Collection<SBoolean> opinions) {
        if (opinions.contains(null) || opinions.size() < 2)
            throw new IllegalArgumentException("BCF: Cannot fuse null opinions, or only one opinion was passed");
        SBoolean bcf = null;
        
        for (SBoolean so : opinions) {
            if (bcf == null) bcf = so; // first time
            else bcf = bcf.bcFusion(so);
        }
        return bcf;
    }

	 /**
     * This method implements MIN fusion. This takes the minimum, i.e., returns the opinion with 
     * the lowest probability of being true, meaning the lowest projected probability P(X=x).
     *
     * @param opinions a collection of opinions from different sources.
     * @return a new SBoolean that represents the fused evidence.
     * @throws IllegalArgumentException
     */
    public static SBoolean minimumBeliefFusion(Collection<SBoolean> opinions) {
        if (opinions.contains(null) || opinions.size() < 2)
            throw new IllegalArgumentException("MBF: Cannot fuse null opinions, or only one opinion was passed");

        SBoolean min = null;
        for (SBoolean so : opinions) {
            if (min == null) min = so;
            min = min.min(so);
        }
        return min.clone();
    }

    /**
     * This method implements MAJORITY fusion. This returns a new dogmatic opinion that specifies the 
     * decision of the majority.
     * If the majority is tied, a vacuous opinion is returned.
     * It is assumed that the base rates of all opinions are equal.
     * For this operation, opinions that are undecided (projections equals base rate) are ignored.
     *
     * @param opinions a collection of opinions from different sources.
     * @return a new SBoolean that represents the fused evidence.
     * @throws IllegalArgumentException
     */
    public static SBoolean majorityBeliefFusion(Collection<SBoolean> opinions) {
        if (opinions.contains(null) || opinions.size() < 2)
            throw new IllegalArgumentException("MajBF: Cannot fuse null opinions, or only one opinion was passed");
        int pos=0,neg=0;
        for (SBoolean so: opinions) {
            if(so.projection() < so.a)
                neg++;
            else if (so.projection() > so.a)
                pos++;
        }
        if(pos>neg) return new SBoolean(1D,0D,0D,0.5D); 		// true
        else if(pos<neg) return new SBoolean(0D,1D,0D,0.5D); 	// false
        else return new SBoolean(0D,0D,1D,0.5D); 				// uncertain
    }
    
    /** 
     * This method implements AVERAGE fusion.
     *
     * @param opinions a collection of opinions from different sources.
     * @return a new SBoolean that represents the fused evidence.
     * @throws IllegalArgumentException
     */

   public static SBoolean averageBeliefFusion(Collection<SBoolean> opinions) {
	   
	   //implemented using equation (32) of https://folk.uio.no/josang/papers/JWZ2017-FUSION.pdf 
	   // because the Josang's book has a problem.
	   
       if (opinions == null || opinions.contains(null) || opinions.isEmpty())
           throw new IllegalArgumentException("AVF: Cannot average null opinions");

        double b = 0.0D; double u=0.0D; double a = 0.0D; 
        double PU = 1.0D; //product of all uncertainties
        int count = 0;
   
        double oBelief;
        double oAtomicity;
        double oUncertainty;
        double oDisbelief;
      
        for (SBoolean o : opinions)  PU *= o.uncertainty(); // product of all uncertainties
       
        // case I: all opinions with uncertainty > 0:
        if (PU!=0) {
         	for (SBoolean o: opinions) {
        		u += PU/o.uncertainty();
        		b += o.belief()*PU/o.uncertainty();
        		a += o.baseRate();
        	}
            oBelief = b / u;
            oAtomicity = a / opinions.size();
            oUncertainty = opinions.size()*PU/u;
            oDisbelief = 1.0D - oBelief - oUncertainty;
            return new SBoolean(oBelief, oDisbelief, oUncertainty, oAtomicity);
        }
        else { // there is at least one opinion with uncertainty = 0. Then we only consider these opinions
        	for (SBoolean o: opinions) {
        		if (o.uncertainty()==0.0D) {
        			b += o.belief();
        			a += o.baseRate();
        			count++;
        		}
        	}
            oBelief = b / count;
            oAtomicity = a / count;
            oUncertainty = 0.0D;
            oDisbelief = 1.0D - oBelief - oUncertainty;
            return new SBoolean(oBelief, oDisbelief, oUncertainty, oAtomicity);
        }
        
        /* OLD VERSION
        int count = 0;
        double b = 0.0D; double a = 0.0D; double p = 0.0D;
        for (SBoolean opinion : opinions) {
            if (opinion != null)
            {
            	SBoolean x = opinion.clone();
                count++;
                b += x.belief();
                a += x.baseRate();
                p += x.belief() + x.baseRate() * x.uncertainty();
            }
        }
        if (count == 0) {
            throw new IllegalArgumentException("Opinions must not be empty");
        }
        oBelief = b / count;
        oAtomicity = a / count;
        oUncertainty = (p / count - oBelief) / oAtomicity;
        oDisbelief = 1.0D - oBelief - oUncertainty;

        return new SBoolean(oBelief, oDisbelief, oUncertainty, oAtomicity);
        */
    }
   
   
 

   /**
    * This method implements cumulative belief fusion (CBF) for multiple sources, as discussed in the corrected
    * version of <a href="https://folk.uio.no/josang/papers/JWZ2017-FUSION.pdf">a FUSION 2017 paper by Josang et al.</a>
    *
    * As discussed in the book, cumulative fusion is useful in scenarios where opinions from multiple sources 
    * are combined, where each source is relying on independent (in the statistical sense) evidence.
    * 
    * 
    * @param opinions a collection of opinions from different sources.
    * @return a new SBoolean that represents the fused evidence based on evidence accumulation.
    * @throws IllegalArgumentException
    */
   public static SBoolean cumulativeBeliefFusion(Collection<SBoolean> opinions) {
       //handle edge cases
       if (opinions == null || opinions.contains(null) || opinions.isEmpty())
           throw new IllegalArgumentException("aCBF: Cannot average null opinions");
 
       if (opinions.size() == 1){
           return opinions.iterator().next().clone();
       }

       //fusion as defined by Josang
       double resultBelief, resultDisbelief, resultUncertainty, resultRelativeWeight = 0, resultAtomicity = -1;

       Collection<SBoolean> dogmatic = new ArrayList<>(opinions.size());
       Iterator<SBoolean> it = opinions.iterator();
       boolean first = true;
       while(it.hasNext()) {
    	   SBoolean o = it.next();
           if(first) {
               resultAtomicity = o.baseRate();
               first = false;
           }
           //dogmatic iff uncertainty is zero.
           if (o.uncertainty() == 0.0D)
               dogmatic.add(o);
       }

       if(dogmatic.isEmpty()){
           //there are no dogmatic opinions -- case I/Eq16 of 10.23919/ICIF.2017.8009820
           double productOfUncertainties = opinions.stream().mapToDouble(o -> o.uncertainty()).reduce(1.0D, (acc, u) -> acc * u);

           double numerator = 0.0D;
           double beliefAccumulator = 0.0D;
           double disbeliefAccumulator = 0.0D;

           //this computes the top and bottom sums in Eq16, but ignores the - (N-1) * productOfUncertainties in the numerator (see below)
           for (SBoolean o : opinions) {
               //productWithoutO = product of uncertainties without o's uncertainty
               //this can be rewritten:
               //prod {C_j != C } u^{C_j} = (u^C)^-1 * prod{C_j} u^{C_j} = 1/(u^C) * prod{C_j} u^{C_j}
               //so instead of n-1 multiplications, we only need a division
               double productWithoutO = productOfUncertainties / o.uncertainty();

               beliefAccumulator = beliefAccumulator + productWithoutO * o.belief();
               disbeliefAccumulator = disbeliefAccumulator + productWithoutO * o.disbelief();
               numerator = numerator + productWithoutO;
           }

           //this completes the numerator:
           numerator = numerator - (opinions.size() - 1) * productOfUncertainties;

           resultBelief = beliefAccumulator / numerator;
           resultDisbelief = disbeliefAccumulator / numerator;
           resultUncertainty = productOfUncertainties / numerator;

           resultRelativeWeight = 0.0D;
       } else {
           //at least 1 dogmatic opinion
           //note: this computation assumes that the relative weight represents how many opinions have been fused into that opinion.
           //for a normal multi-source fusion operation, this should be 1, in which case the gamma's in Eq17 are 1/N as noted in the text (i.e., all opinions count equally)
           //however, this formulation also allows partial fusion beforehand, by "remembering" the amount of dogmatic (!) opinions in o.relativeWeight.

           double totalWeight = dogmatic.stream().mapToDouble( o -> o.getRelativeWeight()).sum();

           resultBelief = dogmatic.stream().mapToDouble(o-> o.getRelativeWeight()/totalWeight * (o).belief()).sum();

           resultDisbelief = dogmatic.stream().mapToDouble(o-> o.getRelativeWeight()/totalWeight * (o).disbelief()).sum();

           resultUncertainty = 0.0D;

           resultRelativeWeight = totalWeight;
       }

       SBoolean result = new SBoolean(resultBelief, resultDisbelief, resultUncertainty, resultAtomicity,resultRelativeWeight);
       return result;
   }

   /**
    * This method implements epistemic cumulative belief fusion (eCBF) for multiple sources, 
    * as discussed in the corrected
    * version of <a href="https://folk.uio.no/josang/papers/JWZ2017-FUSION.pdf">a FUSION 2017 paper by Josang et al.</a>
    *
    * eCBF is useful when the opinions represent knowledge, and not observations, and therefore they are
    * uncertainty maximized. As in the CBF, each source is supposed to be relying on independent 
    * (in the statistical sense) evidence (in this case, knowledge).
    * 
    * 
    * @param opinions a collection of opinions from different sources.
    * @return a new SBoolean that represents the fused evidence based on evidence accumulation.
    * @throws IllegalArgumentException
    */
   public static SBoolean epistemicCumulativeBeliefFusion(Collection<SBoolean> opinions) {
       //handle edge cases
       if (opinions == null || opinions.contains(null) || opinions.isEmpty())
           throw new IllegalArgumentException("eCBF: Cannot average null opinions");
 
       if (opinions.size() == 1){
           return opinions.iterator().next().clone();
       }
       
       //generate uncertaintyMaximized() versions of opinions
       //Collection<SBoolean> opinions = new ArrayList<>(ops.size());
       //for (SBoolean o:ops) {
       //	   opinions.add(o.uncertaintyMaximized());
       // }
 
       //fusion as defined by Josang
       double resultBelief, resultDisbelief, resultUncertainty, resultRelativeWeight = 0.0D, resultAtomicity = -1.0D;

       Collection<SBoolean> dogmatic = new ArrayList<>(opinions.size());
       Iterator<SBoolean> it = opinions.iterator();
       boolean first = true;
       while(it.hasNext()) {
    	   SBoolean o = it.next(); //.uncertaintyMaximized();
           if(first) {
               resultAtomicity = o.baseRate();
               first = false;
           }
           //dogmatic iff uncertainty is zero.
           if (o.uncertainty() == 0.0D)
               dogmatic.add(o);
       }

       if(dogmatic.isEmpty()){
           //there are no dogmatic opinions -- case I/Eq16 of 10.23919/ICIF.2017.8009820
           double productOfUncertainties = opinions.stream().mapToDouble(o -> o.uncertainty()).reduce(1.0D, (acc, u) -> acc * u);

           double numerator = 0.0D;
           double beliefAccumulator = 0.0D;
           double disbeliefAccumulator = 0.0D;

           //this computes the top and bottom sums in Eq16, but ignores the - (N-1) * productOfUncertainties in the numerator (see below)
           for (SBoolean o : opinions) {
               //productWithoutO = product of uncertainties without o's uncertainty
               //this can be rewritten:
               //prod {C_j != C } u^{C_j} = (u^C)^-1 * prod{C_j} u^{C_j} = 1/(u^C) * prod{C_j} u^{C_j}
               //so instead of n-1 multiplications, we only need a division
               double productWithoutO = productOfUncertainties / o.uncertainty();

               beliefAccumulator = beliefAccumulator + productWithoutO * o.belief();
               disbeliefAccumulator = disbeliefAccumulator + productWithoutO * o.disbelief();
               numerator = numerator + productWithoutO;
           }

           //this completes the numerator:
           numerator = numerator - (opinions.size() - 1) * productOfUncertainties;

           resultBelief = beliefAccumulator / numerator;
           resultDisbelief = disbeliefAccumulator / numerator;
           resultUncertainty = productOfUncertainties / numerator;

           resultRelativeWeight = 0.0D;
       } else {
           //at least 1 dogmatic opinion
           //note: this computation assumes that the relative weight represents how many opinions have been fused into that opinion.
           //for a normal multi-source fusion operation, this should be 1, in which case the gamma's in Eq17 are 1/N as noted in the text (i.e., all opinions count equally)
           //however, this formulation also allows partial fusion beforehand, by "remembering" the amount of dogmatic (!) opinions in o.relativeWeight.

           double totalWeight = dogmatic.stream().mapToDouble( o -> o.getRelativeWeight()).sum();

           resultBelief = dogmatic.stream().mapToDouble(o-> o.getRelativeWeight()/totalWeight * (o).belief()).sum();

           resultDisbelief = dogmatic.stream().mapToDouble(o-> o.getRelativeWeight()/totalWeight * (o).disbelief()).sum();

           resultUncertainty = 0.0D;

           resultRelativeWeight = totalWeight;
       }

       SBoolean result = new SBoolean(resultBelief, resultDisbelief, resultUncertainty, resultAtomicity,resultRelativeWeight);
       return result.uncertaintyMaximized();
   }

   /**
    * This method implements weighted belief fusion (WBF) for multiple sources, as discussed in a FUSION 2018 paper by van der Heijden et al.
    *
    * As discussed in the book, WBF is intended to represent the confidence-weighted averaging of evidence.
    * Similar to AverageBF, it is useful when dependence between sources is assumed. However, WBF introduces 
    * additional weights to increase the significance of sources that possess high certainty. 
    *
    * @param opinions a collection of opinions from different sources.
    * @return a new SubjectiveOpinion that represents the fused evidence based on confidence-weighted averaging of evidence.
    * @throws IllegalArgumentException
    */
   public static SBoolean weightedBeliefFusion(Collection<SBoolean> opinions) {
       if (opinions == null || opinions.contains(null) || opinions.isEmpty())
           throw new IllegalArgumentException("WBF: Cannot average null opinions");

       if (opinions.size() == 1) {
           return opinions.iterator().next().clone();
       }

       double resultBelief, resultDisbelief, resultUncertainty, resultRelativeWeight = 0, resultAtomicity;

       Collection<SBoolean> dogmatic = new ArrayList<>(opinions.size());
       Iterator<SBoolean> it = opinions.iterator();
       while(it.hasNext()) {
    	   SBoolean o = it.next();
           //dogmatic iff uncertainty is zero.
           if (o.uncertainty() == 0) 
               dogmatic.add(o);
       }

       if (dogmatic.isEmpty() && opinions.stream().anyMatch(o -> o.certainty() > 0)) {
           //Case 1: no dogmatic opinions, at least one non-vacuous opinion
           double productOfUncertainties = opinions.stream().mapToDouble(o -> o.uncertainty()).reduce(1.0D, (acc, u) -> acc * u);
           double sumOfUncertainties = opinions.stream().mapToDouble(o -> o.uncertainty()).sum();

           double numerator = 0.0D;
           double beliefAccumulator = 0.0D;
           double disbeliefAccumulator = 0.0D;
           double atomicityAccumulator = 0.0D;

           for (SBoolean o : opinions) {
               //prod = product of uncertainties without o's uncertainty
               double prod = productOfUncertainties / o.uncertainty();

               //recall certainty = 1 - uncertainty
               beliefAccumulator = beliefAccumulator + prod * o.belief() * o.certainty();
               disbeliefAccumulator = disbeliefAccumulator + prod * o.disbelief() * o.certainty();
               atomicityAccumulator = atomicityAccumulator + o.baseRate() * o.certainty();
               numerator = numerator + prod;
           }

           numerator = numerator - opinions.size() * productOfUncertainties;

           resultBelief = beliefAccumulator / numerator;
           resultDisbelief = disbeliefAccumulator / numerator;
           resultUncertainty = (opinions.size() - sumOfUncertainties) * productOfUncertainties / numerator;
           resultAtomicity = atomicityAccumulator / (opinions.size() - sumOfUncertainties);
       } else if (opinions.stream().allMatch(o -> o.uncertainty() == 1)) {
           //Case 3 -- everything is vacuous
           resultBelief = 0;
           resultDisbelief = 0;
           resultUncertainty = 1;
           boolean first = true;

           //all confidences are zero, so the weight for each opinion is the same -> use a plain average for the resultAtomicity
           resultAtomicity = 0;
           for (SBoolean o : opinions) {
               if (first) {
                   resultAtomicity = resultAtomicity + o.baseRate();
                   first = false;
               }
           }
           resultAtomicity = resultAtomicity / ((double)opinions.size());

       } else {
           //Case 2 -- dogmatic opinions are involved
           double totalWeight = dogmatic.stream().mapToDouble( o -> o.getRelativeWeight()).sum();

           resultBelief = dogmatic.stream().mapToDouble(o-> o.getRelativeWeight()/totalWeight * o.belief()).sum();

           resultDisbelief = dogmatic.stream().mapToDouble(o-> o.getRelativeWeight()/totalWeight * o.disbelief()).sum();

           resultUncertainty = 0.0D;

           resultRelativeWeight = totalWeight;

           //note: the for loop below will always set resultAtomicity correctly.
           resultAtomicity = -1;
           boolean first = true;
           for(SBoolean o : opinions){
               if (first) {
                   resultAtomicity = o.baseRate();
                   first = false;
               }
           }
       }

       SBoolean result = new SBoolean(resultBelief, resultDisbelief, resultUncertainty, resultAtomicity,resultRelativeWeight);
       return result;
   }
   
   /**
    * This method implements consensus & compromise fusion (CCF) for multiple sources, as discussed in a FUSION 2018 paper by van der Heijden et al.
    *
    * For more details, refer to Chapter 12 of the Subjective Logic book by Josang, specifically Section 12.6, which defines CC fusion for the case of two sources.
    *
    * @param opinions a collection of opinions from different sources.
    * @return a new SBoolean that represents the fused evidence.
    * @throws IllegalArgumentException
    */
   public static SBoolean consensusAndCompromiseFusion(Collection<SBoolean> opinions) 
   {
       if (opinions == null || opinions.contains(null) || opinions.size() < 2)
           throw new IllegalArgumentException("CCF: Cannot fuse null opinions, or only one opinion was passed");
 
       double baseRate = -1;
       boolean first = true;
       for (SBoolean so: opinions) {
           if(first) {
               baseRate = so.baseRate();
               first = false;
           }else if (baseRate != so.baseRate()) {
               throw new IllegalArgumentException("CCF: Base rates for CC Fusion must be the same");
           }
       }

       //Step 1: consensus phase
       final double consensusBelief = opinions.stream().mapToDouble(o -> o.belief()).min().getAsDouble();
       final double consensusDisbelief = opinions.stream().mapToDouble(o -> o.disbelief()).min().getAsDouble();

       final double consensusMass = consensusBelief + consensusDisbelief;

       List<Double> residueBeliefs = new ArrayList<>(opinions.size());
       List<Double> residueDisbeliefs = new ArrayList<>(opinions.size());
       List<Double> uncertainties = new ArrayList<>(opinions.size());
       for (SBoolean so : opinions) {
           //note: this max should not be necessary..
           residueBeliefs.add(Math.max(so.belief()-consensusBelief,0));
           residueDisbeliefs.add(Math.max(so.disbelief()-consensusDisbelief,0));
           uncertainties.add(so.uncertainty());
       }

       // System.out.println("consensusBelief="+consensusBelief);
       // System.out.println("consensusDisbelief="+consensusDisbelief);
       // System.out.println("consensusMass="+consensusMass);

       //Step 2: Compromise phase

       double productOfUncertainties = opinions.stream().mapToDouble(o -> o.uncertainty()).reduce(1.0D, (acc, u) -> acc * u);

       double compromiseBeliefAccumulator = 0;
       double compromiseDisbeliefAccumulator = 0;
       double compromiseXAccumulator = 0; //this is what will later become uncertainty

       //this computation consists of 4 sub-sums that will be added independently.
       for (int i=0; i<opinions.size(); i++) {
           double bResI = residueBeliefs.get(i);
           double dResI = residueDisbeliefs.get(i);
           double uI = uncertainties.get(i);
           // double uWithoutI = productOfUncertainties / uI;
           double uWithoutI = uI==0.0?0.0:productOfUncertainties / uI;

           //sub-sum 1:
           compromiseBeliefAccumulator = compromiseBeliefAccumulator + bResI * uWithoutI;
           compromiseDisbeliefAccumulator = compromiseDisbeliefAccumulator + dResI * uWithoutI;
           //note: compromiseXAccumulator is unchanged, since b^{ResI}_X() of the entire domain is 0
       }
       
       //sub-sums 2,3,4 are all related to different permutations of possible values
       for(List<Domain> permutation : tabulateOptions(opinions.size())){
           Domain intersection = permutation.stream().reduce(Domain.DOMAIN, (acc, p) -> acc.intersect(p));
           Domain union = permutation.stream().reduce(Domain.NIL, (acc, p) -> acc.union(p));

           //sub-sum 2: intersection of elements in permutation is x
           if(intersection.equals(Domain.TRUE)) {
               // --> add to belief
               double prod = 1;
               if(permutation.contains(Domain.DOMAIN))
                   prod = 0;
               else
                   for (int j=0; j<permutation.size();j++)
                       switch (permutation.get(j)){
                           case DOMAIN:
                               prod = 0; // multiplication by 0
                               break;
                           case TRUE:
                               prod = prod * residueBeliefs.get(j) * 1;
                               break;
                       }
               compromiseBeliefAccumulator = compromiseBeliefAccumulator + prod;
           } else if (intersection.equals(Domain.FALSE)) {
               // --> add to disbelief
               double prod = 1;
               if(permutation.contains(Domain.DOMAIN))
                   prod = 0;
               else
                   for (int j=0; j<permutation.size();j++)
                       switch (permutation.get(j)){
                           case DOMAIN:
                               prod = 0; // multiplication by 0
                               break;
                           case FALSE:
                               prod = prod * residueDisbeliefs.get(j) * 1;
                               break;
                       }
               compromiseDisbeliefAccumulator = compromiseDisbeliefAccumulator + prod;
           }

           switch (union){
               case DOMAIN:
                   if(!intersection.equals(Domain.NIL)) {
                       //sub-sum 3: union of elements in permutation is x, and intersection of elements in permutation is not NIL

                       //Note: this is always zero for binary domains, as explained by the following:
                       //double prod = 1;
                       //for (int j=0; j<permutation.size(); j++) {
                       //    switch (permutation.get(j)) {
                       //        case NIL:
                       //        case DOMAIN:
                       //            prod = 0; //because residue belief over NIL/DOMAIN is zero here
                       //            break;
                       //        case TRUE:
                       //        case FALSE:
                       //            prod = 0; //because 1-a(y|x) is zero here, since a(y|x)=1 when x=y, and this must occur, since a(x|!x) occurring implies the intersection is NIL
                       //            break;
                       //    }
                       //}

                   }
                   else {
                       //sub-sum 4: union of elements in permutation is x, and intersection of elements in permutation is NIL
                       double prod = 1;
                       for (int j=0; j<permutation.size(); j++) {
                           switch (permutation.get(j)) {
                               case NIL:
                               case DOMAIN:
                                   prod = 0; //because residue belief over NIL/DOMAIN is zero here
                                   break;
                               case TRUE:
                                   prod = prod * residueBeliefs.get(j);
                                   break;
                               case FALSE:
                                   prod = prod * residueDisbeliefs.get(j);
                                   break;
                           }
                       }
                       compromiseXAccumulator = compromiseXAccumulator + prod;
                   }
                   break;
               case NIL:
                   //union of NIL means we have nothing to add
                   //sub-sum 3: union of elements in permutation is x, and intersection of elements in permutation is not NIL
                   //sub-sum 4: union of elements in permutation is x, and intersection of elements in permutation is NIL
                   break;
               case TRUE:
                   //sub-sum 3: this is always zero for TRUE and FALSE, since 1-a(y_i|y_j)=0 in binary domains, where the relative base rate is either 1 if the union is x

                   //sub-sum 4: union of elements in permutation is x, and intersection of elements in permutation is NIL
                   if(intersection.equals(Domain.NIL)){
                       //union is true, intersection is nil --> compute the product
                       double prod = 1;
                       for (int j=0; j<permutation.size(); j++) {
                           switch (permutation.get(j)) { //other cases will not occur
                               case TRUE:
                                   prod = prod * residueBeliefs.get(j);
                                   break;
                               case FALSE:
                                   prod = prod * residueDisbeliefs.get(j);
                                   break;
                               case NIL:
                                   prod = 0;
                                   break;
                               default:
                                   throw new RuntimeException();
                           }
                       }
                       compromiseBeliefAccumulator = compromiseBeliefAccumulator + prod;
                   }
                   break;
               case FALSE:
                   //sub-sum 3: this is always zero for TRUE and FALSE, since 1-a(y_i|y_j)=0 in binary domains, where the relative base rate is either 1 if the union is x
                   //sub-sum 4: union of elements in permutation is x, and intersection of elements in permutation is NIL
                   if(intersection.equals(Domain.NIL)){
                       //union is true, intersection is nil --> compute the product
                       double prod = 1;
                       for (int j=0; j<permutation.size(); j++) {
                           switch (permutation.get(j)) { //other cases will not occur
                               case TRUE:
                                   prod = prod * residueBeliefs.get(j);
                                   break;
                               case FALSE:
                                   prod = prod * residueDisbeliefs.get(j);
                                   break;
                               case NIL:
                                   prod = 0;
                                   break;
                               default:
                                   throw new RuntimeException();
                           }
                       }
                       compromiseDisbeliefAccumulator= compromiseDisbeliefAccumulator + prod;
                   }
                   break;
               default:
                   break;

           }
       }

       double compromiseBelief = compromiseBeliefAccumulator;
       double compromiseDisbelief = compromiseDisbeliefAccumulator;
       double compromiseUncertainty = compromiseXAccumulator;

       double preliminaryUncertainty = productOfUncertainties;
       double compromiseMass = compromiseBelief + compromiseDisbelief + compromiseUncertainty;
       
       //System.out.println("compromiseBelief="+compromiseBelief);
       //System.out.println("compromiseDisbelief="+compromiseDisbelief);
       //System.out.println("compromiseUncertainty="+compromiseUncertainty);
       //System.out.println("compromiseMass="+compromiseMass);

       //Step 3: Normalization phase
       //double normalizationFactor = (1-consensusMass-preliminaryUncertainty)/(compromiseMass);
       double normalizationFactor = compromiseMass==0.0?1.0:(1-consensusMass-preliminaryUncertainty)/(compromiseMass);


       double fusedBelief = consensusBelief + normalizationFactor * compromiseBelief;
       double fusedDisbelief = consensusDisbelief + normalizationFactor * compromiseDisbelief;
       
       //double fusedUncertainty = preliminaryUncertainty + normalizationFactor* compromiseUncertainty;
       //compromiseUncertainty = 0; --> but this variable is never used again anyway.
        double fusedUncertainty = 1.0 - fusedBelief - fusedDisbelief;
       

       SBoolean res = new SBoolean(fusedBelief, fusedDisbelief, fusedUncertainty, baseRate);

       return res;
   }

   public enum Domain {
       NIL, TRUE, FALSE, DOMAIN;

       public Domain intersect(Domain d){
           switch(this){
               case NIL:
                   return NIL;
               case TRUE:
                   switch (d){
                       case NIL:
                       case FALSE:
                           return NIL;
                       case TRUE:
                       case DOMAIN:
                           return TRUE;
                       default:
                           throw new RuntimeException("unidentified domain");
                   }
               case FALSE:
                   switch (d){
                       case NIL:
                       case TRUE:
                           return NIL;
                       case FALSE:
                       case DOMAIN:
                           return FALSE;
                       default:
                           throw new RuntimeException("unidentified domain");
                   }
               case DOMAIN:
                   return d;
               default:
                   throw new RuntimeException("unidentified domain");
           }
       }

       public Domain union(Domain d){
           switch (this) {
               case DOMAIN:
                   return DOMAIN;
               case TRUE:
                   switch (d){
                       case TRUE:
                       case NIL:
                           return TRUE;
                       case FALSE:
                       case DOMAIN:
                           return DOMAIN;
                       default:
                           throw new RuntimeException("unidentified domain");
                   }
               case FALSE:
                   switch (d){
                       case FALSE:
                       case NIL:
                           return FALSE;
                       case TRUE:
                       case DOMAIN:
                           return DOMAIN;
                       default:
                           throw new RuntimeException("unidentified domain");
                   }
               case NIL:
                   return d;
               default:
                   throw new RuntimeException("unidentified domain");
           }
       }
   }

   private static Set<List<Domain>> tabulateOptions(int size) {
       if (size == 1) {
           Set<List<Domain>> result = new HashSet<List<Domain>>();
           for(Domain item : Domain.values()){
               List<Domain> l = new ArrayList<Domain>();
               l.add(item);
               result.add(l);
           }
           return result;
       }
       Set<List<Domain>> result = new HashSet();
       for (List<Domain> tuple : tabulateOptions(size - 1)) {
           for (Domain d : Domain.values()) {
               List newList = new ArrayList(tuple);
               newList.add(d);
               result.add(newList);
           }
       }
       return result;
   }


	/************************************ 
	 * BINARY VERSIONS OF FUSING OPERATIONS
	 * 
	 */
  
   public final SBoolean bcFusion(SBoolean opinion) { //belief constraint fusion
	   //implemented using equation 12.2 of Josang's book
	   double harmony = this.belief()*opinion.uncertainty() + this.uncertainty()*opinion.belief() + this.belief()*opinion.belief();
	   double conflict = this.belief()*opinion.disbelief()+this.disbelief()*opinion.belief(); //this.degreeOfConflict(opinion);// 0.0D; // binomial opinions; 
       if (conflict == 1.0D) 
           throw new IllegalArgumentException("BCF: Cannot fuse totally conflicting opinions");
       double b = harmony/(1.0D-conflict);
	   double u = (this.uncertainty()*opinion.uncertainty())/(1.0D-conflict); 
	   double a = (this.uncertainty()+opinion.uncertainty()==2.0D)? 
			(this.baseRate()+opinion.baseRate())/2.0D :
			(this.baseRate()*(1.0D-this.uncertainty()) + opinion.baseRate()*(1.0D-opinion.uncertainty()))/(2-this.uncertainty()-opinion.uncertainty());
	   return new SBoolean(b,1.0D-b-u,u,a);
   }
   
   public final SBoolean ccFusion(SBoolean opinion) { //consensus and compromise fusion
       Collection<SBoolean> opinions = new ArrayList<>();
       opinions.add(this);
       opinions.add(opinion);
       return consensusAndCompromiseFusion(opinions);
   }

   public final SBoolean cumulativeFusion(SBoolean opinion) {
       Collection<SBoolean> opinions = new ArrayList<>();
       opinions.add(this);
       opinions.add(opinion);
       return cumulativeBeliefFusion(opinions);
   }

   public final SBoolean epistemicCumulativeFusion(SBoolean opinion) {
       Collection<SBoolean> opinions = new ArrayList<>();
       opinions.add(this);
       opinions.add(opinion);
       return epistemicCumulativeBeliefFusion(opinions);
   }

     public final SBoolean weightedFusion(SBoolean opinion) {
       Collection<SBoolean> opinions = new ArrayList<>();
       opinions.add(this);
       opinions.add(opinion);
       return weightedBeliefFusion(opinions);
   }

   
   public final SBoolean minimumFusion(SBoolean opinion) {
       Collection<SBoolean> opinions = new ArrayList<>();
       opinions.add(this);
       opinions.add(opinion);
       return minimumBeliefFusion(opinions);
   }

   public final SBoolean majorityFusion(SBoolean opinion) {
       Collection<SBoolean> opinions = new ArrayList<>();
       opinions.add(this);
       opinions.add(opinion);
       return majorityBeliefFusion(opinions);
   }

   public final SBoolean averageFusion(SBoolean opinion) {
  	   Collection<SBoolean> opinions = new ArrayList<>();
       opinions.add(this);
       opinions.add(opinion);
       return averageBeliefFusion(opinions);
   }
   
   /****
    * DISCOUNTING OPERATIONS
    */
   
   /**
    * Binary versions
    */
   
   /**
    * This method implements the "probability-sensitive trust discounting operator", 
    * which causes the uncertainty in A's derived opinion about X to increase as a 
    * function of the projected distrust in the source/advisor B. 
    * 
    * For more details, refer to Chapter 14 of the Subjective Logic book by Josang, 
    * specifically Section 14.3.2 that defines Trust Discounting with Two-Edge Paths.
    * 
    * we assume that "this" represents the opinion (functional trust) of an agent B 
    * on statement X, i.e., [B:X]
    *
    * @param AtrustOnB The trust referral that Agent A has on B. [A;B]
    * @return a new SBoolean that represents the opinion of A about X, [A:X]=[A;B]x[B:X]
    * @throws IllegalArgumentException
    */

   public final SBoolean discount(SBoolean AtrustOnB) {
       if (AtrustOnB==null) throw new IllegalArgumentException("Discountion operator parameter cannot be null");

       /* This version is 
       double b = this.belief()*AtrustOnB.belief();
       double d = this.disbelief()*AtrustOnB.belief();
       double u = 1-b-d; // = AtrustOnB.disbelief() + AtrustOnB.uncertainty() + AtrustOnB.belief()*this.uncertainty();
       double a = this.baseRate();
		*/
       // THIS IS THE DISCOUNT OPERATOR DEFINED IN THE JOSANG 2016 BOOK 
       double p = AtrustOnB.projection();
       double b = p * this.belief();
       double d = p * this.disbelief();
       double u = 1 - p * (this.disbelief() + this.belief());
       double a = this.baseRate();
       return new SBoolean(b,d,u,a);
   }
   
   /**
    * This method implements the discounting operator from the Trustyfeer 2018 
    * paper bu Kurdi et al., which uses the belief() of the trust of A on B, instead of 
    * the projection() of the trust of A on B, that was originally used by Josang. 
    * 
    * Heba Kurdi, Bushra Alshayban, Lina Altoaimy, and Shada Alsalamah
    * "TrustyFeer: A Subjective Logic Trust Model for Smart City Peer-to-Peer Federated Clouds"
    * Wireless Communications and Mobile Computing, Volume 2018, Article ID 1073216, 13 pages
    * https://doi.org/10.1155/2018/1073216
    * 
    * We assume that "this" represents the opinion (functional trust) of an agent B 
    * on statement X, i.e., [B:X]
    *
    * @param AtrustOnB The trust referral that Agent A has on B. [A;B]
    * @return a new SBoolean that represents the opinion of A about X, [A:X]=[A;B]x[B:X]
    * @throws IllegalArgumentException
    */
   public final SBoolean discountB(SBoolean AtrustOnB) {
       if (AtrustOnB==null) throw new IllegalArgumentException("Discountion operator parameter cannot be null");

       double p = AtrustOnB.belief(); // instead of AtrustOnB.projection();
       double b = p * this.belief();
       double d = p * this.disbelief();
       double u = 1-b-d; // = AtrustOnB.disbelief() + AtrustOnB.uncertainty() + AtrustOnB.belief()*this.uncertainty();
       double a = this.baseRate();
       return new SBoolean(b,d,u,a);
   }

   
   /**
    * Multi-edge path versions
    */
   
   /**
    * This method implements the discounting operator on multi-edge paths, 
    * using the "probability-sensitive trust discounting operator"
    * which causes the uncertainty in A�s derived opinion about X to increase as a 
    * function of the projected distrust in the source/advisor B. 
    * 
    * For more details, refer to Chapter 14 of the Subjective Logic book by Josang, 
    * specifically Section 14.3.4 that defines Trust Discounting with Multi-Edge Paths.
    * 
    * we assume that "this" represents the opinion (functional trust) of an agent An 
    * on statement X, i.e., [An:X]
    *
    * @param agentsTrusts A collection of trust referrals that Agent (Ai) has on (Ai+1). [Ai;Ai+1]
    * @return a new SBoolean that represents the resulting opinion of A1 on X. 
    * [A1:X]=[A1;A2;...;An]x[An:X]
    * @throws IllegalArgumentException
    */
   	public final SBoolean discount(Collection <SBoolean> agentsTrusts) {
       if (agentsTrusts==null) throw 
       		new IllegalArgumentException("Discountion operator parameter cannot be null");

       // THIS IS THE DISCOUNT OPERATOR DEFINED IN THE JOSANG 2016 BOOK 
       double p = agentsTrusts.stream(). // we multiply the projections of all trust opinions
    		   mapToDouble( o -> o.projection()).
    		   reduce(1.0,(acc,value) -> acc * value);
       /* alternative implementation, using imperative programming
       double p1 = 1.0;
       for (SBoolean so : agentsTrusts) {
           p1 *= so.projection();
       }
       assert(p==p1);
       */

       double b = p * this.belief();
       double d = p * this.disbelief();
       double u = 1 - p * (this.disbelief() + this.belief());
       double a = this.baseRate();
       return new SBoolean(b,d,u,a);
   }
 
   	
    /**
     * This method implements the discounting operator on multi-edge paths, 
     * using the "discounting operator" discountB() defined by Kurdi et al in 
     * their 2018 paper 
     * 
     * Heba Kurdi, Bushra Alshayban, Lina Altoaimy, and Shada Alsalamah
     * "TrustyFeer: A Subjective Logic Trust Model for Smart City Peer-to-Peer Federated Clouds"
     * Wireless Communications and Mobile Computing, Volume 2018, Article ID 1073216, 13 pages
     * https://doi.org/10.1155/2018/1073216
     * 
     * we assume that "this" represents the opinion (functional trust) of an agent An 
     * on statement X, i.e., [An:X]
     *
     * @param agentsTrusts A collection of trust referrals that Agent (Ai) has on (Ai+1). [Ai;Ai+1]
     * @return a new SBoolean that represents the resulting opinion of A1 on X. 
     * [A1:X]=[A1;A2;...;An]x[An:X]
     * @throws IllegalArgumentException
     */
    public final SBoolean discountB(Collection <SBoolean> agentsTrusts) {
        if (agentsTrusts==null) throw 
        		new IllegalArgumentException("Discountion operator parameter cannot be null");

        // THIS IS THE DISCOUNT OPERATOR DEFINED IN THE JOSANG 2016 BOOK 
        double p = agentsTrusts.stream(). // we multiply the projections of all trust opinions
     		   mapToDouble( o -> o.belief()).
     		   reduce(1.0,(acc,value) -> acc * value);
        double b = p * this.belief();
        double d = p * this.disbelief();
        double u = 1 - p * (this.disbelief() + this.belief());
        double a = this.baseRate();
        return new SBoolean(b,d,u,a);
    }

   
	/***
	 * comparison operations
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SBoolean sBoolean = (SBoolean) o;

		/*	return  (Double.compare(sBoolean.b, this.b) == 0) && 
				(Double.compare(sBoolean.d, this.d) == 0) &&
				(Double.compare(sBoolean.u, this.u) == 0) &&
				(Double.compare(sBoolean.a, this.a) == 0);
		 */
		return 	Math.abs(this.belief()-sBoolean.belief()) < 0.001D && 
				Math.abs(this.disbelief()-sBoolean.disbelief()) < 0.001D &&
				Math.abs(this.uncertainty()-sBoolean.uncertainty()) < 0.001D &&
				Math.abs(this.baseRate()-sBoolean.baseRate()) < 0.001D ;

	}

	public boolean distinct(SBoolean b) {
		return !this.equals(b);
	}
	
	public SBoolean min(SBoolean opinion) { // minimum based on projections
		return this.projection() <= opinion.projection()? this : opinion;
	}

	public SBoolean max(SBoolean opinion) { // maximum based on projections
		return this.projection() >= opinion.projection()? this : opinion;
	}


	@Override
	public int hashCode() {
		int result;
		result = Math.round((float)this.b*100)+10*Math.round((float)this.d*100)+100*Math.round((float)this.u*100)+1000*Math.round((float)this.a*100);
		return result;
	}


	/******
	 * Conversions
	 */
	
	public String toString() {
		return String.format("SBoolean(%5.3f, %5.3f, %5.3f, %5.3f)", this.b, this.d, this.u, this.a);
	}

	public UBoolean toUBoolean(){ // returns the projected probability
		return new UBoolean(true, this.projection()); 
	}
	
	/**
	 * Other Methods 
	 */
	@Override
	public int compareTo(SBoolean other) {
		double x = Math.abs(this.belief()-other.belief()) + 
				   Math.abs(this.disbelief()-other.disbelief()) + 
				   Math.abs(this.uncertainty()-other.uncertainty()) +
				   Math.abs(this.baseRate()-other.baseRate());
		if (x<0.001D) return 0;
		if (this.projection()-other.projection() < 0) return -1;
		return 1;
	}

 	public SBoolean clone() {
		return new SBoolean(this.belief(),this.disbelief(),this.uncertainty(),this.baseRate(),this.relativeWeight);
	}

}
