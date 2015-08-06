import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import weka.gui.beans.Classifier;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by Giovanni Liva on 20/07/2015.
 */
public class Forecast {
    private int _id;
    private List<Switch> switches = new ArrayList<>();
    private List<Switch> switchesEval = new ArrayList<>();
    private String path = "forecast_";

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public List<Switch> getSwitches() {
        return switches;
    }

    public void setSwitches(List<Switch> switches) {
        this.switches = switches;
    }

    public String getPath() {
        return path + _id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Forecast(int id){
        this._id = id;
        generateSwitch();
    }

    public void generateSwitch() {
        String path = GenerateModel.initFolder + this.getPath() + "\\";
        File folder = new File(path);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                String switchName = fileEntry.toString().substring(fileEntry.toString().lastIndexOf("\\") + 1);
                Switch sw = new Switch(switchName, fileEntry.toString());
                switches.add(sw);
            }
        }
    }

    public void writeFile(){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("forecast_" + _id);
        int swCouter = 0;
        int _nRow = 2;
        //Num of value in Results structure
        int _max_val = 8;
        int _maxCol = 19;
        int space = 4;
        //DEFAULT STYLE
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.COLOR_RED);
        font.setColor(HSSFColor.RED.index);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.RED.index);

        HSSFFont fontMin = workbook.createFont();
        fontMin.setBoldweight(HSSFFont.COLOR_NORMAL);
        fontMin.setColor(HSSFColor.BLUE.index);
        HSSFCellStyle styleMin= workbook.createCellStyle();
        styleMin.setFont(fontMin);
        styleMin.setFillForegroundColor(HSSFColor.BLUE.index);

        //FulFill the Sheet
        for(Switch sw : getSwitches()){

            //Title
            HSSFRow swhead = sheet.createRow((short)(_nRow));
            _nRow++;
            HSSFRow head = sheet.createRow((short)(_nRow));
            _nRow++;
            swhead.createCell(1).setCellValue(sw.getDpid());
            head.createCell(1).setCellValue("Classifier");
            head.createCell(2).setCellValue("Max Error");
            head.createCell(3).setCellValue("[MIN]% Correct");
            head.createCell(4).setCellValue("% Correct");
            head.createCell(5).setCellValue("[MAX]% Correct");
            head.createCell(6).setCellValue("[MIN]Sigma");
            head.createCell(7).setCellValue("Sigma");
            head.createCell(8).setCellValue("[MAX]Sigma");
            head.createCell(9).setCellValue("[MIN]RMSE");
            head.createCell(10).setCellValue("RMSE");
            head.createCell(11).setCellValue("[MAX]RMSE");
            head.createCell(12).setCellValue("[MIN]Precision");
            head.createCell(13).setCellValue("Precision");
            head.createCell(14).setCellValue("[MAX]Precision");
            head.createCell(15).setCellValue("[MIN]Recall");
            head.createCell(16).setCellValue("Recall");
            head.createCell(17).setCellValue("[MAX]Recall");
            head.createCell(18).setCellValue("Coverage (0.95)");
            _nRow++;
            int _minRow = _nRow;
            //Body
            for(String cName : sw.getMap().keySet()){
                Result r = sw.getMap().get(cName);
                HSSFRow row = sheet.createRow((short)(_nRow));
                row.createCell(1).setCellValue(cName);
                row.createCell(2).setCellValue(r.maxError);
                row.createCell(3).setCellValue(r.correct - r.correctCoef);
                row.createCell(4).setCellValue(r.correct);
                row.createCell(5).setCellValue(r.correct + r.correctCoef);
                row.createCell(6).setCellValue(r.sigma - r.sigmaCoef);
                row.createCell(7).setCellValue(r.sigma);
                row.createCell(8).setCellValue(r.sigma + r.sigmaCoef);
                row.createCell(9).setCellValue(r.RMSE - r.RMSECoef);
                row.createCell(10).setCellValue(r.RMSE);
                row.createCell(11).setCellValue(r.RMSE + r.RMSECoef);
                row.createCell(12).setCellValue(r.precision - r.precisionCoef);
                row.createCell(13).setCellValue(r.precision);
                row.createCell(14).setCellValue(r.precision + r.precisionCoef);
                row.createCell(15).setCellValue(r.recall - r.recallCoef);
                row.createCell(16).setCellValue(r.recall);
                row.createCell(17).setCellValue(r.recall + r.recallCoef);
                row.createCell(18).setCellValue(r.coverage);
                _nRow++;
            }
            int _maxRow = _nRow;
            swCouter++;
            _nRow += space;
            //HIGHLIGHT MAX/MIN VAL
            //System.out.println("Start looking through cells " + maxRow + "::" + maxCol);
            double _maxVal = 0;
            double _minVal = Double.MAX_VALUE;
            int pos_x = 0,pos_y = 0;
            int min_x = 0,min_y = 0;
            for(int col = 2; col < _maxCol; col++){
                _maxVal = 0;
                _minVal = Double.MAX_VALUE;
                for(int r = _minRow; r <= _maxRow; r++){
                    try {
                        HSSFCell c = sheet.getRow(r).getCell(col);
                        switch(c.getCellType()){
                            case Cell.CELL_TYPE_NUMERIC:
                                if(_maxVal < c.getNumericCellValue()){
                                    _maxVal = c.getNumericCellValue();
                                    pos_x = r;
                                    pos_y = col;
                                }
                                if(_minVal > c.getNumericCellValue()){
                                    _minVal = c.getNumericCellValue();
                                    min_x = r;
                                    min_y = col;
                                }
                                break;
                        }
                    }
                    catch(Exception e){
                        //Not defined cell -> not a big problem just skip
                    }
                }
                HSSFCell c = sheet.getRow(pos_x).getCell(pos_y);
                c.setCellStyle(style);
                c = sheet.getRow(min_x).getCell(min_y);
                c.setCellStyle(styleMin);
                //System.exit(0);
            }

        }
        try {
            FileOutputStream fileOut = new FileOutputStream("forecast_" + _id + ".xls");
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Forecast " + this._id + " report generated!");
    }
}
