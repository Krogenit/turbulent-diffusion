package diffuse;

import com.nag.routines.D01.D01AK;

public class RLAYTWODIM {
	
	D01AK d01ak = new D01AK();
	int mlw = 8192;
	int mliw = 4096;
	double[] mww = new double[mlw];
	int[] miw = new int[mliw];
	
	RLAYFIRST rlay;
	
	public RLAYTWODIM(double w, double wg, double nu, double mu, double delta, double u, double v, double H) {
		rlay = new RLAYFIRST(w, wg, nu, mu, delta, u, v, H);
	}
	
	double pi2 = 2.0 * Math.PI;
	
	public double eval() {
		double a = 0;
		double b = pi2;
		double epsabs = 1d-6; // абсолютная погрешность
		double epsrel = 1d-4; // относительная погрешность
		double abserr1 = 0.0;
		int ifail = -1;
		
		d01ak.eval(rlay, a, b, epsabs, epsrel, 0.0, abserr1, mww, mlw, miw, mliw, ifail);
		return d01ak.getRESULT();
	}

	public RLAYFIRST getRlay() {
		return rlay;
	}

	public void setX(double x) {
		rlay.getRlay().setX1(x);
	}
	
	public void setY(double y) {
		rlay.getRlay().setY(y);
	}

	public void setZ(double z) {
		rlay.getRlay().setZ(z);
	}

	public void setX0(double x0) {
		rlay.getRlay().setX0(x0);
	}

	public void setC(double c) {
		rlay.getRlay().setC(c);
	}

	public void setZ0(double z0) {
		rlay.getRlay().setZ0(z0);
	}

	public double getU() {
		return rlay.getRlay().getU();
	}

	public RLAYTWODIM copy() {
		return rlay.getRlay().copy();
	}
	
	@Override
	public String toString() {
		return rlay.getRlay().toString();
	}

	public void setY0(double i) {
		rlay.getRlay().setY0(i);
	}
}
