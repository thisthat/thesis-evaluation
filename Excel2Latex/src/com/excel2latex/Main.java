package com.excel2latex;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;


public class Main {

    public static int _maxRow = 134;
    public static int _maxCol = 19;

    public static String testType = "c-s";
    public static String testName = "random5";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        if(args.length != 1){
            System.out.println("Usage: excel2latex path");
            return;
        }
        String filename = args[0];


        File file = new File(filename);

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        for(int i = 0; i < 5; i++){
            XSSFSheet sheet = workbook.getSheetAt(i);
            printTableLatex(sheet, "forecast_" + (i+1) + ".tex", i+1);
        }

    }

    public static void printTable(XSSFSheet sheet){
        int r = 2;
        int c = 1;
        int[] admittedCells = {1,2,4,7,10};
        while(r < _maxRow){
            c = 1;
            while(c < _maxCol){
                if(Arrays.asList(admittedCells).contains(c)){
                    try {
                        XSSFRow row = sheet.getRow(r);
                        XSSFCell cell = row.getCell(c);
                        switch(c){
                            case 1: //Name
                                System.out.print(cell.toString() + " ");
                                break;
                            case 2: //Max Error
                                try {
                                    System.out.print( (int)Double.parseDouble(cell.toString()) + "\t");
                                }
                                catch(Exception e){
                                    //Allora è un header
                                    System.out.print( cell.toString() + "\t");
                                }
                                break;
                            default: //Other
                                System.out.print( formatCell(cell.toString()) + "\t");
                        }
                        //System.out.print(cell.toString() + " ");
                    }
                    catch(Exception e){
                        //Skip the row and restart
                        //System.err.println(e.toString());
                        //r++;
                        //c = 0;
                    }
                }
                c++;
            }
            System.out.print("\n");
            r++;
        }
    }

    public static void printTableLatex(XSSFSheet sheet, String fileOutput, int forecast) throws FileNotFoundException, UnsupportedEncodingException {
        int r = 2;
        int c = 1;
        int[] admittedCells = {1,2,4,7,10,13,16,18};
        XSSFRow row;
        XSSFCell cell;
        PrintWriter writer = new PrintWriter(fileOutput, "UTF-8");
        while(r < _maxRow) {
            row = sheet.getRow(r);
            cell = row.getCell(c);
            //Get title
            String title = cell.toString();
            r++;
            //Get header
            String[] header = new String[admittedCells.length];
            row = sheet.getRow(r);
            int j = 0;
            for(int i = 1; i < _maxCol; i++){
                if(!inArray(admittedCells,i)){
                    continue;
                }
                header[j++] = row.getCell(i).toString();
            }
            //Skip empty row
            r++;
            r++;
            //Get MPL
            String[] MPL = new String[admittedCells.length];
            row = sheet.getRow(r);
            j = 0;
            for(int i = 1; i < _maxCol; i++){
                if(!inArray(admittedCells,i)){
                    continue;
                }
                switch(i){
                    case 1: //Name
                        MPL[j++] = row.getCell(i).toString();
                        break;
                    case 2: //Max Error
                        MPL[j++] = (int)Double.parseDouble(row.getCell(i).toString()) + "";
                        break;
                    default: //Other
                        MPL[j++] = formatCell(row.getCell(i).toString());
                }
            }
            r++;
            //Get BN
            String[] BN = new String[admittedCells.length];
            row = sheet.getRow(r);
            j = 0;
            for(int i = 1; i < _maxCol; i++){
                if(!inArray(admittedCells,i)){
                    continue;
                }
                switch(i){
                    case 1: //Name
                        BN[j++] = row.getCell(i).toString();
                        break;
                    case 2: //Max Error
                        BN[j++] = (int)Double.parseDouble(row.getCell(i).toString()) + "";
                        break;
                    default: //Other
                        BN[j++] = formatCell(row.getCell(i).toString());
                }
            }
            r++;
            //Get SMO
            String[] SMO = new String[admittedCells.length];
            row = sheet.getRow(r);
            j = 0;
            for(int i = 1; i < _maxCol; i++){
                if(!inArray(admittedCells,i)){
                    continue;
                }
                switch(i){
                    case 1: //Name
                        SMO[j++] = row.getCell(i).toString();
                        break;
                    case 2: //Max Error
                        SMO[j++] = (int)Double.parseDouble(row.getCell(i).toString()) + "";
                        break;
                    default: //Other
                        SMO[j++] = formatCell(row.getCell(i).toString());
                }
                //SMO[i-1] = row.getCell(i).toString();
            }
            //Get to next table
            r += 4;
            printTable(title, header, MPL, BN, SMO, writer, forecast);
        }
        writer.close();
    }

    private static boolean inArray(int[] admittedCells, int i) {
        for(int j = 0; j < admittedCells.length; j++){
            if(admittedCells[j] == i) return true;
        }
        return false;
    }

    private static String formatCell(String s) {
        //return s;
        try {
            int n_cifre_round = 3;
            double mul = Math.pow(10, n_cifre_round);
            double d = Double.parseDouble(s) * mul;
            double out = (Math.round(d) / mul);
            //System.out.println("Input: " + s + "\nOutput: " + out);
            return out + "";
        }
        catch (Exception e){
            return s;
        }

    }

    private static String printArray(String[] a){
        String out = "";
        for(int i = 0; i < a.length - 1; i++){
            String elm = a[i];
            elm = elm.replace("%","\\%");
            out += elm + "&\t";
        }
        out += (a[a.length - 1] + "\t");
        out += "\\\\\n";
        return out;
    }

    private static void printTable(String title, String[] header, String[] MPL, String[] BN, String[] SMO,PrintWriter file, int forecast){
        file.println("");
        file.println("\\begin{table}[h!]");
        file.println("\\centering");
        file.println("\\resizebox{1\\textwidth}{!}{%");
        file.println("\\begin{tabular}{r|ccccccccccccccccc}");
        file.println(printArray(header));
        file.println("\\hline");
        file.println(printArray(MPL));
        file.println(printArray(BN));
        file.println(printArray(SMO));
        file.println("\\end{tabular}%");
        file.println("}");
        file.println("\\caption{" + title + "}");
        file.println("\\label{tab:" + testType + "_table_" + testName + "_switch_" + title +  "_forecast" + forecast + "}%");
        file.println("\\end{table}%");

    }


}
