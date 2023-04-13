package uDataTypes;

public class UncertaintyTest {
	
	
	public static void main(String[] args) {


	UBoolean b1 = new UBoolean(true, 1);
	UBoolean b2 = new UBoolean(true, 1);

	UBoolean res = b1.equivalent(b2);
	System.out.println("res="+res);
	
	UReal x = new UReal(2.0,0.1);
	UReal y = new UReal(2.0,0.0);
	UReal y2 = new UReal(2.0,0.0001);
	
	System.out.println("z="+(x.ge(y)));
	
	System.out.println("z2="+(x.ge(y2)));
	
	UInteger a = new UInteger(-4,3.0);
	UInteger b = new UInteger(2,4.0);
	UReal ar = new UReal(-4.0,3.0);
	UReal br = new UReal(2.0,4.0);
	System.out.println("a/a=" + a.divideBy(a));
	System.out.println("a/b=" + a.divideBy(b));
	System.out.println("b/a=" + b.divideBy(a));
	System.out.println("a/R b=" + a.divideByR(b));
	System.out.println("a mod b=" + a.mod(b));
	System.out.println("ar/ar=" + ar.divideBy(ar));
	System.out.println("ar/br=" + ar.divideBy(br));
	System.out.println("br/ar=" + br.divideBy(ar));
	
	System.out.println(new UReal(2.0,5.0).divideBy(new UReal(65.0,0.0)));
	System.out.println(new UInteger(2,5.0).divideBy(new UInteger(65,0.0)));
	System.out.println(new UInteger(2,5.0).divideByR(new UInteger(65,0.0)));
	}
		

}
