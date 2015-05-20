package com.cgi.chatroom.dao;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.InputStream;

/**
 * Very basic base DBUnit superclass to be extended for all DAO integration test.
 * <p/>
 * Dataset files need to be placed placed under the directory /dbunit-tests/test-data
 * <p/>
 * Dataset name corresponds to the class name with a .xml extension (instead of .java)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/dbunit-tests/test-context.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractDbUnitTest {

    private static final String DBUNIT_DATASET_LOCATION = "dbunit-tests/test-data/";

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        IDatabaseConnection dbUnitConnection = new DatabaseConnection(DataSourceUtils.getConnection(dataSource));
        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();

        InputStream datasetInputStream = getClass().getClassLoader()
                .getResourceAsStream(DBUNIT_DATASET_LOCATION + getDataSetName());
        // Retrieve data set and refresh the database with the data set.
        if (datasetInputStream != null) {
            IDataSet dataSet = dataSetBuilder.build(datasetInputStream);
            try {
                DatabaseOperation.REFRESH.execute(dbUnitConnection, dataSet);
            } finally {
                DataSourceUtils.releaseConnection(DataSourceUtils.getConnection(dataSource), dataSource);
            }
        }
    }

    protected String getDataSetName() {
        return getClass().getSimpleName() + ".xml";
    }

}
