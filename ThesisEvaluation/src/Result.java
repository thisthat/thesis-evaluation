/**
 * Created by Giovanni Liva on 21/07/2015.
 */
public class Result {
    public String name;
    public int _n;
    public double correct;
    public int maxError;
    public double RMSE;
    public double sigma;
    public double precision;
    public double recall;
    public double coverage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get_n() {
        return _n;
    }

    public void set_n(int _n) {
        this._n = _n;
    }

    public double getCorrect() {
        return correct;
    }

    public void setCorrect(double correct) {
        this.correct = correct;
    }

    public int getMaxError() {
        return maxError;
    }

    public void setMaxError(int maxError) {
        this.maxError = maxError;
    }

    public double getRMSE() {
        return RMSE;
    }

    public void setRMSE(double RMSE) {
        this.RMSE = RMSE;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    public Result() {};
}