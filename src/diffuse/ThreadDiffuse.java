package diffuse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.nag.routines.D01.D01AK;
import com.nag.routines.E04.E04JY;
import com.nag.routines.E05.E05JA;
import com.nag.routines.E05.E05JB;
import com.nag.routines.E05.E05SA;
import com.nag.routines.E05.E05ZK;
import com.nag.routines.E05.E05ZL;

public class ThreadDiffuse implements Runnable {

	public double xMin;
	public double xMax;
	public double yMin;
	public double yMax;
	public double zMin;
	public double zMax;
	public int count;
	public double error;
	
	NumberFormat formatter = new DecimalFormat("#0.00");
	NumberFormat formatter1 = new DecimalFormat("#0.000000");
	NumberFormat formatter2 = new DecimalFormat("#0.00000");
	
	D01AK d01ak = new D01AK();
	E04JY e04jy = new E04JY();
	
	E05JB e05jb = new E05JB();
	E05JA e05ja = new E05JA();
	
	E05SA e05sa = new E05SA();
	E05ZK e05zk = new E05ZK();
	E05ZL e05zl = new E05ZL();
									//w,   wg,    nu,   mu,  delta, u,   v,    H
	RLAYTWODIM rlay = new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 1.0);
	List<Double> xPoints;
	List<Double> yPoints;
	List<Double> zPoints;
	
	private double outX0, outZ0, outC;
	
	public ThreadDiffuse(RLAYTWODIM rlay, double error, double[] xPoints, double[] yPoints, double[] zPoints) {
		super();
		this.rlay = rlay;
		this.count = xPoints.length + yPoints.length + zPoints.length;
		this.error = error;
		this.xPoints = new ArrayList<Double>();
		this.yPoints = new ArrayList<Double>();
		this.zPoints = new ArrayList<Double>();
		
		zMin = zPoints.length > 0 ? zPoints[0] : 0;
		xMin = xPoints.length > 0 ? xPoints[0] : 0;
		yMin = yPoints.length > 0 ? yPoints[0] : 0;
		zMax = zPoints.length > 0 ? zPoints[0] : 0;
		xMax = xPoints.length > 0 ? xPoints[0] : 0;
		yMax = yPoints.length > 0 ? yPoints[0] : 0;
		for(int i=0;i<xPoints.length;i++) this.xPoints.add(xPoints[i]);
		for(int i=0;i<zPoints.length;i++) this.zPoints.add(zPoints[i]);
		for(int i=0;i<yPoints.length;i++) this.yPoints.add(yPoints[i]);
		
		for(Double x : xPoints) {
			if(x.doubleValue() < xMin) {
				xMin = x.doubleValue();
			}
			
			if(x.doubleValue() > xMax) {
				xMax = x.doubleValue();
			}
		}
		
		for(Double z : zPoints) {
			if(z.doubleValue() < zMin) {
				zMin = z.doubleValue();
			}
			
			if(z.doubleValue() > zMax) {
				zMax = z.doubleValue();
			}
		}
		
		for(Double y : yPoints) {
			if(y.doubleValue() < yMin) {
				yMin = y.doubleValue();
			}
			
			if(y.doubleValue() > yMax) {
				yMax = y.doubleValue();
			}
		}
	}



	@Override
	public void run() {
		MinFuncE05SA minFunc = new MinFuncE05SA(rlay, xPoints, yPoints, zPoints);

		minFunc.setzMax(zMax);
		minFunc.setzMin(zMin);
		minFunc.setyMax(yMax);
		minFunc.setyMin(yMin);
		minFunc.setError(error);
		minFunc.setNX(count);
		minFunc.setxMax(xMax);
		minFunc.setxMin(xMin);

		int N = minFunc.getN();
		double finalF = 0;
		double[] bl = new double[] {-20, 0.0, 0.0};
		double[] bu = new double[] {20, 10.0, 2.0};
		double[] x = new double[] {5.0, 1.0, 1.5};

		int ibound = 0;
		int iinit = 0;
		int sdlist = 192;
		double[] list = new double[N * sdlist];
		int[] initpt = new int[N];
		int[] numpts = new int[N];
		double obj = 0;
		int lcomm = 100;
		double[] comm = new double[lcomm];
		int ifail = -1;
		
		int npar = N * 10;
		double[] xb = new double[N];
		double fb = 0;
		int[] itt = new int[6];
		int inform = 0;
		
		e05ja.eval(N, comm, lcomm, ifail);
		lcomm = e05ja.getLCOMM();
		comm = e05ja.getCOMM();

		int liopts = 100;
		int lopts = 100;
		int[] iopts = new int[liopts];
		double[] opts = new double[lopts];
		
		ifail = -1;
		e05zk.eval("Initialize = E05SAF", iopts,liopts,opts,lopts,ifail);
		int ivalue = 0, rvalue = 0, optype = 0;
		String cvalue = "                                                                                                                ";

		ifail = 0;
		e05zk.eval("Verify Gradients = Off", iopts,liopts,opts,lopts,ifail);
		ifail = 0;
		e05zk.eval("SMP Monmod = ALL", iopts,liopts,opts,lopts,ifail);
		ifail = 0;
		e05zk.eval("Maximum iterations static = 2000", iopts,liopts,opts,lopts,ifail);
		ifail = 0;

		ifail = -1;
		try {
			e05sa.eval(N, npar, xb, fb, bl, bu, minFunc, new MonMod(), iopts, opts, minFunc.getIUSER(), minFunc.getRUSER(), itt, inform, ifail);
		} catch(NullPointerException e) {
			
		}
		System.out.println(" IFAIL = " + e05jb.getIFAIL());
		x = e05sa.getXB();
		System.out.print(" Result X = " + x[0]);// + " Func = " + d01ak.getRESULT());
		if(Main.paramsCount > 1) System.out.print(" Ñ = " + x[1] );
		if(Main.paramsCount > 2) System.out.print(" Z0 = " + x[2]);
		System.out.println();

		System.out.println(" xMIN = " + xMin + " xMAX = " + xMax + " yMIN = " + yMin + " yMAX = " + yMax + " zMIN = " + zMin + " zMAX = " + zMax + " Count = " + count + " Error = " + error);
		
		Main.current++;
		System.out.println("Progress " + formatter.format((Main.current / (float) Main.total) * 100f) + "%");
		
		this.outX0 = x[0];
		if(Main.paramsCount > 1) this.outC = x[1];
		if(Main.paramsCount > 2) this.outZ0 = x[2];	
	}

	public double getError() {
		return error;
	}

	public NumberFormat getFormatter2() {
		return formatter2;
	}

	public RLAYTWODIM getRlay() {
		return rlay;
	}

	public List<Double> getxPoints() {
		return xPoints;
	}
	
	public List<Double> getyPoints() {
		return yPoints;
	}

	public List<Double> getzPoints() {
		return zPoints;
	}

	public double getOutX0() {
		return outX0;
	}

	public double getOutZ0() {
		return outZ0;
	}

	public double getOutC() {
		return outC;
	}

	public int getCount() {
		return count;
	}
	
	
	
}
