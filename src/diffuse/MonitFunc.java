package diffuse;

import com.nag.routines.E05.E05JB.E05JB_MONIT;

public class MonitFunc implements E05JB_MONIT {

	private int N;
	private double[] boxl, boxu;
	private int[] icount;
	private int inform;
	
	public MonitFunc(int N) {
		this.N = N;
	}
	
	@Override
	public void eval(int n, int ncall, double[] xbest, int[] icount, int ninit, double[] list, int[] numpts, int[] initpt, int nbaskt, double[] xbaskt, double[] boxl, double[] boxu, int nstate, int[] arg13, double[] arg14, int inform) {
		if(inform >= 0) {
//			if (nstate == 0 || nstate == 1) {
//				System.out.println("*** Begin monitoring information ***");
//				System.out.println("Values controlling initial splitting of a box:");
				
//				for (int i = 1; i < n; i++) {
//					System.out.println("In dimension " + i + " ");
//					System.out.println("Extent of initialization list in this dimension = " + numpts[i] + " ");
//					System.out.print("Initialization points in this dimension: " + list[i + numpts[i]] + " ");
//					System.out.println(initpt[i] + " ");
//				}
//			} 
			
			if(nstate <= 0) {
//				System.out.println("Total sub-boxes = " + icount[0] + " ");
//				System.out.println("Total function evaluations (rounded to nearest 20) = " + (20*((ncall+10)/20)) + " ");
//				System.out.println("Total function evaluations used in local search (rounded" + (15*((icount[1]+7)/15)) + " ");
//				System.out.println("Total points used in local search = " + icount[2] + " ");
//				System.out.println("Total sweeps through levels = " + icount[3] + " ");
//				System.out.println("Total splits by init. list = " + icount[4] + " ");
//				System.out.println("Lowest level with nonsplit boxes = " + icount[5] + " ");
//				System.out.println("Number of candidate minima in the \"shopping basket = " + nbaskt + "");
//				
//				System.out.println("Shopping basket: ");
				System.out.print("* Current global optimum candidate " + xbest[0]);
				if(Main.paramsCount > 1) System.out.print(" " + xbest[1]);
				if(Main.paramsCount > 2) System.out.print(" " + xbest[2]);
			}
		}
	}

	@Override
	public double[] getBOXL() {
		return null;
	}

	@Override
	public double[] getBOXU() {
		return null;
	}

	@Override
	public int[] getICOUNT() {
		return null;
	}

	@Override
	public int getINFORM() {
		return inform;
	}

	@Override
	public int[] getINITPT() {
		return null;
	}

	@Override
	public int[] getIUSER() {
		return null;
	}

	@Override
	public double[] getLIST() {
		return null;
	}

	@Override
	public int getN() {
		return N;
	}

	@Override
	public int getNBASKT() {
		return 0;
	}

	@Override
	public int getNCALL() {
		return 0;
	}

	@Override
	public int getNINIT() {
		return 0;
	}

	@Override
	public int getNSTATE() {
		return 0;
	}

	@Override
	public int[] getNUMPTS() {
		return null;
	}

	@Override
	public double[] getRUSER() {
		return null;
	}

	@Override
	public double[] getXBASKT() {
		return null;
	}

	@Override
	public double[] getXBEST() {
		return null;
	}

	@Override
	public void setBOXL(double[] arg0) {
		
	}

	@Override
	public void setBOXU(double[] arg0) {

	}

	@Override
	public void setICOUNT(int[] arg0) {

	}

	@Override
	public void setINFORM(int arg0) {
		inform = arg0;
	}

	@Override
	public void setINITPT(int[] arg0) {

	}

	@Override
	public void setIUSER(int[] arg0) {

	}

	@Override
	public void setLIST(double[] arg0) {

	}

	@Override
	public void setN(int arg0) {

	}

	@Override
	public void setNBASKT(int arg0) {

	}

	@Override
	public void setNCALL(int arg0) {

	}

	@Override
	public void setNINIT(int arg0) {

	}

	@Override
	public void setNSTATE(int arg0) {

	}

	@Override
	public void setNUMPTS(int[] arg0) {

	}

	@Override
	public void setRUSER(double[] arg0) {

	}

	@Override
	public void setXBASKT(double[] arg0) {

	}

	@Override
	public void setXBEST(double[] arg0) {

	}

}
