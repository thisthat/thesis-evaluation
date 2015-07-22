import java.io.File;
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
        
    }
}
