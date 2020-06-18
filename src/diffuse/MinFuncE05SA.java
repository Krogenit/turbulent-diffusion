package diffuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nag.routines.D01.D01AK;
import com.nag.routines.E04.E04JY.E04JY_FUNCT1;
import com.nag.routines.E05.E05JB.E05JB_OBJFUN;
import com.nag.routines.E05.E05SA.E05SA_OBJFUN;

public class MinFuncE05SA implements E05SA_OBJFUN {

						//w,   wg,    nu,   mu,  delta, u,   v,    H
	RLAYTWODIM rlay = new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 1.0);
	Random rand = new Random();
//	D01AK d01ak = new D01AK();
	
	int inform, nstate;
	int N;
	double[] X;
	double F;
	
	int liw, lw;
	double[] w;
	int[] iw;
	
	int[] iuser;
	double[] ruser;
	
	double[] vecout;
	int mode;
	
	public double xMin;
	public double xMax;
	public double yMin;
	public double yMax;
	public double zMin;
	public double zMax;
	public int NX;
	public double error;
	
	List<Double> xPoints;
	List<Double> yPoints;
	List<Double> zPoints;
	
	private static File calculatedResults = new File("calculatedE05SA.data");
	public static Map<String, Double> calculatedValues = new ConcurrentHashMap<String, Double>();
	
	static {
//		if(calculatedResults.exists()) {
//			try {
//				BufferedReader br = new BufferedReader(new FileReader(calculatedResults));
//				int size = Integer.valueOf(br.readLine());
//				for(int i=0;i<size;i++) {
//					String[] data = br.readLine().split("]=");
//					String key = data[0];
//					double value = Double.valueOf(data[1]);
//					calculatedValues.put(key + "]", value);
//				}
//				br.close();
//			} catch (NumberFormatException | IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public MinFuncE05SA(RLAYTWODIM rlay, List<Double> xPoints, List<Double> yPoints, List<Double> zPoints) {
		initBase();
		this.rlay = rlay;
		this.xPoints = xPoints;
		this.yPoints = yPoints;
		this.zPoints = zPoints;
	}
	
	public void initBase() {
		N = Main.paramsCount;
		X = new double[N];
		iuser = new int[1];
		ruser = new double[1];
		liw = N + 2;
		lw = Math.max(N * (N - 1) / 2 + 12 * N, 13);
		iw = new int[liw];
		w = new double[lw];
		vecout = new double[N];
	}
	
	@Override
	public void eval(int mode, int N, double[] X, double F, double[] vecout, int nstate, int[] iuser, double[] ruser) {
		boolean calculateFunc = false;
		if(mode == 0 || mode == 5 || mode == 2 || mode == 7) {
			calculateFunc = true;
		}
		
		if(calculateFunc) {
			F = 0.0;
			int state = 0;
			
			List<ThreadRlayMin> threads = new ArrayList<ThreadRlayMin>();
			int threadCount = (xPoints.size() + yPoints.size() + zPoints.size()) / 3;
			threadCount = 8;
			ExecutorService service = Executors.newFixedThreadPool(threadCount);
			for(Double x : xPoints)
				for(Double y : yPoints)
					for(Double z : zPoints) {
						threads.add(new ThreadRlayMin(rlay.copy(), x, y, z, state, error, X, N, calculatedValues));
						state++;
					}
			
			for (ThreadRlayMin t : threads)
				service.submit(t);
			service.shutdown();
			
			while (true) {
				try {
					boolean ready = service.awaitTermination(1, TimeUnit.MILLISECONDS);
					if (ready) break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			for (ThreadRlayMin t : threads) 
				F += t.getResult();
			
			this.F = F;
		}
	}
	
	public void setRLAY(RLAYTWODIM rlay) {
		this.rlay = rlay;
	}
	
	public double getzMin() {
		return zMin;
	}

	public void setzMin(double zMin) {
		this.zMin = zMin;
	}
	
	public double getyMin() {
		return yMin;
	}

	public void setyMin(double yMin) {
		this.yMin = yMin;
	}
	
	public double getyMax() {
		return yMax;
	}

	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

	public double getzMax() {
		return zMax;
	}

	public void setzMax(double zMax) {
		this.zMax = zMax;
	}
	
	public double getxMin() {
		return xMin;
	}

	public void setxMin(double xMin) {
		this.xMin = xMin;
	}

	public double getxMax() {
		return xMax;
	}

	public void setxMax(double xMax) {
		this.xMax = xMax;
	}

	public int getNX() {
		return NX;
	}

	public void setNX(int nX) {
		NX = nX;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	@Override
	public int[] getIUSER() {
		return this.iuser;
	}

	@Override
	public double[] getRUSER() {
		return this.ruser;
	}

	@Override
	public void setIUSER(int[] arg0) {
		this.iuser = arg0;
	}

	@Override
	public void setRUSER(double[] arg0) {
		this.ruser = arg0;
	}

	public int getLiw() {
		return liw;
	}

	public int getLw() {
		return lw;
	}

	public double[] getW() {
		return w;
	}

	public int[] getIw() {
		return iw;
	}

	@Override
	public int getNSTATE() {
		return this.nstate;
	}

	@Override
	public double[] getX() {
		return X;
	}

	@Override
	public void setNSTATE(int arg0) {
		this.nstate = arg0;
	}

	@Override
	public void setX(double[] arg0) {
		this.X = arg0;
 	}

	@Override
	public int getMODE() {
		return mode;
	}

	@Override
	public int getNDIM() {
		return N;
	}

	@Override
	public double getOBJF() {
		return F;
	}

	@Override
	public double[] getVECOUT() {
		return vecout;
	}

	@Override
	public void setMODE(int arg0) {
		mode = arg0;
	}

	@Override
	public void setNDIM(int arg0) {
		N = arg0;
	}

	@Override
	public void setOBJF(double arg0) {
		F = arg0;
	}

	@Override
	public void setVECOUT(double[] arg0) {
		vecout = arg0;
	}

	public int getN() {
		return N;
	}

	public static void saveCalculatedValues() {
		FileWriter fw;
		try {
			fw = new FileWriter(calculatedResults);
			fw.write(calculatedValues.size() + "\n");
			Iterator<String> it = calculatedValues.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				Double value = calculatedValues.get(key);
				fw.write(key + "=" + value.doubleValue() + "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
