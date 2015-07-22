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
        HSSFSheet sheet = workbook.createSheet("FirstSheet");
        int swCouter = 0;
        int _nRow = 10;
        //Num of value in Results structure
        int _max_val = 8;
        int _maxCol = 10;
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
            HSSFRow head = sheet.createRow((short)(3 + _nRow * swCouter));
            HSSFRow headhead = sheet.createRow((short)(2 + _nRow * swCouter));
            HSSFRow swhead = sheet.createRow((short)(2 + _nRow * swCouter));
            swhead.createCell(1).setCellValue(sw.getDpid());
            headhead.createCell(1).setCellValue("Classifier");
            head.createCell(2).setCellValue("Max Error");
            head.createCell(3).setCellValue("% Correct");
            head.createCell(4).setCellValue("Sigma");
            head.createCell(5).setCellValue("RMSE");
            head.createCell(6).setCellValue("Precision");
            head.createCell(7).setCellValue("Recall");
            head.createCell(8).setCellValue("Coverage (0.95)");


            //Body
            for(String cName : sw.getMap().keySet()){

            }
        }
        Classifier _first = classifiers.firstElement();
        String _firstname = _first.getClass().toString();
        Vector<Result> _firstrv = map.get(_firstname);





        int i = 0;
        for(Classifier c: classifiers){
            String name = c.getClass().toString();
            Vector<Result> rv = map.get(name);
            HSSFRow row = sheet.createRow((short)3 + i++);
            row.createCell(0).setCellValue(name.substring(name.lastIndexOf('.') + 1));

        }
        int maxRow = i;
        //Get the first sheet

        //HSSFCell c = sheet.getRow(4).getCell(4);
        //c.setCellStyle(style);

        //HIGHLIGHT MAX/MIN VAL
        //System.out.println("Start looking through cells " + maxRow + "::" + maxCol);
        double _maxVal = 0;
        double _minVal = Double.MAX_VALUE;
        int pos_x = 0,pos_y = 0;
        int min_x = 0,min_y = 0;
        for(int col = 2; col < maxCol; col++){
            _maxVal = 0;
            _minVal = Double.MAX_VALUE;
            for(int r = 3; r < 3+maxRow; r++){
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

        try {
            FileOutputStream fileOut = new FileOutputStream("forecast_1.xls");
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Forecast " + this._id + " report generated!");
    }
}
