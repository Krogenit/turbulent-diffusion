package diffuse;

import com.nag.routines.E05.E05SA.E05SA_MONMOD;

public class MonMod implements E05SA_MONMOD {

	@Override
	public void eval(int ndim, int npar, double[] x, double[] xb, double fb, double[] xbest, double[] fbest, int[] itt, int[] iuser, double[] ruser, int inform) {
		if(itt[0] == 1 || itt[0] == 0) {
//			System.out.println("* Locations of particles");
			int indent = 2;
//			for(int j = 1;j<npar;j++) {
//				System.out.println("* Particle " + j);
//			}
			
			System.out.print("* Current global optimum candidate " + xb[0]);
			if(Main.paramsCount > 1) System.out.print(" " + xb[1]);
			if(Main.paramsCount > 2) System.out.print(" " + xb[2]);
			System.out.println();
			
		}
		
		inform = 0;
	}

	@Override
	public double getFB() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getFBEST() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getINFORM() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getITT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getIUSER() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNDIM() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNPAR() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getRUSER() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getX() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getXB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getXBEST() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFB(double arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFBEST(double[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setINFORM(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setITT(int[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIUSER(int[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNDIM(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNPAR(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRUSER(double[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setX(double[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXB(double[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXBEST(double[] arg0) {
		// TODO Auto-generated method stub
		
	}

}
