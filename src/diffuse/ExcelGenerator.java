package diffuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ExcelGenerator {
	
	List<ThreadDiffuse> threads;
	private String fileName;
	private Comparator sorter;
	boolean createRowsForPoints = false;
	
	public ExcelGenerator(List<ThreadDiffuse> threads, String fileName, boolean createRowsForPoints, Comparator sorter) {
		this.threads = threads;
		this.fileName = fileName;
		this.createRowsForPoints = createRowsForPoints;
		this.sorter = sorter;
	}
	
	private void createHeaderRow(Sheet sheet, CellStyle headerCellStyle, int index,  boolean createPoints, ThreadDiffuse thread) {
		Row headerRow = sheet.createRow(index);
		
		Cell cell = headerRow.createCell(0);
	    cell.setCellValue("X0");
	    cell.setCellStyle(headerCellStyle);
	    if(Main.paramsCount > 1) { 
	    	cell = headerRow.createCell(1);
	    	cell.setCellValue("C");
	    	cell.setCellStyle(headerCellStyle);
	    }
	    if(Main.paramsCount > 2) { 
	    	cell = headerRow.createCell(2);
	    	cell.setCellValue("Z0");
	    	cell.setCellStyle(headerCellStyle);
	    }
	    cell = headerRow.createCell(3);
	    cell.setCellValue("   ");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(4);
	    cell.setCellValue("U");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(5);
	    cell.setCellValue("   ");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(6);
	    cell.setCellValue("ERROR");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(7);
	    cell.setCellValue("COUNT");
	    cell.setCellStyle(headerCellStyle);
	    
	    if(createPoints) {
			int pointCell = 8;
			
			for(Double x : thread.getxPoints()) {
				Cell cell0 = headerRow.createCell(pointCell++);
				cell0.setCellValue("xPoint " + (pointCell - 8));
				cell0.setCellStyle(headerCellStyle);
			}
			
			for(Double y : thread.getyPoints()) {
				Cell cell0 = headerRow.createCell(pointCell++);
				cell0.setCellValue("yPoint " + (pointCell - 8 - thread.getxPoints().size()));
				cell0.setCellStyle(headerCellStyle);
			}
			
			for(Double z : thread.getzPoints()) {
				Cell cell0 = headerRow.createCell(pointCell++);
				cell0.setCellValue("zPoint " + (pointCell - 8 - thread.getxPoints().size() - thread.getyPoints().size()));
				cell0.setCellStyle(headerCellStyle);
			}
	    }
	}
	
	public void generateExcelNew() {
		Collections.sort(threads, sorter);
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("diffuse_data1");
		
		 Font headerFont = workbook.createFont();
		    headerFont.setBold(true);
		    headerFont.setFontHeightInPoints((short) 12);
		    headerFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
		    
		CellStyle headerCellStyle = workbook.createCellStyle();
		    headerCellStyle.setFont(headerFont);
		    
		if(!createRowsForPoints) createHeaderRow(sheet, headerCellStyle, 0, true, null);

		int rows = 0;
	    double x0 = 0.0;
	    double c = 4.0;
	    double z0 = 0.5;
	    
		for(ThreadDiffuse thread : threads) {
			if(createRowsForPoints) createHeaderRow(sheet, headerCellStyle, rows, true, thread);
			Row row = sheet.createRow(rows+1);
			rows+=(createRowsForPoints ? 2 : 1);
			double diff = 1 - Math.abs(thread.getOutX0());
			double diff1 = 1 - Math.abs(c - thread.getOutC());
			double diff2 = 1 - Math.abs(z0 - thread.getOutZ0());
			if(Main.paramsCount > 1 && diff1 < diff) diff = diff1;
			if(Main.paramsCount > 2 && diff2 < diff) diff = diff2;
			if(diff < 0.2) diff = 0.2;
			CellStyle style = createStyle(workbook, (float) diff);
			Cell cell = row.createCell(0);
			cell.setCellValue(thread.getFormatter2().format(thread.getOutX0()));
			cell.setCellStyle(style);
			if(Main.paramsCount > 1) { 
			cell = row.createCell(1);
			cell.setCellValue(thread.getFormatter2().format(thread.getOutC()));
			cell.setCellStyle(style);
			} if(Main.paramsCount > 2) { 
			cell = row.createCell(2);
			cell.setCellValue(thread.getFormatter2().format(thread.getOutZ0()));
			cell.setCellStyle(style);
			}
			
			cell = row.createCell(3);
			cell.setCellValue("    ");
//			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue(thread.getRlay().getU());
//			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("    ");
//			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue(thread.getError());
//			cell.setCellStyle(style);
			cell = row.createCell(7);
			cell.setCellValue(thread.getCount());
//			cell.setCellStyle(style);
			
			int pointCell = 8;
			
			for(Double x : thread.getxPoints()) {
				cell = row.createCell(pointCell++);
				cell.setCellValue(x.doubleValue());
//				cell.setCellStyle(style);
			}
			
			for(Double y : thread.getyPoints()) {
				cell = row.createCell(pointCell++);
				cell.setCellValue(y.doubleValue());
//				cell.setCellStyle(style);
			}
			
			for(Double z : thread.getzPoints()) {
				cell = row.createCell(pointCell++);
				cell.setCellValue(z.doubleValue());
//				cell.setCellStyle(style);
			}
			
		}
		
//		for(int i=0;i<8;i++) {
//			sheet.autoSizeColumn(sheet.get);
//		}
		
		FileOutputStream fileOut;
		try {
			fileName = fileName.replace("%NUM%", "0");
			File f = new File(fileName);
			int i = 0;
			while(f.exists()) {
				i++;
				fileName = fileName.replace((i-1) + ".xlsx", i + ".xlsx");
				f = new File(fileName);
			}
			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Excel file " + fileName + " generated");
	}
	
	public void generateExcel() {
		List<Double[]> data = new ArrayList<Double[]>();
		File folder = new File(".", "fail");
		
		try {
			for (File fileEntry : folder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(fileEntry));
				String[] result = reader.readLine().split(" ");
				String[] params = reader.readLine().split(" ");

				Double[] outData = new Double[7];
				outData[0] = Double.parseDouble(params[3]);
				outData[1] = Double.parseDouble(params[6]);
				outData[2] = Double.parseDouble(params[9]);
				outData[3] = Double.parseDouble(params[12]);
				
				outData[4] = Double.parseDouble(result[4]);
				outData[5] = Double.parseDouble(result[7]);
				outData[6] = Double.parseDouble(result[10]);
				
				data.add(outData);

				reader.close();
			}

			folder = new File(".", "succ");
			for (File fileEntry : folder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(fileEntry));
				String[] result = reader.readLine().split(" ");
				String[] params = reader.readLine().split(" ");

				Double[] outData = new Double[7];
				outData[0] = Double.parseDouble(params[3]);
				outData[1] = Double.parseDouble(params[6]);
				outData[2] = Double.parseDouble(params[9]);
				outData[3] = Double.parseDouble(params[12]);
				
				outData[4] = Double.parseDouble(result[4]);
				outData[5] = Double.parseDouble(result[7]);
				outData[6] = Double.parseDouble(result[10]);
				
				data.add(outData);

				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(data, new DataComparator());
		
//		Map<Integer, List<Double[]>> dataCombined = new HashMap<Integer, List<Double[]>>();
//		for(Double[] data1 : data) {
//			int roundX = (int) Math.round(data1[4]);
//			
//			if(dataCombined.containsKey(roundX)) {
//				List<Double[]> dataSet = dataCombined.get(roundX);
//				dataSet.add(data1);
//			} else {
//				List<Double[]> dataSet = new ArrayList<Double[]>();
//				dataSet.add(data1);
//				dataCombined.put(roundX, dataSet);
//			}
//		}
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("diffuse_data");
		
		 Font headerFont = workbook.createFont();
		    headerFont.setBold(true);
		    headerFont.setFontHeightInPoints((short) 14);
		    headerFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
		    
		CellStyle headerCellStyle = workbook.createCellStyle();
		    headerCellStyle.setFont(headerFont);
		    
		Row headerRow = sheet.createRow(0);
		
		Cell cell = headerRow.createCell(0);
	    cell.setCellValue("XMIN");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(1);
	    cell.setCellValue("XMAX");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(2);
	    cell.setCellValue("COUNT");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(3);
	    cell.setCellValue("ERROR");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(4);
	    cell.setCellValue("   ");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(5);
	    cell.setCellValue("X0");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(6);
	    cell.setCellValue("C");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(7);
	    cell.setCellValue("Z0");
	    cell.setCellStyle(headerCellStyle);
	    
	    cell = headerRow.createCell(12);
	    cell.setCellValue("XMIN");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(13);
	    cell.setCellValue("XMAX");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(14);
	    cell.setCellValue("COUNT");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(15);
	    cell.setCellValue("ERROR");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(16);
	    cell.setCellValue("   ");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(17);
	    cell.setCellValue("X0");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(18);
	    cell.setCellValue("C");
	    cell.setCellStyle(headerCellStyle);
	    cell = headerRow.createCell(19);
	    cell.setCellValue("Z0");
	    cell.setCellStyle(headerCellStyle);
	    
	    double x0 = 0.0;
	    double c = 4.0;
	    double z0 = 0.5;
		
//		Iterator<Integer> keys = dataCombined.keySet().iterator();
		int rows = 1;
		int succIndex = 1;
//		while(keys.hasNext()) {
//			Integer key = keys.next();
//			List<Double[]> dataSet = dataCombined.get(key);
			for(Double[] data1 : data) {
				Row row = sheet.createRow(rows++);
				double diff = 1 - Math.abs(data1[4]);
				double diff1 = 1 - Math.abs(c - data1[5]);
				double diff2 = 1 - Math.abs(z0 - data1[6]);
				if(diff1 < diff) diff = diff1;
				if(diff2 < diff) diff = diff2;
				if(diff < 0.2) diff = 0.2;
//				diff *= 255;

				CellStyle style = createStyle(workbook, (float) diff);
				cell = row.createCell(0);
				cell.setCellValue(data1[0]);
				cell.setCellStyle(style);
				cell = row.createCell(1);
				cell.setCellValue(data1[1]);
				cell.setCellStyle(style);
				cell = row.createCell(2);
				cell.setCellValue(data1[2]);
				cell.setCellStyle(style);
				cell = row.createCell(3);
				cell.setCellValue(data1[3]);
				cell.setCellStyle(style);
				
				cell = row.createCell(4);
				cell.setCellValue("   ");
				cell.setCellStyle(style);
				
				cell = row.createCell(5);
				cell.setCellValue(data1[4]);
				cell.setCellStyle(style);
				cell = row.createCell(6);
				cell.setCellValue(data1[5]);
				cell.setCellStyle(style);
				cell = row.createCell(7);
				cell.setCellValue(data1[6]);
				cell.setCellStyle(style);
				
				if(diff >= 0.9f) {
					row = sheet.getRow(succIndex++);
					cell = row.createCell(12);
					cell.setCellValue(data1[0]);
					cell.setCellStyle(style);
					cell = row.createCell(13);
					cell.setCellValue(data1[1]);
					cell.setCellStyle(style);
					cell = row.createCell(14);
					cell.setCellValue(data1[2]);
					cell.setCellStyle(style);
					cell = row.createCell(15);
					cell.setCellValue(data1[3]);
					cell.setCellStyle(style);
					
					cell = row.createCell(16);
					cell.setCellValue("   ");
					cell.setCellStyle(style);
					
					cell = row.createCell(17);
					cell.setCellValue(data1[4]);
					cell.setCellStyle(style);
					cell = row.createCell(18);
					cell.setCellValue(data1[5]);
					cell.setCellStyle(style);
					cell = row.createCell(19);
					cell.setCellValue(data1[6]);
					cell.setCellStyle(style);
				}
			}
//		}
		
		for(int i=0;i<8;i++) {
			sheet.autoSizeColumn(i);
		}
		
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("diffuse_data.xlsx");
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public class DataComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			return this.compare((Double[]) o1, (Double[]) o2);
		}
        	
        public int compare(Double[] data1, Double[] data2) {
        	return data1[4] > data2[4] ? 1 : -1;
        }
    }
	
	public class ThreadComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			return this.compare((ThreadDiffuse) o1, (ThreadDiffuse) o2);
		}
        	
        public int compare(ThreadDiffuse data1, ThreadDiffuse data2) {
        	return data1.getOutX0() > data2.getOutX0() ? 1 : -1;
        }
    }
	
	Map<Float, CellStyle> styles = new HashMap<Float, CellStyle>();
	DefaultIndexedColorMap mapColors = new DefaultIndexedColorMap();
	
	public CellStyle createStyle(XSSFWorkbook workbook, float diff) {
		if(styles.containsKey(diff)) {
			return styles.get(Float.valueOf(diff));
		} else {
//			System.out.println(diff);
//			XSSFColor customColor = new XSSFColor(new Color(diff, diff, diff)); //new XSSFColor(new byte[] {(byte) diff, (byte) diff, (byte) diff}, new DefaultIndexedColorMap());//setColor(workbook, (byte) diff, (byte) diff, (byte) diff);
			XSSFCellStyle  style = workbook.createCellStyle();
//			style.setFillBackgroundColor(customColor.getIndex());
//			style.setFillForegroundColor(customColor.getIndex());
			style.setFillForegroundColor(new XSSFColor(new java.awt.Color(diff, diff, diff), mapColors));
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
			styles.put(Float.valueOf(diff), style);
			return style;
		}
	}
	
//	public XSSFColor setColor(XSSFWorkbook workbook, byte r,byte g, byte b){
//	    XSSFPalette palette = workbook.getCustomPalette();
//	    XSSFColor hssfColor = null;
//	    try {
//	        hssfColor= palette.findColor(r, g, b); 
//	        if (hssfColor == null ){
//	            palette.setColorAtIndex(IndexedColors.LAVENDER.index, r, g,b);
//	            hssfColor = palette.getColor(IndexedColors.LAVENDER.index);
//	        }
//	    } catch (Exception e) {
//	    	e.printStackTrace();
//	    }
//
//	    return hssfColor;
//	}
}
