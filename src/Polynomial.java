
public class Polynomial {

	private C[] factors;
	
	public Polynomial() {}

	public Polynomial(C[] factors) {
		this.factors = factors.clone();
	}
	
	public C evaluate(C x) {
		C sum = C.ZERO;
		for(int i = 0; i < factors.length; i++) {
			sum = sum.plus(factors[i].times(x.pow(factors.length - i - 1)));
		}
		return sum;
	}
	
	public Polynomial getDerivative() {
		C[] newFactors = new C[factors.length - 1];
		for(int i = 0; i < newFactors.length; i++) {
			newFactors[i] = factors[i].times(new C(newFactors.length - i, 0));
		}
		return new Polynomial(newFactors);
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < factors.length; i++) {
			if(!factors[i].equals(C.ZERO)) {
				String sign = i == 0 ? "" : "+";
				String factor = "(" + factors[i] + ")";
				int exponent = factors.length - i - 1;
				str += sign + factor;
				if(i != factors.length - 1) str += "x" + (exponent == 1 ? "" : "^" + exponent);
			}
		}
		return str;
	}
	
}
