import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.KStar;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Created by Giovanni Liva on 20/07/2015.
 */
public class GenerateModel {
    static String initFolder = "";
    static String evalFolder = "";
    static final int howManyForecast = 5;
    static Forecast[] forecasts = new Forecast[howManyForecast];
    static Vector<Classifier> classifiers = new Vector<Classifier>();
    static boolean nb = false;
    static boolean smo = false;
    static boolean zero = false;
    static boolean bn = false;
    static boolean nn = false;
    static boolean j48 = false;
    static boolean kstar = false;

    public static void main(String[] args) {

        if(args.length < 1) {
            System.out.println("Usage: java -jar ThesisEvaluation path_learn -[nb|smo|zero|bn|nn|j48|kstar]");
            System.out.println("\t-nb:       use NaiveBayes");
            System.out.println("\t-smo:      use SMO");
            System.out.println("\t-zero:     use ZeroR");
            System.out.println("\t-bn:       use BayesNet");
            System.out.println("\t-nn:       use Neural Network with MultiLayerPerceptron");
            System.out.println("\t-j48:      use J48 Tree");
            System.out.println("\t-kstar:    use K*");

            System.exit(0);
        }
        for(int i = 1; i < args.length; i++){
            switch (args[i]){
                case "-nb": nb = true; break;
                case "-smo": smo = true; break;
                case "-zero": zero = true; break;
                case "-bn": bn = true; break;
                case "-nn": nn = true; break;
                case "-j48": j48 = true; break;
                case "-kstar": kstar = true; break;
            }
        }

        initFolder = args[0];
        run();
    }

    public static void run(){
        //Set up the Classifiers
        if(nb){
            classifiers.add(new NaiveBayes());
        }
        if(smo){
            classifiers.add(new SMO());
        }
        if(zero){
            classifiers.add(new ZeroR());
        }
        if(bn){
            classifiers.add(new BayesNet());
        }
        if(nn){
            classifiers.add(new MultilayerPerceptron());
        }
        if(j48){
            classifiers.add(new J48());
        }
        if(kstar){
            classifiers.add(new KStar());
        }

        //Collect Everything
        for(int i = 1; i <= howManyForecast; i++){
            forecasts[i-1] = new Forecast(i);
        }
        //Search only for merge.arff and write the model!
        for(int i = 0; i < howManyForecast; i++){
            List<Switch> ls = forecasts[i].getSwitches();
            for (Switch sw : ls) {
                String learningARFF = sw.getMergeARFF();
                File f = new File(learningARFF);
                for(Classifier c: classifiers) {
                    try {
                        generateModel(c, learningARFF, f.getParent());
                        //System.out.println();
                    }
                    catch (Exception e) {}
                }
            }

        }

    }

    public static void generateModel(Classifier c, String fileModel, String nameModel) throws Exception {
        String name = c.getClass().toString();
        name = name.substring(name.lastIndexOf('.') + 1);
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(fileModel);
        Instances dataset = source.getDataSet();
        //Set the index to the last column
        dataset.setClassIndex(dataset.numAttributes() - 1);
        c.buildClassifier(dataset);
        SerializationHelper.write(nameModel + "\\" + name + ".model", c);
        System.out.println("Model [" + nameModel + "\\" + name + ".model] Generated!" );
    }


}


