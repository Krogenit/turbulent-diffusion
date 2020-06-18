package diffuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.nag.exceptions.NAGBadIntegerException;
import com.nag.routines.Routine;
import com.nag.routines.D01.D01AK;
import com.nag.routines.E04.E04JY;

public class Main {

	D01AK d01ak = new D01AK();
	E04JY e04jy = new E04JY();
	public static NumberFormat formatter = new DecimalFormat("#0.00");
	public static NumberFormat formatter1 = new DecimalFormat("#0.000000");
	public static NumberFormat formatter2 = new DecimalFormat("#0.00000");
	Random rand = new Random();
	
	public static int current = 0;
	public static int total = 0;
	
	public static int paramsCount = 3;
	private int THREAD_COUNT = 2;
	
	public Main() {
		calculate();
		
//		calculateVertical();

//		calculateU();
//		MinFunc.saveCalculatedValues();
//		MinFuncE05SA.saveCalculatedValues();
//		MinFuncE05JB.saveCalculatedValues();
//		calculatePoints();
//		MinFunc.saveCalculatedValues();
//		calculateVertical();
//		MinFunc.saveCalculatedValues();
//		calculateHeights();
//		MinFunc.saveCalculatedValues();
//		calculateError();
//		MinFunc.saveCalculatedValues();
//		calculateCount();
//		MinFunc.saveCalculatedValues();
	}
	
	private void calculateCount() {
		List<ThreadDiffuse> threads = new ArrayList<ThreadDiffuse>();
		ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
		
		// count
		for(int i=0;i<100;i++) {
			int size = rand.nextInt(40) + 1;
			double[] xPoints = new double[size];
			for(int j=0;j<size;j++) {
				xPoints[j] = 2 + ((j / (float) size) * 2.0f);
			}
			double[] yPoints = new double[size];
			for(int j=0;j<size;j++) {
				yPoints[j] = 2 + ((j / (float) size) * 2.0f);
			}
			ThreadDiffuse threadDif = new ThreadDiffuse(new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 1.0), 0.00001f, xPoints, yPoints, new double[] {0.01});
			threads.add(threadDif);
		}

		total = threads.size();
		for (ThreadDiffuse t : threads)
			service.submit(t);
		service.shutdown();

		while (true) {
			try {
				boolean ready = service.awaitTermination(5, TimeUnit.SECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		ExcelGenerator outGenerator = new ExcelGenerator(threads, "diffuse_data_"+paramsCount+"param_count%NUM%.xlsx", true, new Comparator<ThreadDiffuse>() {
			@Override
	        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
	        	return data1.getCount() > data2.getCount() ? 1 : -1;
	        }
		});
		outGenerator.generateExcelNew();
	}
	
	private void calculateU() {
		List<ThreadDiffuse> threads = new ArrayList<ThreadDiffuse>();
		ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
		
		// u
		for (int i = 0; i < 100; i++) {
			ThreadDiffuse threadDif = new ThreadDiffuse(new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 
					rand.nextDouble() * 4 - 2
					, 0.01, 1.0), 0.00001f, new double[] {2, 3, 4, 5, 6}, new double[] {2, 3, 4, 5, 6}, new double[] {0.01});
			threads.add(threadDif);
		}

		total = threads.size();
		for (ThreadDiffuse t : threads)
			service.submit(t);
		service.shutdown();

		while (true) {
			try {
				boolean ready = service.awaitTermination(5, TimeUnit.SECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ExcelGenerator outGenerator = new ExcelGenerator(threads, "diffuse_data_"+paramsCount+"param_u%NUM%.xlsx", true, new Comparator<ThreadDiffuse>() {
			@Override
	        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
	        	return data1.getRlay().getU() > data2.getRlay().getU() ? 1 : -1;
	        }
		});
		outGenerator.generateExcelNew();
	}
	
	private void calculateError() {
		List<ThreadDiffuse> threads = new ArrayList<ThreadDiffuse>();
		ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
		
		// error
		for (int i = 0; i < 100; i++) {
			ThreadDiffuse threadDif = new ThreadDiffuse(new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 1.0), rand.nextDouble() / 100f + 0.00000001f, new double[] {2, 3, 4, 5, 6}, new double[] {2,3,4}, new double[] {0.01});
			threads.add(threadDif);
		}

		total = threads.size();
		for (ThreadDiffuse t : threads)
			service.submit(t);
		service.shutdown();

		while (true) {
			try {
				boolean ready = service.awaitTermination(5, TimeUnit.SECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ExcelGenerator outGenerator = new ExcelGenerator(threads, "diffuse_data_"+paramsCount+"param_error%NUM%.xlsx", true, new Comparator<ThreadDiffuse>() {
			@Override
	        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
	        	return data1.getError() > data2.getError() ? 1 : -1;
	        }
		});
		outGenerator.generateExcelNew();
	}
	
	private void calculateVertical() {
		List<ThreadDiffuse> threads = new ArrayList<ThreadDiffuse>();
		ExecutorService service = Executors.newFixedThreadPool(1);
		
		// heights
		for (int i = 0; i < 10; i++) {
			int size = i + 1;
			double[] zPoints = new double[size];
			for (int j = 0; j < size; j++) {
				zPoints[j] = j/100f + 0.01f;
			}
			ThreadDiffuse threadDif = new ThreadDiffuse(new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 2.0), 0.00001f, new double[] {rand.nextInt(40) - 20}, new double[] {rand.nextInt(40) - 20}, zPoints);
			threads.add(threadDif);
		}

		total = threads.size();
		for (ThreadDiffuse t : threads)
			service.submit(t);
		service.shutdown();

		while (true) {
			try {
				boolean ready = service.awaitTermination(5, TimeUnit.SECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ExcelGenerator outGenerator = new ExcelGenerator(threads, "diffuse_data_E05SA_"+paramsCount+"param_vertical%NUM%.xlsx", true, new Comparator<ThreadDiffuse>() {
			@Override
	        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
	        	return data1.getOutX0() > data2.getOutX0() ? 1 : -1;
	        }
		});
		outGenerator.generateExcelNew();
	}
	
	private void calculateHeights() {
		List<ThreadDiffuse> threads = new ArrayList<ThreadDiffuse>();
		ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
		
		// heights
		for (int i = 0; i < 100; i++) {
			ThreadDiffuse threadDif = new ThreadDiffuse(new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 1.0), 0.00001f, new double[] {2, 3, 4, 5, 6}, new double[] {2, 3, 4, 5, 6}, new double[] {rand.nextInt(10) / 9.1f + 0.001f});
			threads.add(threadDif);
		}

		total = threads.size();
		for (ThreadDiffuse t : threads)
			service.submit(t);
		service.shutdown();

		while (true) {
			try {
				boolean ready = service.awaitTermination(5, TimeUnit.SECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ExcelGenerator outGenerator = new ExcelGenerator(threads, "diffuse_data_"+paramsCount+"param_height%NUM%.xlsx", true, new Comparator<ThreadDiffuse>() {
			@Override
	        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
	        	return data1.getzPoints().get(0) > data2.getzPoints().get(0) ? 1 : -1;
	        }
		});
		outGenerator.generateExcelNew();
	}
	
	private void calculatePoints() {
		List<ThreadDiffuse> threads = new ArrayList<ThreadDiffuse>();
		ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
		
		// points
		for (int i = 0; i < 200; i++) {
			int size = rand.nextInt(25) + 1;
			double[] xPoints = new double[size];
			for (int j = 0; j < size; j++) {
				xPoints[j] = rand.nextInt(40) - 20;
			}
			size = rand.nextInt(25) + 1;
			double[] yPoints = new double[size];
			for (int j = 0; j < size; j++) {
				yPoints[j] = rand.nextInt(40) - 20;
			}
			ThreadDiffuse threadDif = new ThreadDiffuse(new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.04, 0.01, 1.0), 0.00001f, xPoints, yPoints, new double[] {0.01f});
			threads.add(threadDif);
		}

		total = threads.size();
		for (ThreadDiffuse t : threads)
			service.submit(t);
		service.shutdown();

		while (true) {
			try {
				boolean ready = service.awaitTermination(5, TimeUnit.SECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ExcelGenerator outGenerator = new ExcelGenerator(threads, "diffuse_data_"+paramsCount+"param_ponints%NUM%.xlsx", true, new Comparator<ThreadDiffuse>() {
			@Override
	        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
	        	return data1.getOutX0() > data2.getOutX0() ? 1 : -1;
	        }
		});
		outGenerator.generateExcelNew();
	}
	
	public class ThreadComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			return this.compare((ThreadDiffuse) o1, (ThreadDiffuse) o2);
		}
        	
        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
        	return data1.getCount() > data2.getCount() ? 1 : -1;
        }
    }
	
	public void calculate() {
		int xMin = -2;
		int xMax = 12;
		double NX = 28;
		double HX = (xMax - xMin) / NX;
		
		int yMin = -1;
		int yMax = 1;
		double NY = 8;
		double HY = (yMax - yMin) / NY;

		long time = System.currentTimeMillis();
		List<ThreadRlay> threads = new ArrayList<ThreadRlay>();
		ExecutorService service = Executors.newFixedThreadPool(8);
		for (double x = xMin; x <= xMax; x += HX) {
			for (double y = yMin; y <= yMax; y += HY) {
				threads.add(new ThreadRlay(new RLAYTWODIM(0.01, 0.012, 0.01, 0.05, 0.0, 0.4, 0.1, 1.0), x, y, 0.01, 0, 4, 0.5));
			}
		}
		
		total = threads.size();
		for (ThreadRlay t : threads)
			service.submit(t);
		service.shutdown();
		
		while (true) {
			try {
				boolean ready = service.awaitTermination(5, TimeUnit.SECONDS);
				if (ready) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileWriter fw = new FileWriter(new File("out.data"));
			
			fw.write("START ");
			for (double x = xMin; x <= xMax; x += HX) {
				fw.write(x + " ");
			}
			fw.write("\n");
			
			for (double y = yMin; y <= yMax; y += HY) {
				fw.write(y + " ");
				for (double x = xMin; x <= xMax; x += HX) {
					for (int i=0;i<threads.size();i++) {
						ThreadRlay t = threads.get(i);
						if(t.getX() == x && t.getY() == y) {
							fw.write(t.getResult() + " ");
							threads.remove(i);
							i--;
							break;
						}
					}
				}
				fw.write("\n");
			}
			
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long diff = System.currentTimeMillis() - time;
		System.out.println("Total time = " + diff + "ms");
		//24053ms
	}

	public static void main(String[] args) throws NAGBadIntegerException {
		Routine.init();
		Routine.setComplex(new NAGComplex());
		new Main();
	}
}
