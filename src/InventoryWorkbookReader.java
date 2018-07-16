import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
//import org.apache.xmlbeans:xmlbeans:2.6.0;


public interface InventoryWorkbookReader {
	final static String[] colHeaders = {"Generic Name", "Compound ID", "EC50","EC90", "Purity", "Volume", 
			"Concentration (uL)","Box Number", "Source", "Instructions to aquire more", "Number of Aliquots"}; 
	
	public static void main(String[] args) throws IOException{}
	
	public static void writeInventory(ArrayList<Compound> compounds)throws IOException{
		
		try{
			Workbook wb = new HSSFWorkbook();
			CreationHelper createHelper = wb.getCreationHelper();
			Sheet sheet = wb.createSheet("Inventory");
			Row headerRow = sheet.createRow(1);
			Row compRow = sheet.createRow(2);
			for (int y = 1; y < compounds.size() + 1; y++){
				if(y > 1){
					compRow = sheet.createRow(y);
				}
				for(int x =0; x< colHeaders.length; x++){
					if(y == 1){
						headerRow.createCell(x).setCellValue(createHelper.createRichTextString(colHeaders[x]));
					}else{
						switch(colHeaders[x]){
						case "Generic Name": compRow.createCell(x).setCellValue(compounds.get(y - 1).getGenericName());break;
						case "Compound ID": compRow.createCell(x).setCellValue(compounds.get(y - 1).getCompoundID());break;
						case "EC50": compRow.createCell(x).setCellValue(compounds.get(y - 1).getEc50());break;
						case "EC90": compRow.createCell(x).setCellValue(compounds.get(y - 1).getEc90());break;
						case "Purity": compRow.createCell(x).setCellValue(compounds.get(y - 1).getPurity());break;
						case "Volume": compRow.createCell(x).setCellValue(compounds.get(y - 1).getVolume());break;
						case "Concentration (uL)": compRow.createCell(x).setCellValue(compounds.get(y - 1).getConcentration());break;
						case "Box Number": compRow.createCell(x).setCellValue(compounds.get(y - 1).getBoxNumber());break;
						case "Source": compRow.createCell(x).setCellValue(compounds.get(y - 1).getSource());break;
						case "Instructions to aquire more": compRow.createCell(x).setCellValue(compounds.get(y - 1).getAquireInstructions());break;
						case "Number of Aliquots": compRow.createCell(x).setCellValue(compounds.get(y-1).getNumberOfaAliquots());
						}
					}
				}
			}
			FileOutputStream fileOut = new FileOutputStream("Inventory.xls");
			wb.write(fileOut);
			wb.close();
			fileOut.close();
			
		} catch(IOException ex){
			System.out.println("ERROR Writing to file.");
		}finally{
			
		}
	}
	
	public static ArrayList<Compound> readInventory() throws IOException, EncryptedDocumentException, InvalidFormatException, EmptyWbkException{
		ArrayList<Compound> compounds = new ArrayList<Compound>();
		InputStream input = new FileInputStream("Inventory.xls");
		String genericName = "SampleName"; String compoundID = "0000"; double ec50 = 0; double ec90 = 0; double purity = 0; 
		double volume = 0; double concentration = 0; int boxNumber = 0; String source = "Blank"; String sourceInstructions = "Blank";
		int numberOfAliquots = 1;
		
		try{
			Workbook wb = WorkbookFactory.create(input);
			Sheet sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(2);
			if(row == null){
				System.out.println("The row was null");
			}
			Cell cell = row.getCell(0);
			
		    Compound intermediateCompound;
		    int y = 2;
		    do{
		    	row = sheet.getRow(y);
		    	for (int x = 0; x < colHeaders.length; x++){
		    		cell = row.getCell(x);
		    		if(cell == null){
						break; 
					}
		    		try{
		    			switch(x){
						case 0: genericName = cell.getStringCellValue();break;
						case 1: compoundID = cell.getStringCellValue();break;
						case 2: ec50 = Double.parseDouble(cell.getStringCellValue());break; 
						case 3: ec90 = Double.parseDouble(cell.getStringCellValue());break;
						case 4: purity = Double.parseDouble(cell.getStringCellValue());break;
						case 5: volume = Double.parseDouble(cell.getStringCellValue());break;
						case 6: concentration = Double.parseDouble(cell.getStringCellValue());break;
						case 7: boxNumber = Integer.parseInt(cell.getStringCellValue());break;
						case 8: source = cell.getStringCellValue();break;
						case 9: sourceInstructions = cell.getStringCellValue();break;
						case 10: numberOfAliquots = Integer.parseInt(cell.getStringCellValue());break;
						};
		    		}catch(IllegalStateException exW){
		    			switch(x){
						case 2: ec50 = cell.getNumericCellValue();break;
						case 3: ec90 = cell.getNumericCellValue();break;
						case 4: purity = cell.getNumericCellValue();break;
						case 5: volume = cell.getNumericCellValue();break;
						case 6: concentration =cell.getNumericCellValue();break;
						case 7: boxNumber = (int)cell.getNumericCellValue();break;
						case 10: numberOfAliquots = (int)cell.getNumericCellValue();break;
		    		}
		    	}catch(NumberFormatException exW2){
		    		if (cell.getStringCellValue().isEmpty()){
		    			switch(x){
						case 0: genericName = "Name not entered";break;
						case 1: compoundID = "ID Not Entered" ;break;
						case 2: ec50  = 0;break; 
						case 3: ec90 = 0 ;break;
						case 4: purity = 0;break;
						case 5: volume = 0;break;
						case 6: concentration = 0;break;
						case 7: boxNumber = 0;break;
						case 8: source = "No Source Entered" ;break;
						case 9: sourceInstructions = "No Instructions Entered";break;
						};
		    		}
		    	}
		    	}
		    	intermediateCompound = new Compound(genericName, compoundID, ec50, ec90, purity, source, sourceInstructions, concentration, 
		    			volume, numberOfAliquots);
		    	intermediateCompound.setBoxNumber(boxNumber);
		    	compounds.add(intermediateCompound);
		    	y++;
		    }while(sheet.getRow(y) != null);
		}catch(IOException exR){
			System.out.println("Error reading File");
			
		}catch(InvalidFormatException exR2){
			System.out.println("InValid Format");
		}catch(EncryptedDocumentException exR3){
			//This needs to change to a pop out window
			System.out.println("You do not have access to this file.");
		}catch(NullPointerException exR4){
			System.out.println("No Compounds Loaded");
		}finally{
			input.close();
		}
		
		
	    
	    return compounds;
		
		
	}
}	

class EmptyWbkException extends Exception{
	EmptyWbkException(){
		
	}
}
	
			
