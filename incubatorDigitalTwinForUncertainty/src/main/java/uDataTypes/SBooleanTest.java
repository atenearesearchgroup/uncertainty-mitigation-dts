package uDataTypes;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

//import junit.framework.*;

class Test {
	
	boolean testAdjustBaseRate = true; 
	boolean print = true;
	
	void test0(String s, SBoolean x) {
		assertEquals(x.uncertaintyMaximized().projection(),x.projection(),"projection of uncertainty maximized");
		assertEquals(true,x.uncertaintyMaximized().isMaximizedUncertainty(),"uncertaintymaximized works");
		
		if (print) System.out.println(s + "="+x+" p="+x.projection());
		
		if (testAdjustBaseRate) {// displays the results of the adjustBaserate() operation to observe its behaviour on different SBooleans
			for (double i=0;i<=1;i+=0.1) {
				System.out.println(
						s+"="+x+
						" p="+x.projection()+
						" i="+((double)Math.round(i*100)/100)+
						" "+s+".adjusted="+x.applyOn(new UBoolean(i))+
						" p="+x.applyOn(new UBoolean(i)).projection());
			};
			System.out.println("---");
		}

		
	}
	
	void test0(SBoolean x) {
		test0("x",x);
	}
	
	void test1() {
		final SBoolean t = new SBoolean(); test0(t);
		//System.out.println(
	    //	"t=" + t + " p=" + t.projection() + " mu=" + t.uncertaintyMaximized() + " uBoolean=" + t.toUBoolean());
		final SBoolean f = new SBoolean(new UBoolean(true, 0)); test0(f);
		//System.out.println(
		//	"f=" + f + " p=" + f.projection() + " mu=" + f.uncertaintyMaximized() + " uBoolean=" + f.toUBoolean());

		final SBoolean b = new SBoolean(0.7, 0.1, 0.2, 0.5); test0(b);
		//System.out.println("b=" + b + " p=" + b.projection() + " mu=" + b.uncertaintyMaximized() + " uBoolean="
		// 	+ b.toUBoolean());
		
		final SBoolean b0 = new SBoolean(0.85, 0.05, 0.10, 0.9); test0(b0);
		//System.out.println("b0=" + b0 + " p=" + b0.projection() + " mu=" + b0.uncertaintyMaximized() + " uBoolean="
		//		+ b0.toUBoolean());
		
		final SBoolean b1 = new SBoolean(new UBoolean(true, 0.7)); test0(b1);
		//System.out.println("b1=" + b1 + " p=" + b1.projection() + " mu=" + b1.uncertaintyMaximized() + " uBoolean="
		//		+ b1.toUBoolean());
		final SBoolean b2 = new SBoolean(new UBoolean(false, 0.7)); test0(b2);
		//System.out.println("b2=" + b2 + " p=" + b2.projection() + " mu=" + b2.uncertaintyMaximized() + " uBoolean="
		//		+ b2.toUBoolean());
		final SBoolean b3 = new SBoolean(0.2, 0.6, 0.2, 0.5); test0(b3);
		//System.out.println("b3=" + b3 + " p=" + b3.projection() + " mu=" + b3.uncertaintyMaximized() + " uBoolean="
		//		+ b3.toUBoolean());
		final SBoolean b4 = new SBoolean(0.6, 0.2, 0.2, 0.5); test0(b4);
		//System.out.println("b4=" + b4 + " p=" + b4.projection() + " mu=" + b4.uncertaintyMaximized() + " uBoolean="
		//		+ b4.toUBoolean());

		final SBoolean torf = t.or(f); test0(torf); 
		assertEquals(t,torf,"torf");
		//System.out.println("torf=" + torf + " p=" + torf.projection() + " mu=" + torf.uncertaintyMaximized()
		//		+ " uBoolean=" + torf.toUBoolean());
		final SBoolean tandf = t.and(f); test0(tandf);
		assertEquals(f,tandf,"tandf");
		//System.out.println("tandf=" + tandf + " p=" + tandf.projection() + " mu=" + tandf.uncertaintyMaximized()
		//		+ " uBoolean=" + tandf.toUBoolean());

		final SBoolean torf1 = t.or(b1);test0(torf1);
		assertEquals(t,torf1,"torf1");
		//System.out.println("torb1=" + torf1 + " p=" + torf1.projection() + " mu=" + torf1.uncertaintyMaximized()
		// 	+ " uBoolean=" + torf1.toUBoolean());
		final SBoolean tandf1 = t.and(b1);test0(tandf1);
		assertEquals(b1,tandf1,"tandf1");
		//System.out.println("tandb1=" + tandf1 + " p=" + tandf1.projection() + " mu=" + tandf1.uncertaintyMaximized()
		//		+ " uBoolean=" + tandf1.toUBoolean());

		final SBoolean torf2 = b3.or(b4);test0(torf2);
		assertEquals(new SBoolean(0.680,0.173,0.147,0.750),torf2,"torf2");
		//System.out.println("torf2=" + torf2 + " p=" + torf2.projection() + " mu=" + torf2.uncertaintyMaximized()
		//		+ " uBoolean=" + torf2.toUBoolean());
		final SBoolean tandf2 = b3.and(b4);test0(tandf2);
		assertEquals(new SBoolean(0.173,0.680,0.147,0.250),tandf2,"tandf2");
		//System.out.println("tandf2=" + tandf2 + " p=" + tandf2.projection() + " mu=" + tandf2.uncertaintyMaximized()
		//		+ " uBoolean=" + tandf2.toUBoolean());

		final SBoolean txorf = b1.xor(b2);test0(txorf);
		assertEquals(new SBoolean(0.4,0.6,0,0.4),txorf,"txorf");
		//System.out.println("txorf=" + txorf + " p=" + txorf.projection() + " mu=" + txorf.uncertaintyMaximized()
		//		+ " uBoolean=" + txorf.toUBoolean());
		final SBoolean tequif = b1.equivalent(b2);test0(tequif);
		assertEquals(new SBoolean(0.6,0.4,0,0.6),tequif,"tequif");
		//System.out.println("tequif=" + tequif + " p=" + tequif.projection() + " mu=" + tequif.uncertaintyMaximized()
		//	+ " uBoolean=" + tequif.toUBoolean());
		assertEquals(new UBoolean(false,0.6),b1.toUBoolean().xor(b2.toUBoolean()),"uno");
		assertEquals(new UBoolean(true,0.6),b1.toUBoolean().equivalent(b2.toUBoolean()),"dos");

		final SBoolean txorf2 = b3.xor(b4); test0(txorf2);
		assertEquals(new SBoolean(0.4,0.56,0.04,0),txorf2,"txorf2");
		//System.out.println("txorf2=" + txorf2 + " p=" + txorf2.projection() + " mu=" + txorf2.uncertaintyMaximized()
		//		+ " uBoolean=" + txorf2.toUBoolean());
		final SBoolean tequif2 = b3.equivalent(b4);test0(tequif2);
		assertEquals(new SBoolean(0.56,0.4,0.04,1),tequif2,"tequif2");
		//System.out.println("tequif2=" + tequif2 + " p=" + tequif2.projection() + " mu=" + tequif2.uncertaintyMaximized()
		//		+ " uBoolean=" + tequif2.toUBoolean());
		assertEquals(new UBoolean(false,0.6),b3.toUBoolean().xor(b4.toUBoolean()),"tres");
		assertEquals(new UBoolean(true,0.6),b3.toUBoolean().equivalent(b4.toUBoolean()),"cuatro");

		final SBoolean b3equivB4 = b3.implies(b4).and(b4.implies(b3));
		test0(b3equivB4);
		assertEquals(new SBoolean(0.389, 0.477, 0.134, 0.563),b3equivB4,"b3equivB4");
		// System.out.println("b3equivB4="+b3equivB4+" p="+b3equivB4.projection()+" mu="+b3equivB4.uncertaintyMaximized()+" uBoolean="+b3equivB4.toUBoolean());
	
		//System.out.println(txorf.hashCode());
		//System.out.println(tequif.hashCode());
		//System.out.println(txorf2.hashCode());
		//System.out.println(tequif2.hashCode());
	
		//SBoolean b5 = new SBoolean(0.4,0.2,0.4,0.9);test0(b5);
		//System.out.println("b5="+b5+" p="+b5.projection()+" mu="+b5.uncertaintyMaximized()+" uBoolean="+b5.toUBoolean());
		SBoolean b5 = new SBoolean(0.154,0.150,0.696,0.1);test0(b5);
		
//		System.out.println("b5="+b5+" p="+b5.projection()+" mu="+b5.uncertaintyMaximized()+" uBoolean="+b5.toUBoolean());
		assertEquals(b5,(new SBoolean(0.75,0.15,0.1,0.5)).and(new SBoolean(0.1,0,0.9,0.2)),"b5and");
	
		b5 = (new SBoolean(0.75,0.15,0.1,0.5)).or(new SBoolean(0.35,0,0.65,0.2));test0(b5);
		SBoolean res= new SBoolean(0.837,0.065,0.098,0.6); test0(res);
		assertEquals(res,b5,"b5or");
		
		
	
	SBoolean x = new SBoolean(0.0,0.0,1,0.8); test0(x); 
	SBoolean yGivenX = new SBoolean(0.4,0.5,0.1,0.4); test0(yGivenX);
	SBoolean yGivenNotX = new SBoolean(0.0,0.4,0.6,0.4); test0(yGivenNotX);
	SBoolean y = x.deduceY(yGivenX, yGivenNotX); test0(y);
	res = new SBoolean(0.320, 0.480, 0.200, 0.400); test0(res);
	assertEquals(res,y);
	// System.out.println("y="+y+" p="+y.projection()+" mu="+y.uncertaintyMaximized()+" uBoolean="+y.toUBoolean());


	x = new SBoolean(0.10,0.8,0.1,0.8); test0(x);
	yGivenX = new SBoolean(0.4,0.5,0.1,0.4); test0(yGivenX);
	yGivenNotX = new SBoolean(0.0,0.4,0.6,0.4); test0(yGivenNotX);
	y = x.deduceY(yGivenX, yGivenNotX); test0(y);
	res = new SBoolean(0.072, 0.418, 0.510, 0.400); test0(res);
	assertEquals(res,y);
	//System.out.println("y="+y+" p="+y.projection()+" mu="+y.uncertaintyMaximized()+" uBoolean="+y.toUBoolean());

	x = new SBoolean(0.0,0.40,0.6,0.5); test0(x);
	yGivenX = new SBoolean(0.55,0.3,0.15,0.38); test0(yGivenX);
	yGivenNotX = new SBoolean(0.1,0.75,0.15,0.38); test0(yGivenNotX);
	y = x.deduceY(yGivenX, yGivenNotX); test0(y);
	res = new SBoolean(0.151, 0.48, 0.369, 0.382); test0(res);
	assertEquals(res,y);
	//System.out.println("y="+y+" p="+y.projection()+" mu="+y.uncertaintyMaximized()+" uBoolean="+y.toUBoolean());
	}
	
	void test2() {
		SBoolean b1 = new SBoolean(0.55,0.3,0.15,0.38); test0(b1);
		SBoolean b2 = new SBoolean(0.6,0.3,0.1,0.38); test0(b2);
		SBoolean b3 = new SBoolean(0.7,0.2,0.1,0.38); test0(b3);
		SBoolean b4 = new SBoolean(0.8,0.1,0.1,0.38); test0(b4);
		SBoolean b5 = new SBoolean(0.9,0.05,0.05,0.38);test0(b5);
		SBoolean res;
		
	    SBoolean wcf, majF, minF, aCCF, eCCF, ACF, CCF, BCF;
	    Collection<SBoolean> opinions = new ArrayList<>();
	    opinions.add(b1);
	    opinions.add(b2);
	    opinions.add(b3);
	    opinions.add(b4);
	    opinions.add(b5);

	    wcf = SBoolean.weightedBeliefFusion(opinions);test0(wcf);
	    res = new SBoolean(0.757, 0.156, 0.087, 0.380); test0(res);
		assertEquals(res,wcf);
	    // 	System.out.println("Wei="+wcf+" p="+wcf.projection()+" w="+wcf.getRelativeWeight());
	    
		majF = SBoolean.majorityBeliefFusion(opinions);test0(majF);
	    res = new SBoolean(1, 0, 0, 0.5); test0(res);
		assertEquals(res,majF);
	    // 	System.out.println("Maj="+majF+" p="+majF.projection()+" w="+majF.getRelativeWeight());
	    minF = SBoolean.minimumBeliefFusion(opinions);test0(minF);
	    res = new SBoolean(0.55, 0.3, 0.15, 0.38); test0(res);
		assertEquals(res,minF);
		//System.out.println("Min="+minF+" p="+minF.projection()+" w="+minF.getRelativeWeight());
	    ACF = SBoolean.averageBeliefFusion(opinions);test0(ACF);
	    res = new SBoolean(0.753, 0.159, 0.088, 0.38); test0(res);
		assertEquals(res,ACF);
		//System.out.println("Avg="+ACF+" p="+ACF.projection()+" w="+ACF.getRelativeWeight());
	    aCCF = SBoolean.cumulativeBeliefFusion(opinions);test0(aCCF);
	    res = new SBoolean(0.810, 0.171, 0.019, 0.38); test0(res);
		assertEquals(res,aCCF);
		//System.out.println("aCu="+aCCF+" p="+aCCF.projection()+" w="+aCCF.getRelativeWeight());
	    eCCF = SBoolean.epistemicCumulativeBeliefFusion(opinions);test0(eCCF);
	    res = new SBoolean(0.705, 0.0, 0.295, 0.38); test0(res);
		assertEquals(res,eCCF);
		//System.out.println("eCu="+eCCF+" p="+eCCF.projection()+" w="+eCCF.getRelativeWeight());
	    BCF = SBoolean.beliefConstraintFusion(opinions);test0(BCF);
	    res = new SBoolean(0.997, 0.003, 0, 0.38); test0(res);
		assertEquals(res,BCF);
		// System.out.println("CBF="+BCF+" p="+BCF.projection()+" w="+BCF.getRelativeWeight());

		// System.out.println("---");
		
		Collection<SBoolean> opinions2 = new ArrayList<>();
	    opinions2.add(b1);
	    opinions2.add(b2);
	    opinions2.add(b3);
	    opinions2.add(b4);
	    opinions2.add(b5);
	    CCF = SBoolean.consensusAndCompromiseFusion(opinions2);test0(CCF);
	    res = new SBoolean(0.564, 0.057, 0.379, 0.38); test0(res);
		assertEquals(res,CCF);
		//System.out.println("C&C="+CCF+" p="+CCF.projection()+" w="+CCF.getRelativeWeight());
	    BCF = SBoolean.beliefConstraintFusion(opinions2);test0(BCF);
	    res = new SBoolean(0.997, 0.003, 0, 0.38); test0(res);
		assertEquals(res,BCF);
		// System.out.println("CBF="+BCF+" p="+BCF.projection()+" w="+BCF.getRelativeWeight());
		// System.out.println("---");
		
		SBoolean T = new SBoolean(1,0,0,0.5); //true but with 0.5 mass
		SBoolean F = new SBoolean(0,1,0,0.5); //false but with 0.5 mass
		SBoolean U = new SBoolean(0,0,1,0.5); //the Vacuous opinion
	    SBoolean I = new SBoolean(0.5, 0.5, 0, 0.5); //Dogmatic ignorance

		Collection<SBoolean> TF = new ArrayList<>();
	    TF.add(F);
	    TF.add(T);
	    CCF = SBoolean.consensusAndCompromiseFusion(TF);test0(CCF);
		assertEquals(U,CCF);
	    // BCF = SBoolean.beliefConstraintFusion(TF);test0(BCF); // cannot be used with totally conflicting opinions
		ACF = SBoolean.averageBeliefFusion(TF);test0(ACF);
		assertEquals(I,ACF);
		aCCF = SBoolean.cumulativeBeliefFusion(TF);test0(aCCF);
		assertEquals(I,aCCF);
		eCCF = SBoolean.epistemicCumulativeBeliefFusion(TF);test0(eCCF);
		assertEquals(U,eCCF);
	    wcf = SBoolean.weightedBeliefFusion(TF);test0(wcf);
		assertEquals(I,wcf);
		
		assertEquals(T.ccFusion(U),U);
		assertEquals(F.ccFusion(U),U);
		assertEquals(T.ccFusion(T),T);
		assertEquals(F.ccFusion(F),F);
		assertEquals(U.ccFusion(U),U);
		assertEquals(U.ccFusion(T),U);
		assertEquals(U.ccFusion(F),U);
		assertEquals(U.ccFusion(I),U);
		assertEquals(I.ccFusion(I),I);
		assertEquals(I.ccFusion(U),U);
		assertEquals(I.ccFusion(T),new SBoolean(0.5,0.0,0.5,0.5));
		assertEquals(I.ccFusion(F),new SBoolean(0.0,0.5,0.5,0.5));
		
	    res = new SBoolean(0.25, 0.15, 0.6, 0.5); test0(res);

		assertEquals(res.ccFusion(U),res);
		assertEquals(U.ccFusion(res),res);
		assertEquals(res.ccFusion(res),res);

		Collection<SBoolean> opinions3 = new ArrayList<>();
	    opinions3.add(new SBoolean(0.1,0.3,0.6,0.5));
	    opinions3.add(new SBoolean(0.4,0.2,0.4,0.5));
	    opinions3.add(new SBoolean(0.7,0.1,0.2,0.5));
	    	    
	    CCF = SBoolean.consensusAndCompromiseFusion(opinions3);test0(CCF);
	    res = new SBoolean(0.629, 0.182, 0.189, 0.5); test0(res);
		assertEquals(res,CCF);
		//System.out.println("C&C="+CCF+" p="+CCF.projection()+" w="+CCF.getRelativeWeight());
	    BCF = SBoolean.beliefConstraintFusion(opinions3);test0(BCF);
	    res = new SBoolean(0.738, 0.184, 0.078, 0.5); test0(res);
		assertEquals(res,BCF);
		//System.out.println("CBF="+BCF+" p="+BCF.projection()+" w="+BCF.getRelativeWeight());
		ACF = SBoolean.averageBeliefFusion(opinions3);test0(ACF);
	    res = new SBoolean(0.509, 0.164, 0.327, 0.5); test0(res);
		assertEquals(res,ACF);
		//System.out.println("Avg="+ACF+" p="+ACF.projection()+" w="+ACF.getRelativeWeight());
		aCCF = SBoolean.cumulativeBeliefFusion(opinions3);test0(aCCF);
	    res = new SBoolean(0.651, 0.209, 0.140, 0.5); test0(res);
		assertEquals(res,aCCF);
		//System.out.println("aCu="+aCCF+" p="+aCCF.projection()+" w="+aCCF.getRelativeWeight());
		eCCF = SBoolean.epistemicCumulativeBeliefFusion(opinions3);test0(eCCF);
	    res = new SBoolean(0.442, 0, 0.558, 0.5); test0(res);
		assertEquals(res,eCCF);
		// System.out.println("eCu="+eCCF+" p="+eCCF.projection()+" w="+eCCF.getRelativeWeight());
	    wcf = SBoolean.weightedBeliefFusion(opinions3);test0(wcf);
	    res = new SBoolean(0.562, 0.146, 0.292, 0.5); test0(res);
		assertEquals(res,wcf);
		//System.out.println("Wei="+wcf+" p="+wcf.projection()+" w="+wcf.getRelativeWeight());
		
		//System.out.println("---");
		SBoolean uno = new SBoolean(0.9,0.1,0,0.5);test0(uno);
		SBoolean dos = new SBoolean(0.1,0.9,0,0.5);test0(dos);
		assertEquals(0.8,uno.degreeOfConflict(dos),"doc1");
		assertEquals(0.8,dos.degreeOfConflict(uno),"doc2");
		//System.out.println(uno.degreeOfConflict(dos));
		res = new SBoolean(0.5, 0.5, 0, 0.5); test0(res);
		assertEquals(res,uno.bcFusion(dos));
		//System.out.println(uno.bcFusion(dos));
	
	}
	
	void testAverageFusion() {
		SBoolean uno,dos,tres,res,r1;
		Collection<SBoolean> opinions; 
		
		uno = new SBoolean(0.9,0.1,0,0.5);
		dos = new SBoolean(0.1,0.9,0,0.5);
		res = new SBoolean(0.5,0.5,0,0.5);
		assertEquals(res,uno.averageFusion(dos));
		opinions = new ArrayList<>();
	    opinions.add(uno);
	    opinions.add(dos);
		assertEquals(res,SBoolean.averageBeliefFusion(opinions));
		
		uno  = new SBoolean(0.1,0.3,0.6,0.5);
		dos  = new SBoolean(0.4,0.2,0.4,0.5);
		tres = new SBoolean(0.7,0.1,0.2,0.5);
		res  = new SBoolean(0.509,0.164,0.327,0.5);
		opinions = new ArrayList<>();
	    opinions.add(uno);
	    opinions.add(dos);
	    opinions.add(tres);
		assertEquals(res,(SBoolean.averageBeliefFusion(opinions)));

		res  = new SBoolean(0.629,0.182,0.189,0.5);
		assertEquals(res,(SBoolean.consensusAndCompromiseFusion(opinions)));

		res  = new SBoolean(0.738,0.184,0.078,0.5);
		assertEquals(res,(SBoolean.beliefConstraintFusion(opinions)));
		
		res  = new SBoolean(0.651,0.209,0.140,0.5);
		assertEquals(res,(SBoolean.cumulativeBeliefFusion(opinions)));
		
		res  = new SBoolean(0.442,0,0.558,0.5);
		assertEquals(res,(SBoolean.epistemicCumulativeBeliefFusion(opinions))); // FAILS
		
		res  = new SBoolean(0.562,0.146,0.292,0.5);
		assertEquals(res,(SBoolean.weightedBeliefFusion(opinions)));
		
	
		uno = new SBoolean(0.33,0.33,0.34,0.5);
		dos = new SBoolean(0.33,0.33,0.34,0.5);
		res = new SBoolean(0.33,0.33,0.34,0.5);
		assertEquals(res, uno.averageFusion(dos));

		uno = new SBoolean(0.35,0.15,0.5,0.5);
		dos = new SBoolean(0.15,0.55,0.3,0.5);
		res = new SBoolean(0.225,0.4,0.375,0.5);
		assertEquals(res, uno.averageFusion(dos)); 

		uno = new SBoolean(0.9,0.1,0,0.5);
		dos = new SBoolean(0.1,0.9,0,0.5);
		res = new SBoolean(0.5,0.5,0,0.5);
		assertEquals(res, uno.averageFusion(dos));

		uno = new SBoolean(0.9,0.1,0,0.5);
		dos = new SBoolean(0.1,0.9,0,0.5);
		res = new SBoolean(0.5,0.5,0,0.5);
		assertEquals(res, uno.averageFusion(dos));
		
	}
	void otherTests() {
		// System.out.println("other tests");
		for(double u=0;u<=10;u++) {
			for(double i=0;i<=10;i++) {
				for(double b=0;b<=10;b++)
					if (u/10+i/10 <= 1) test0(new SBoolean(i/10,1-u/10-i/10,u/10,b/10));
			}
		}	
	}
	
	void discountTest(boolean print) {
		SBoolean AonB = new SBoolean(0.0,0.0,1,0.9); test0("[A;B]",AonB); 
		SBoolean BonX = new SBoolean(0.95,0,0.05,0.20); test0("[B:X]",BonX);
		SBoolean AonX = new SBoolean(0.855,0,0.145,0.2); 
		assertEquals(AonX,BonX.discount(AonB)); test0("[A;B:X]",BonX.discount(AonB));
		if (print) System.out.println(BonX.discount(AonB)+ ", p="+BonX.discount(AonB).projection());
		if (print) System.out.println(BonX.discountB(AonB)+ ", p="+BonX.discountB(AonB).projection());
		
		
		AonB = new SBoolean(0.2,0.4,0.4,0.75);  test0("[A;B]",AonB); 
		BonX = new SBoolean(0.45,0.35,0.20,0.25); test0("[B:X]",BonX);
		AonX = new SBoolean(0.225,0.175,0.6,0.25); test0("[A;B:X]",BonX.discount(AonB));
		assertEquals(AonX,BonX.discount(AonB));
		if (print) System.out.println(BonX.discount(AonB)+ ", p="+BonX.discount(AonB).projection());
		if (print) System.out.println(BonX.discountB(AonB)+ ", p="+BonX.discountB(AonB).projection());
		
		//MULTI-EDGE PATH DISCOUNT
		
		SBoolean A2onA1 = new SBoolean(0.95,0,0.05,0.5); test0("[A2;A1]",BonX.discount(A2onA1));
		SBoolean A3onA2 = new SBoolean(0.95,0,0.05,0.5); test0("[A3;A2]",BonX.discount(A3onA2));
		SBoolean A4onA3 = new SBoolean(0.95,0,0.05,0.5); test0("[A4;A3]",BonX.discount(A4onA3));
		SBoolean A1onX = new SBoolean(0.45,0.35,0.20,0.25); test0("[A1:X]",BonX.discount(A1onX));
		SBoolean A2onX = new SBoolean(0.439,0.341,0.22,0.25); 
		SBoolean A3onX = new SBoolean(0.428,0.333,0.24,0.25); 
		SBoolean A4onX = new SBoolean(0.417,0.324,0.259,0.25); 
		
	    Collection<SBoolean> opinions = new ArrayList<>();
	    opinions.add(A2onA1);
	    assertEquals(A2onX,A1onX.discount(opinions));
	    if (print) System.out.println(BonX.discount(opinions)+ ", p="+BonX.discount(opinions).projection());
	    if (print) System.out.println(BonX.discountB(opinions)+ ", p="+BonX.discountB(opinions).projection());
	    opinions.add(A3onA2);
	    assertEquals(A3onX,A1onX.discount(opinions));
	    if (print) System.out.println(BonX.discount(opinions)+ ", p="+BonX.discount(opinions).projection());
	    if (print) System.out.println(BonX.discountB(opinions)+ ", p="+BonX.discountB(opinions).projection());
	    opinions.add(A4onA3);
		assertEquals(A4onX,A1onX.discount(opinions));
		if (print) System.out.println(BonX.discount(opinions)+ ", p="+BonX.discount(opinions).projection());
		if (print) System.out.println(BonX.discountB(opinions)+ ", p="+BonX.discountB(opinions).projection());

		// Example from Josang, page 261
		A2onA1 = new SBoolean(0.2,0.1,0.7,0.8); 
		A3onA2 = new SBoolean(0.2,0.1,0.7,0.8); 
		A4onA3 = new SBoolean(0.2,0.1,0.7,0.8); 
		A1onX = new SBoolean(0.8,0.2,0.0,0.1); 
		A2onX = new SBoolean(0.608,0.152,0.240,0.1); 
		A3onX = new SBoolean(0.462,0.116,0.422,0.1); 
		A4onX = new SBoolean(0.351,0.088,0.561,0.1); 
	    opinions = new ArrayList<>();
	    opinions.add(A2onA1);
	    assertEquals(A2onX,A1onX.discount(opinions));
	    if (print) System.out.println(BonX.discount(opinions)+ ", p="+BonX.discount(opinions).projection());
	    if (print) System.out.println(BonX.discountB(opinions)+ ", p="+BonX.discountB(opinions).projection());
	    opinions.add(A3onA2);
	    assertEquals(A3onX,A1onX.discount(opinions));
	    if (print) System.out.println(BonX.discount(opinions)+ ", p="+BonX.discount(opinions).projection());
	    if (print) System.out.println(BonX.discountB(opinions)+ ", p="+BonX.discountB(opinions).projection());
	    opinions.add(A4onA3);
		assertEquals(A4onX,A1onX.discount(opinions));
		if (print) System.out.println(BonX.discount(opinions)+ ", p="+BonX.discount(opinions).projection());
		if (print) System.out.println(BonX.discountB(opinions)+ ", p="+BonX.discountB(opinions).projection());
		
		

		
	}
}
public class SBooleanTest {
	
	public static void main(final String[] args) {


		Test t = new Test();
		t.testAdjustBaseRate = false; // enables/disables display of base adjustments
		t.print = false; // displays results
		t.test1();
		t.test2();
		t.testAverageFusion();
		t.otherTests();
		//t.print = true; // displays results
		t.discountTest(false);
		
/*
 		System.out.println("r="+new UReal(345.09,12343.4));
		System.out.println("i="+new UInteger(345,12343.4));
		System.out.println("b="+new UBoolean(true,0.75));
		System.out.println("b="+new UBoolean(true,0.25));
		System.out.println("s="+new UString("Hola",0.99));
		System.out.println("o="+new SBoolean(0.7,0.2,0.1,0.5));
 */
}
	

}
