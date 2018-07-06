package com.emaraic.ml;

import weka.core.*;
import weka.core.converters.*;

/**
 * Based strongly on an example by FracPete:
 * https://weka.wikispaces.com/Transferring+an+ARFF+file+into+a+database
 *
 * @author MikolajBuchwald (mikolaj[dot]buchwald[at]gmail[dot]com)
 */

public class DatabaseManager {

    /**
     * loads a dataset into a MySQL database
     *
     * @param data object containing data
     */
    public static void saveToDatabase(Instances data, String tableName) throws Exception {
        data.setClassIndex(data.numAttributes() - 1);

        DatabaseSaver save = new DatabaseSaver();
        // Note: CEST timezone throws an JDBC error, hence I hard-fixed it to UTC.
        save.setUrl("jdbc:mysql://localhost:3306/weka_test?serverTimezone=UTC");
        save.setUser("phpmyadmin");
        save.setPassword("some_pass");
        save.setInstances(data);
        save.setRelationForTableName(false);
        save.setTableName(tableName);
        save.connectToDatabase();
        // #TODO: "[DatabaseSaver] Table 'iris' already exists - will append data..."
        save.writeBatch();
    }
}
