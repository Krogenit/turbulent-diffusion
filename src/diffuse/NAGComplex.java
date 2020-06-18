package diffuse;

import com.nag.types.NAGComplexInterface;

public class NAGComplex extends com.nag.types.NAGComplex {

	public NAGComplex() {
		super();
	}
	
	public NAGComplex(NAGComplex x) {
		super();
		this.setRe(x.getRe());
		this.setIm(x.getIm());
	}

	public NAGComplex(double re, double im) {
		super();
		this.setRe(re);
		this.setIm(im);
	}
	
	public NAGComplex(NAGComplexInterface x) {
		super();
		this.setRe(x.getRe());
		this.setIm(x.getIm());
	}

	public NAGComplex addRe(double re) {
		this.setRe(getRe() + re);
		return this;
	}
	
	public NAGComplex addIm(double im) {
		this.setIm(getIm() + im);
		return this;
	}
}
