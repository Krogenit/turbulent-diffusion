package diffuse;

import com.nag.routines.D01.D01AJ;
import com.nag.routines.D01.D01AK;
import com.nag.routines.D01.D01AK.D01AK_F;

public class RLAYFIRST implements D01AK_F {

	private double X;
	D01AJ d01aj = new D01AJ();
	int mlw = 512;
	int mliw = 256;
	double[] mww = new double[mlw];
	int[] miw = new int[mliw];
	
	RLAYSECOND rlay;
	
	public RLAYFIRST(double w, double wg, double nu, double mu, double delta, double u, double v, double H) {
		rlay = new RLAYSECOND(w, wg, nu, mu, delta, u, v, H);
	}
	
	@Override
	public double eval(double X) {
		double a = 0;
		double b = 25.0;
		double epsabs = 1d-8; // абсолютная погрешность
		double epsrel = 1d-5; // относительная погрешность
		double abserr1 = 0.0;
		int ifail = -1;
		
		rlay.setGamma(X);
		d01aj.eval(rlay, a, b, epsabs, epsrel, 0.0, abserr1, mww, mlw, miw, mliw, ifail);
		return d01aj.getRESULT();
	}

	@Override
	public double getX() {
		return X;
	}

	@Override
	public void setX(double X) {
		this.X = X;
	}

	public RLAYSECOND getRlay() {
		return rlay;
	}
	
}
