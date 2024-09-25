
public class C {

	public static C ZERO = new C(0, 0);
	
	public double real, imaginary;
	
	public C() {}
	
	public C(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	@Override
	public String toString() {
		String real = this.real % 1 == 0 ? (((int) this.real) + "") : (this.real + "");
		String imaginary = this.imaginary % 1 == 0 ? (((int) this.imaginary) + "") : (this.imaginary + "");
		return (this.real == 0 ? "" : real + (this.imaginary > 0 ? "+" : "")) + (this.imaginary == 0 ? "" : (imaginary + (this.imaginary == 0 ? "": "i")));
	}
	
	public C plus(C c) {
		return new C(real + c.real, imaginary + c.imaginary);
	}
	
	public C minus(C c) {
		return this.plus(c.times(new C(-1, 0)));
	}
	
	public C times(C f) {
		return new C(real * f.real - imaginary * f.imaginary, real * f.imaginary + f.real * imaginary);
	}
	
	public C div(C n) {
		double r = (real * n.real + imaginary * n.imaginary) / (n.real * n.real + n.imaginary * n.imaginary);
		double i = (imaginary * n.real - real * n.imaginary) / (n.real * n.real + n.imaginary * n.imaginary);
		return new C(r, i);
	}
	
	public C pow(int n) {
		C c = copy();
		for(int i = 0; i < n; i++) {
			c = c.times(c);
		}
		return c;
	}
	
	public double diff(C c) {
		return Math.sqrt((real - c.real) * (real - c.real) + (imaginary - c.imaginary) * (imaginary - c.imaginary));
	}
	
	public boolean equals(C c) {
		return real == c.real && imaginary == c.imaginary;
	}
	
	public C copy() {
		return new C(real, imaginary);
	}
	
}
