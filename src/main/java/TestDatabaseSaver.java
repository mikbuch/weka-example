import com.emaraic.ml.ModelClassifier;
import com.emaraic.ml.ModelGenerator;
import com.emaraic.ml.DatabaseManager;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Debug;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

/**
 * @author Mikolaj Buchwald
 * Website: https://mikbuch.github.io
 * Email: mikolaj.buchwald@gmail.com
 * Created on: Jan 9, 2019
 * Github link: https://github.com/mikbuch/weka-example-gradle
 *
 * Mikolaj's example is heavily based on Taha's code, see:
 * Author Taha Emara
 * Website: http://www.emaraic.com 
 * Email : taha@emaraic.com
 * Created on: Jul 1, 2017
 * Github link: https://github.com/emara-geek/weka-example
 *
 * I'm gratefult to Taha for sharing this code and explaining it in blog article
 * here: http://emaraic.com/blog/weka-java-example
 * It help me a lot in understanding how to use Weka with JAVA code.
 */
public class TestDatabaseSaver {

    private static final String DATASET_PATH = "data/iris.2D.arff";
    private static final String MODEL_PATH = "data/model.bin";

    public static void main(String[] args) throws Exception {
        
        ModelGenerator mg = new ModelGenerator();

        // Loading a dataset from an ARFF file.
        Instances dataset = mg.loadDataset(DATASET_PATH);

        /**
         * Saving to the database
         *
         * You have to have a database instance running.
         */
        DatabaseManager dsmgr = new DatabaseManager();
        // Save dataset to tablename.
        dsmgr.saveToDatabase(dataset, "iris");

        Filter filter = new Normalize();

        // divide dataset to train dataset 80% and test dataset 20%
        int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
        int testSize = dataset.numInstances() - trainSize;

        dataset.randomize(new Debug.Random(1));// if you comment this line the accuracy of the model will be droped from 96.6% to 80%
        
        //Normalize dataset
        filter.setInputFormat(dataset);
        Instances datasetnor = Filter.useFilter(dataset, filter);

        Instances traindataset = new Instances(datasetnor, 0, trainSize);
        Instances testdataset = new Instances(datasetnor, trainSize, testSize);

        // build classifier with train dataset             
        MultilayerPerceptron ann = (MultilayerPerceptron) mg.buildClassifier(traindataset);

        // Evaluate classifier with test dataset
        String evalsummary = mg.evaluateModel(ann, traindataset, testdataset);
        System.out.println("Evaluation: " + evalsummary);

        //Save model 
        mg.saveModel(ann, MODEL_PATH);

        //classifiy a single instance 
        ModelClassifier cls = new ModelClassifier();
        String classname =cls.classifiy(Filter.useFilter(cls.createInstance(1.6, 0.2, 0), filter), MODEL_PATH);
        System.out.println("\n The class name for the instance with petallength = 1.6 and petalwidth =0.2 is  " +classname);

    }

}
