package uDataTypes;

public class ExamplesSBoolean {

	public static void main(final String[] args) {

		boolean isEnemyB = true;
		UBoolean isEnemyU = new UBoolean(true,0.8);
		SBoolean isEnemy10 = new SBoolean(0.8,0.2,0.0,0.8);
		SBoolean isEnemy11 = new SBoolean(0.8,0.1,0.1,0.8);
		SBoolean isEnemy12 = new SBoolean(0.8,0.0,0.2,0.8);
		SBoolean isEnemy20 = new SBoolean(0.9,0.1,0.0,0.8);
		SBoolean isEnemy21 = new SBoolean(0.9,0.05,0.05,0.8);
		SBoolean isEnemy22 = new SBoolean(0.9,0.0,0.1,0.8);
		SBoolean isEnemy30 = new SBoolean(0.7,0.3,0.0,0.8);
		SBoolean isEnemy31 = new SBoolean(0.7,0.15,0.15,0.8);
		SBoolean isEnemy32= new SBoolean(0.7,0.0,0.3,0.8);
	
		boolean isArmedB = true;
		UBoolean isArmedU = new UBoolean(true,0.9);
		SBoolean isArmed1 = new SBoolean(0.9,0.1,0.0,0.9);
		SBoolean isArmed2 = new SBoolean(0.9,0.05,0.05,0.9);
		SBoolean isArmed3 = new SBoolean(0.9,0.0,0.1,0.9);
	
		double distance = 800.0d;
		UReal distanceU = new UReal (900.0d, 10);
	
		double FAR = 1000.0d; // in metres
		UReal FARU = new UReal (800.0d, 10); // in metres
		
		UBoolean d = new UBoolean(true,1.0);
		
		System.out.println(raiseAlarm(isEnemyB,isArmedB,true));
		
		System.out.println(raiseAlarm(isEnemyU,isArmedU,d));
		System.out.println();

		System.out.println(isEnemy10+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy10,isArmed1,d)+" & "+raiseAlarm(isEnemy10,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy11+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy11,isArmed1,d)+" & "+raiseAlarm(isEnemy11,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy12+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy12,isArmed1,d)+" & "+raiseAlarm(isEnemy12,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy20+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy20,isArmed1,d)+" & "+raiseAlarm(isEnemy20,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy21+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy21,isArmed1,d)+" & "+raiseAlarm(isEnemy21,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy22+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy22,isArmed1,d)+" & "+raiseAlarm(isEnemy22,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy30+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy30,isArmed1,d)+" & "+raiseAlarm(isEnemy30,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy31+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy31,isArmed1,d)+" & "+raiseAlarm(isEnemy31,isArmed1,d).projection()+"\\\\");
		System.out.println(isEnemy32+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy32,isArmed1,d)+" & "+raiseAlarm(isEnemy32,isArmed1,d).projection()+"\\\\");
		System.out.println("");

		System.out.println(isEnemy10+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy10,isArmed2,d)+" & "+raiseAlarm(isEnemy10,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy11+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy11,isArmed2,d)+" & "+raiseAlarm(isEnemy11,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy12+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy12,isArmed2,d)+" & "+raiseAlarm(isEnemy12,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy20+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy20,isArmed2,d)+" & "+raiseAlarm(isEnemy20,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy21+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy21,isArmed2,d)+" & "+raiseAlarm(isEnemy21,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy22+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy22,isArmed2,d)+" & "+raiseAlarm(isEnemy22,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy30+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy30,isArmed2,d)+" & "+raiseAlarm(isEnemy30,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy31+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy31,isArmed2,d)+" & "+raiseAlarm(isEnemy31,isArmed2,d).projection()+"\\");
		System.out.println(isEnemy32+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy32,isArmed2,d)+" & "+raiseAlarm(isEnemy32,isArmed2,d).projection()+"\\");
		System.out.println("");

		System.out.println(isEnemy10+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy10,isArmed3,d)+" & "+raiseAlarm(isEnemy10,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy11+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy11,isArmed3,d)+" & "+raiseAlarm(isEnemy11,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy12+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy12,isArmed3,d)+" & "+raiseAlarm(isEnemy12,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy20+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy20,isArmed3,d)+" & "+raiseAlarm(isEnemy20,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy21+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy21,isArmed3,d)+" & "+raiseAlarm(isEnemy21,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy22+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy22,isArmed3,d)+" & "+raiseAlarm(isEnemy22,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy30+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy30,isArmed3,d)+" & "+raiseAlarm(isEnemy30,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy31+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy31,isArmed3,d)+" & "+raiseAlarm(isEnemy31,isArmed3,d).projection()+"\\");
		System.out.println(isEnemy32+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy32,isArmed3,d)+" & "+raiseAlarm(isEnemy32,isArmed3,d).projection()+"\\");
		System.out.println("");

		/* boolean isEnemyB = true;
		UBoolean isEnemyU = new UBoolean(true,0.85);
		SBoolean isEnemy1 = new SBoolean(0.9,0.0,0.1,0.85);
		SBoolean isEnemy2 = new SBoolean(0.9,0.1,0.0,0.85);
		SBoolean isEnemy3 = new SBoolean(0.85,0.0,0.15,0.85);
		SBoolean isEnemy4 = new SBoolean(0.85,0.15,0.0,0.85);
		SBoolean isEnemy5 = new SBoolean(0.8,0.0,0.2,0.85);
		SBoolean isEnemy6 = new SBoolean(0.8,0.2,0.0,0.85);
	
		boolean isArmedB = true;
		UBoolean isArmedU = new UBoolean(true,0.9);
		SBoolean isArmed1 = new SBoolean(0.9,0.0,0.1,0.9);
		SBoolean isArmed2 = new SBoolean(0.9,0.05,0.05,0.9);
		SBoolean isArmed3 = new SBoolean(0.9,0.1,0.0,0.9);
	
		double distance = 800.0d;
		UReal distanceU = new UReal (900.0d, 10);
	
		double FAR = 1000.0d; // in metres
		UReal FARU = new UReal (800.0d, 10); // in metres
		
		UBoolean d = new UBoolean(true,1.0);
		
		System.out.println(raiseAlarm(isEnemyB,isArmedB,true));
		
		System.out.println(raiseAlarm(isEnemyU,isArmedU,d));
		System.out.println();

		System.out.println(isEnemy1+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy1,isArmed1,d)+" & "+raiseAlarm(isEnemy1,isArmed1,d).projection());
		System.out.println(isEnemy2+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy2,isArmed1,d)+" & "+raiseAlarm(isEnemy2,isArmed1,d).projection());
		System.out.println(isEnemy3+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy3,isArmed1,d)+" & "+raiseAlarm(isEnemy3,isArmed1,d).projection());
		System.out.println(isEnemy4+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy4,isArmed1,d)+" & "+raiseAlarm(isEnemy4,isArmed1,d).projection());
		System.out.println(isEnemy5+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy5,isArmed1,d)+" & "+raiseAlarm(isEnemy5,isArmed1,d).projection());
		System.out.println(isEnemy6+" & "+ isArmed1 +" & "+ raiseAlarm(isEnemy6,isArmed1,d)+" & "+raiseAlarm(isEnemy6,isArmed1,d).projection());
		System.out.println();

		System.out.println(isEnemy1+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy1,isArmed2,d)+" & "+raiseAlarm(isEnemy1,isArmed2,d).projection());
		System.out.println(isEnemy2+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy2,isArmed2,d)+" & "+raiseAlarm(isEnemy2,isArmed2,d).projection());
		System.out.println(isEnemy3+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy3,isArmed2,d)+" & "+raiseAlarm(isEnemy3,isArmed2,d).projection());
		System.out.println(isEnemy4+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy4,isArmed2,d)+" & "+raiseAlarm(isEnemy4,isArmed2,d).projection());
		System.out.println(isEnemy5+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy5,isArmed2,d)+" & "+raiseAlarm(isEnemy5,isArmed2,d).projection());
		System.out.println(isEnemy6+" & "+ isArmed2 +" & "+ raiseAlarm(isEnemy6,isArmed2,d)+" & "+raiseAlarm(isEnemy6,isArmed2,d).projection());
		System.out.println();

		System.out.println(isEnemy1+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy1,isArmed3,d)+" & "+raiseAlarm(isEnemy1,isArmed3,d).projection());
		System.out.println(isEnemy2+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy2,isArmed3,d)+" & "+raiseAlarm(isEnemy2,isArmed3,d).projection());
		System.out.println(isEnemy3+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy3,isArmed3,d)+" & "+raiseAlarm(isEnemy3,isArmed3,d).projection());
		System.out.println(isEnemy4+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy4,isArmed3,d)+" & "+raiseAlarm(isEnemy4,isArmed3,d).projection());
		System.out.println(isEnemy5+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy5,isArmed3,d)+" & "+raiseAlarm(isEnemy5,isArmed3,d).projection());
		System.out.println(isEnemy6+" & "+ isArmed3 +" & "+ raiseAlarm(isEnemy6,isArmed3,d)+" & "+raiseAlarm(isEnemy6,isArmed3,d).projection());
*/
		
	}	

	static boolean raiseAlarm(boolean isEnemy, boolean isArmed, boolean isClose) {
		return isEnemy && isArmed && isClose;
	}

	static UBoolean raiseAlarm(UBoolean isEnemy, UBoolean isArmed, UBoolean isClose) {
		return isEnemy.and(isArmed).and(isClose);
	}
	static SBoolean raiseAlarm(SBoolean isEnemy, SBoolean isArmed, UBoolean isClose) {
		return isEnemy.and(isArmed).and(new SBoolean(isClose));
	}

}
