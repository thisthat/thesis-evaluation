import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Giovanni Liva on 20/07/2015.
 */
public class Switch {
    private String dpid;
    private List<String> datasets = new ArrayList<>();
    private List<String> datasetsEval = new ArrayList<>();
    private List<String> models = new ArrayList<>();
    private String folder;
    private Map<String, Result> map = new HashMap<String, Result>();

    public Map<String, Result> getMap() {
        return map;
    }

    public void setMap(Map<String, Result> map) {
        this.map = map;
    }

    public String getDpid() {
        return dpid;
    }

    public List<String> getDatasets() {
        return datasets;
    }

    public List<String> getModels() {
        return models;
    }


    public Switch(String dpid, String folder) {
        this.dpid = dpid;
        this.folder = folder;
        File f = new File(folder);
        for (final File fileEntry : f.listFiles()) {
            String ext = getExt(fileEntry.toString());
            if (fileEntry.isFile() && ext.equals("arff")) {
                datasets.add(fileEntry.toString());
            }
            else if(fileEntry.isFile() && ext.equals("model")){
                models.add(fileEntry.toString());
            }
        }
    }

    public static String getExt(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }
        return  extension;
    }

    public String getMergeARFF(){
        for (String temp : datasets) {
            if(temp.contains("merge.arff")){
                //System.out.println(temp);
                return temp;
            }
        }
        return datasets.get(0);
    }


}
