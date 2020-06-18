package diffuse;

import com.nag.routines.A02.A02AA;
import com.nag.routines.A02.A02AC;
import com.nag.routines.D01.D01AJ.D01AJ_F;
import com.nag.routines.D01.D01AK;
import com.nag.routines.D01.D01AK.D01AK_F;
import com.nag.routines.S.S01EA;

public class RLAYSECOND implements D01AJ_F {

	S01EA a01ea = new S01EA(); //complex exp
	A02AC a02ac = new A02AC(); //complex div
	A02AA a02aa = new A02AA(); //complex square root
	
	private double alf;
	private double X;
	private double Y;
	private double X0;
	private double Y0;
	private double Z;
	private double Z0;
	private double H;
	
	private double mu;
	private double nu;
	private double u;
	private double v;
	private double delta;
	
	private double w, wg;
	
	private double C;
	
	private double gamma;
	
	NAGComplex CI; //complex i
	NAGComplex dva; //complex 2 + i0
	
	public RLAYSECOND(double w, double wg, double nu, double mu, double delta, double u, double v, double H) {
		CI = new NAGComplex(0.0, 1.0);
		dva = new NAGComplex(2.0, 0.0);
		this.w = w;
		this.wg = wg;
		this.nu = nu;
		this.mu = mu;
		this.delta = delta;
		this.u = u;
		this.v = v;
		this.H = H;
	}
	
	double piSqr4 = 4.0 * Math.PI * Math.PI;
	
	@Override
	public double eval(double alf) {
		double R = Math.sqrt(X*X + Y*Y);
		double PSI = Math.atan2(Y, X);
		
		double A1 = alf * Math.cos(gamma);
		double A2 = alf * Math.sin(gamma);
		
		double wwg = w-wg;
		
		NAGComplex CTETA = teta(A1, A2);
		NAGComplex CK1 = new NAGComplex((wwg) / (2 * nu), 0);
		
		NAGComplex comp1 = plus(new NAGComplex((wwg) * (wwg), 0), mul(new NAGComplex(4 * nu, 0), CTETA));
		NAGComplex CK2 = div(sqrt(comp1), mul(dva, new NAGComplex(nu, 0)));
		
		NAGComplex CE1 = mul(new NAGComplex(C, 0), exp(plus(mul(CI, new NAGComplex(A1 * X0 + A2 * Y0, 0)), mul(CK1, new NAGComplex(Z - Z0, 0)))));
		NAGComplex CDET = mul(new NAGComplex(nu, 0), mul(CK2, minus(mul(CK1, CSH(mul(CK2, new NAGComplex(H, 0)))), mul(CK2, CCH(mul(CK2, new NAGComplex(H, 0)))))));
		
		NAGComplex CF;
		
		if(Z < Z0) {
			CF = div(mul(mul(CE1, CSH(mul(CK2, new NAGComplex(H - Z0, 0)))), minus(mul(CK1,CSH(mul(CK2, new NAGComplex(Z, 0)))), mul(CK2, CCH(mul(CK2, new NAGComplex(Z, 0)))))),CDET);
		} else {
			CF = div(mul(mul(CE1, CSH(mul(CK2, new NAGComplex(H - Z, 0)))), minus(mul(CK1,CSH(mul(CK2, new NAGComplex(Z0, 0)))), mul(CK2, CCH(mul(CK2, new NAGComplex(Z0, 0)))))),CDET);
		}
		
		return mul(CF, div(exp(mul(minus(CI), new NAGComplex(R * alf * Math.cos(gamma - PSI), 0))), new NAGComplex(piSqr4, 0))).getRe();
	}

	@Override
	public double getX() {
		return alf;
	}

	@Override
	public void setX(double alf) {
		this.alf = alf;
	}
	
	public void setX1(double X) {
		this.X = X;
	}
	
	public double getX1() {
		return X;
	}
	
	public void setY(double Y) {
		this.Y = Y;
	}
	
	public void setX0(double X0) {
		this.X0 = X0;
	}
	
	public void setZ(double Z) {
		this.Z = Z;
	}
	
	public void setC(double C) {
		this.C = C;
	}
	
	public void setZ0(double Z0) {
		this.Z0 = Z0;
	}
	
	public double getH() {
		return H;
	}

	public double getMu() {
		return mu;
	}

	public double getNu() {
		return nu;
	}

	public double getU() {
		return u;
	}

	public double getV() {
		return v;
	}

	public double getW() {
		return w;
	}

	public double getWg() {
		return wg;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public NAGComplex teta(double x, double y) {
		return new NAGComplex(mu * (x * x + y * y) + delta, - (x * u + y * v));
	}
	
	public NAGComplex CSH(NAGComplex CA) {
		return div(minus(exp(CA), exp(minus(CA))), dva);
	}
	
	public NAGComplex CCH(NAGComplex CA) {
		return div(plus(exp(CA), exp(minus(CA))), dva);
	}
	
	public NAGComplex sqrt(NAGComplex x) {
		a02aa.eval(x.getRe(), x.getIm(), 0.0, 0.0);
		double outRe = a02aa.getYR();
		double outIm = a02aa.getYI();
		return new NAGComplex(outRe, outIm);
	}
	
	public NAGComplex exp(NAGComplex x) {
		a01ea.setZ(new NAGComplex(x));
		return new NAGComplex(a01ea.eval());
	}
	
	public NAGComplex div(NAGComplex x, NAGComplex y) {
		a02ac.eval(x.getRe(), x.getIm(), y.getRe(), y.getIm(), 0.0, 0.0);
		double outRe = a02ac.getZR();
		double outIm = a02ac.getZI();
		return new NAGComplex(outRe, outIm);
	}
	
	public NAGComplex mul(NAGComplex x, NAGComplex y) {
		return new NAGComplex(x.getRe() * y.getRe() - x.getIm() * y.getIm(), x.getIm() * y.getRe() + x.getRe() * y.getIm());
	}
	
	public NAGComplex minus(NAGComplex x) {
		return new NAGComplex(-x.getRe(), -x.getIm());
	}
	
	public NAGComplex minus(NAGComplex x, NAGComplex y) {
		return new NAGComplex(x.getRe() - y.getRe(), x.getIm() - y.getIm());
	}
	
	public NAGComplex plus(NAGComplex x, NAGComplex y) {
		return new NAGComplex(x.getRe() + y.getRe(), x.getIm() + y.getIm());
	}

	@Override
	public String toString() {
		return "RLAY [X=" + X + ", Y=" + Y + ", X0=" + X0 + ", Z=" + Z + ", Z0=" + Z0 + ", H=" + H + ", mu=" + mu + ", nu=" + nu + ", u=" + u + ", v=" + v + ", delta=" + delta + ", w=" + w + ", wg=" + wg + ", C=" + C + "]";
	}

	public RLAYTWODIM copy() {
		return new RLAYTWODIM(w, wg, nu, mu, delta, mu, v, H);
	}

	public void setY0(double i) {
		this.Y0 = i;
	}
}
