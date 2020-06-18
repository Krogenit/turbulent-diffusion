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

public class MinFunc implements E04JY_FUNCT1 {

						//w,   wg,    nu,   mu,  delta, u,   v,    H
	RLAYTWODIM rlay = new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 1.0);
	Random rand = new Random();
//	D01AK d01ak = new D01AK();
	
	int N;
	double[] XC;
	double fResult;
	
	int liw, lw;
	double[] w;
	int[] iw;
	
	int[] iuser;
	double[] ruser;
	
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
	
	private static File calculatedResults = new File("calculated.data");
	public static Map<String, Double> calculatedValues = new ConcurrentHashMap<String, Double>();
	
	static {
		if(calculatedResults.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(calculatedResults));
				int size = Integer.valueOf(br.readLine());
				for(int i=0;i<size;i++) {
					String[] data = br.readLine().split("]=");
					String key = data[0];
					double value = Double.valueOf(data[1]);
					calculatedValues.put(key + "]", value);
				}
				br.close();
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public MinFunc(RLAYTWODIM rlay, List<Double> xPoints, List<Double> yPoints, List<Double> zPoints) {
		initBase();
		this.rlay = rlay;
		this.xPoints = xPoints;
		this.yPoints = yPoints;
		this.zPoints = zPoints;
	}
	
	public MinFunc() {
		initBase();
	}
	
	public void initBase() {
		N = Main.paramsCount;
		XC = new double[N];
		iuser = new int[1];
		ruser = new double[1];
		liw = N + 2;
		lw = Math.max(N * (N - 1) / 2 + 12 * N, 13);
		iw = new int[liw];
		w = new double[lw];
	}
	
	@Override
	public void eval(int N, double[] X, double FC, int[] iuser, double[] ruser) {	
		fResult = 0.0; // результат 
		int state = 0;
		List<ThreadRlayMin> threads = new ArrayList<ThreadRlayMin>();
		int threadCount = (xPoints.size() + yPoints.size() + zPoints.size()) / 3;
		threadCount = 3;
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
				boolean ready = service.awaitTermination(100, TimeUnit.MILLISECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (ThreadRlayMin t : threads) 
			fResult += t.getResult();
		
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
	public double getFC() {
		return fResult;
	}

	@Override
	public int[] getIUSER() {
		return this.iuser;
	}

	@Override
	public int getN() {
		return this.N;
	}

	@Override
	public double[] getRUSER() {
		return this.ruser;
	}

	@Override
	public double[] getXC() {
		return XC;
	}

	@Override
	public void setFC(double FC) {
		this.fResult = FC;
	}

	@Override
	public void setIUSER(int[] arg0) {
		this.iuser = arg0;
	}

	@Override
	public void setN(int N) {
		this.N = N;
	}

	@Override
	public void setRUSER(double[] arg0) {
		this.ruser = arg0;
	}

	@Override
	public void setXC(double[] XC) {
		this.XC = XC;
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

	
}
