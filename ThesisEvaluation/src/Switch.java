import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giovanni Liva on 20/07/2015.
 */
public class Switch {
    private String dpid;
    private List<String> datasets = new ArrayList<>();
    private String folder;

    public String getDpid() {
        return dpid;
    }

    public List<String> getDatasets() {
        return datasets;
    }


    public Switch(String dpid, String folder) {
        this.dpid = dpid;
        this.folder = folder;
        File f = new File(folder);
        for (final File fileEntry : f.listFiles()) {
            if (fileEntry.isFile()) {
                datasets.add(fileEntry.toString());
            }
        }
    }


}
