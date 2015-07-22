import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.KStar;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Created by Giovanni Liva on 20/07/2015.
 */

public class GenerateModel {
    static String initFolder = "";
    static String evalFolder = "";
    static final int howManyForecast = 5;
    static Forecast[] forecasts = new Forecast[5];
    static Vector<Classifier> classifiers = new Vector();
    static boolean nb = false;
    static boolean smo = false;
    static boolean zero = false;
    static boolean bn = false;
    static boolean nn = false;
    static boolean j48 = false;
    static boolean kstar = false;
    static boolean generate = false;
    static boolean evaluate = false;

    public GenerateModel() {
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar ThesisEvaluation path_learn -[nb|smo|zero|bn|nn|j48|kstar] [-g] [-e path_eval]");
            System.out.println("\t-nb:       use NaiveBayes");
            System.out.println("\t-smo:      use SMO");
            System.out.println("\t-zero:     use ZeroR");
            System.out.println("\t-bn:       use BayesNet");
            System.out.println("\t-nn:       use Neural Network with MultiLayerPerceptron");
            System.out.println("\t-j48:      use J48 Tree");
            System.out.println("\t-kstar:    use K*");
            System.out.println("");
            System.out.println("\t-g: Generate the files w/ the model");
            System.out.println("\t-e: Evaluate");
            System.exit(0);
        }

        for(int i = 1; i < args.length; ++i) {
            switch(args[i]) {
                case "-nb":
                    nb = true;
                    break;
                case "-smo":
                    smo = true;
                    break;
                case  "-zero":
                    zero = true;
                    break;
                case  "-bn":
                    bn = true;
                    break;
                case  "-nn":
                    nn = true;
                    break;
                case  "-j48":
                    j48 = true;
                    break;
                case  "-kstar":
                    kstar = true;
                    break;
                case  "-g":
                    generate = true;
                    break;
                case  "-e":
                    evaluate = true;
                    evalFolder = args[i+1];
            }
        }

        initFolder = args[0];
        if(!initFolder.endsWith("\\")) {
            initFolder = initFolder + "\\";
        }
        if(!evalFolder.endsWith("\\")) {
            evalFolder = evalFolder + "\\";
        }

        if(generate) {
            run();
        }
        if(evaluate){
            testModel();
        }

        System.out.println("Everything done! Bye :)");
    }

    public static void run() {
        //Add Classifiers
        if(nb) {
            classifiers.add(new NaiveBayes());
        }
        if(smo) {
            classifiers.add(new SMO());
        }
        if(zero) {
            classifiers.add(new ZeroR());
        }
        if(bn) {
            classifiers.add(new BayesNet());
        }
        if(nn) {
            classifiers.add(new MultilayerPerceptron());
        }
        if(j48) {
            classifiers.add(new J48());
        }
        if(kstar) {
            classifiers.add(new KStar());
        }

        //Collect Info
        for(int i = 1; i <= howManyForecast; ++i) {
            forecasts[i - 1] = new Forecast(i);
        }

        //Create Model
        for(int i = 0; i < howManyForecast; ++i) {
            List<Switch> ls = forecasts[i].getSwitches();
            for(Switch sw : ls) {
                String learningARFF = sw.getMergeARFF();
                File f = new File(learningARFF);
                 for(Classifier c: classifiers) {
                    try {
                        generateModel(c, learningARFF, f.getParent());
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                }
            }
        }

    }

    public static void generateModel(Classifier c, String fileModel, String nameModel) throws Exception {
        String name = c.getClass().toString();
        name = name.substring(name.lastIndexOf(46) + 1);
        DataSource source = new DataSource(fileModel);
        Instances dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes() - 1);
        c.buildClassifier(dataset);
        SerializationHelper.write(nameModel + "\\" + name + ".model", c);
        System.out.println("Model [" + nameModel + "\\" + name + ".model] Generated!");
    }

    public static void testModel() {
        //Collect
        for(int i = 1; i <= howManyForecast; ++i) {
            forecasts[i - 1] = new Forecast(i);
        }
        //Get Evaluation Results
        for(int i = 0; i < howManyForecast; ++i) {
            List<Switch> ls = forecasts[i].getSwitches();
            for(Switch sw : ls) {
                String evalARFF = sw.getMergeARFF();
                File f = new File(evalARFF);
                for(Classifier c: classifiers) {
                    Result r;
                    String cName = c.getClass().toString();
                    cName = cName.substring(cName.lastIndexOf('.') + 1);
                    try {
                        r = evaluateModel(c, evalARFF);
                        sw.getMap().put(cName, r);
                    } catch (Exception e){
                        System.err.println(e.toString());
                    }
                }
            }
        }

        boolean var5 = false;
    }

    public static Result evaluateModel(Classifier c, String fileModel) throws Exception {
        DataSource source = new DataSource(fileModel);
        Instances dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes() - 1);
        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(c, dataset, 10, new Random(), new Object[0]);
        int num = 0;
        int totErr = 0;
        int maxErr = 0;

        for(int r = 0; r < dataset.numInstances(); ++r) {
            double actualClass = dataset.instance(r).classValue();
            String actual = dataset.classAttribute().value((int)actualClass);
            Instance newInst = dataset.instance(r);
            double predictClass = c.classifyInstance(newInst);
            String predict = dataset.classAttribute().value((int)predictClass);
            int indexError = (int)predictClass - (int)actualClass;
            indexError = Math.abs(indexError);
            if(indexError > maxErr) {
                maxErr = indexError;
            }

            totErr += indexError;
            if(actual.equals(predict)) {
                ++num;
            }
        }

        Result r = new Result();
        r.maxError = maxErr;
        r._n = dataset.numInstances();
        r.correct = (double)num / (double)dataset.numInstances() * 100.0D;
        r.sigma = (double)totErr / (double)dataset.numInstances();
        r.RMSE = eval.rootMeanSquaredError();
        r.precision = eval.weightedPrecision();
        r.recall = eval.weightedRecall();
        r.coverage = eval.coverageOfTestCasesByPredictedRegions();
        return r;
    }
}