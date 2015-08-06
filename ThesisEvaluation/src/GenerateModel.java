import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import sun.awt.windows.ThemeReader;
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
    static int classCorrectness = 0;
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
    static boolean evaluate2 = false;

    public GenerateModel() {
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar ThesisEvaluation path_learn -[nb|smo|zero|bn|nn|j48|kstar] [-g] [-e] [-e2 n] ");
            System.out.println("\t-nb:       use NaiveBayes");
            System.out.println("\t-smo:      use SMO");
            System.out.println("\t-zero:     use ZeroR");
            System.out.println("\t-bn:       use BayesNet");
            System.out.println("\t-nn:       use Neural Network with MultiLayerPerceptron");
            System.out.println("\t-j48:      use J48 Tree");
            System.out.println("\t-kstar:    use K*");
            System.out.println("Use only one of the following");
            System.out.println("\t-g: Generate the files w/ the model");
            System.out.println("\t-e: Evaluate in default manner");
            System.out.println("\t-e2: Evaluate with correctness of +- n classes [default 0]");
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
                    break;
                case  "-e2":
                    evaluate = evaluate2 = true;
                    if(i < args.length -1){
                        classCorrectness = Integer.parseInt(args[i+1]);
                    }
            }
        }
        if(generate) {
            System.out.println("[OPT] Generate activated");
        }
        if(evaluate) {
            System.out.println("[OPT] Normal Evaluation");
        }
        if(evaluate2) {
            System.out.println("[OPT] Evaluation with number " + classCorrectness);
        }


        initFolder = args[0];
        if(!initFolder.endsWith("\\")) {
            initFolder = initFolder + "\\";
        }

        //Collect Info
        for(int i = 1; i <= howManyForecast; ++i) {
            forecasts[i - 1] = new Forecast(i);
        }

        if(generate) {
            run();
        }
        else {
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

    public static void testModel(){

        Thread[] th = new Thread[howManyForecast];
        //Get Evaluation Results
        for(int i = 0; i < howManyForecast; ++i) {

            final int finalI = i;
            th[i] = new Thread(new Runnable() {
                public void run() {
                    // code goes here.
                    List<Switch> ls = forecasts[finalI].getSwitches();
                    for(Switch sw : ls) {
                        String evalARFF = sw.getMergeARFF();
                        List<String> swClassifiers = sw.getModels();
                        for(String classifier: swClassifiers) {
                            try {
                                Result r;
                                Classifier c = (Classifier) SerializationHelper.read(classifier);
                                String cName = c.getClass().toString();
                                cName = cName.substring(cName.lastIndexOf('.') + 1);
                                r = evaluate2 ? evaluateModel(c, evalARFF, classCorrectness) : evaluateModel(c, evalARFF);
                                sw.getMap().put(cName, r);
                                System.out.println("[Forecast " + finalI + "] Classifier [" + cName + "] for Switch [" + sw.getDpid() + "] Evaluated!");
                            } catch (Exception e){
                                System.err.println(e.toString());
                            }
                        }
                    }
                }
            });
            th[i].start();
        }

        for(int i = 0; i < howManyForecast; ++i) {
            try {
                th[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            forecasts[i].writeFile();
        }

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
        double coef;
        double tmp;
        r.maxError = maxErr;
        r._n = dataset.numInstances();
        r.correct = (double)num / (double)dataset.numInstances() * 100.0D;
        tmp = (double)num / (double)dataset.numInstances();
        coef = 1.96 * Math.sqrt(tmp * (1-tmp) / r._n) * 100;
        r.correctCoef = coef;
        r.sigma = (double)totErr / (double)dataset.numInstances();
        tmp = (r.sigma / 19f);
        coef = 1.96 * Math.sqrt(tmp * (1-tmp) / 19f) * 19;
        //System.out.println(coef);
        r.sigmaCoef = coef;
        r.RMSE = eval.rootMeanSquaredError();
        coef =  1.96 * Math.sqrt( r.RMSE * (1- r.RMSE) / r._n  );
        r.RMSECoef = coef;
        r.precision = eval.weightedPrecision();
        coef =  1.96 * Math.sqrt( r.precision * (1- r.precision) / r._n  );
        r.precisionCoef = coef;
        r.recall = eval.weightedRecall();
        coef =  1.96 * Math.sqrt( r.recall * (1- r.recall) / r._n  );
        r.recallCoef = coef;
        r.coverage = eval.coverageOfTestCasesByPredictedRegions();
        return r;
    }

    public static Result evaluateModel(Classifier c, String fileModel, int classToCover) throws Exception {
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
            String actual = dataset.classAttribute().value((int) actualClass);
            Instance newInst = dataset.instance(r);
            int predictClass = (int) c.classifyInstance(newInst);
            String predict = dataset.classAttribute().value((int) predictClass);

            int _min = Math.max(0, (int) actualClass - classToCover);
            int _max = (int)actualClass + classToCover;
            if( _min <= predictClass && _max >= predictClass){
                //No error just increment the counter of correct class predicted
                ++num;
            }
            else {
                //Error
                int indexError = predictClass - (int)actualClass;
                indexError = Math.max( Math.abs(indexError) - classToCover, 0 );

                if(indexError > maxErr) {
                    maxErr = indexError;
                }

                totErr += indexError;
            }



        }

        Result r = new Result();
        double coef;
        double tmp;
        r.maxError = maxErr;
        r._n = dataset.numInstances();
        r.correct = (double)num / (double)dataset.numInstances() * 100.0D;
        tmp = (double)num / (double)dataset.numInstances();
        coef = 1.96 * Math.sqrt(tmp * (1-tmp) / r._n) * 100;
        r.correctCoef = coef;
        r.sigma = (double)totErr / (double)dataset.numInstances();
        tmp = (r.sigma / 19f);
        coef = 1.96 * Math.sqrt(tmp * (1-tmp) / 19f) * 19;
        r.sigmaCoef = coef;
        r.RMSE = eval.rootMeanSquaredError();
        coef =  1.96 * Math.sqrt( r.RMSE * (1- r.RMSE) / r._n  );
        r.RMSECoef = coef;
        r.precision = eval.weightedPrecision();
        coef =  1.96 * Math.sqrt( r.precision * (1- r.precision) / r._n  );
        r.precisionCoef = coef;
        r.recall = eval.weightedRecall();
        coef =  1.96 * Math.sqrt( r.recall * (1- r.recall) / r._n  );
        r.recallCoef = coef;
        r.coverage = eval.coverageOfTestCasesByPredictedRegions();
        return r;
    }

}