package diffuse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

public class ThreadRlayMin implements Runnable {

	public static NumberFormat formatter = new DecimalFormat("#0.00");
	RLAYTWODIM rlay;
	double x, y, z;
	double error;
	double X[];
	int state, N;
	
	double result;
	
	Map<String, Double> calculatedValues;
	
	public ThreadRlayMin(RLAYTWODIM rlay, double x, double y, double z, int state, double error, double[] X, int N, Map<String, Double> calculatedValues) {
		this.rlay = rlay;
		this.x = x;
		this.y = y;
		this.z = z;
		this.state = state;
		this.error = error;
		this.X = X;
		this.N = N;
		this.calculatedValues = calculatedValues;
	}
	
	@Override
	public void run() {
		rlay.setX(x);//замер в точке по X
		rlay.setY(y);//замер в точке по Y
		rlay.setZ(z); // замер в точке по Z
		rlay.setX0(0);//позиция источника
		rlay.setC(4.0);//мощность
		rlay.setZ0(0.5);//высота источника
		
		String key = rlay.toString();
		double result1 = 0;
		if(calculatedValues.containsKey(key)) {
			result1 = calculatedValues.get(key);
		} else {
			result1 = rlay.eval();
			calculatedValues.put(key, result1);
		}
		result1 *= (1.0 + x*error * (Math.pow(-1.0, state)));//погрешность измерений
		
		rlay.setX0(X[0]);//ищем позицию источника
		if(N > 1) rlay.setC(X[1]);//ищем мощность
		if(N > 2) rlay.setZ0(X[2]);//ищем высоту
		
		key = rlay.toString();
		double result2 = 0;
		if(calculatedValues.containsKey(key)) {
			result2 = calculatedValues.get(key);
		} else {
			result2 = rlay.eval();	
			calculatedValues.put(key, result2);
		}
		
		double dif = result1 - result2;
		result = (dif * dif);
	}
	
	public double getResult() {
		return this.result;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	
}
