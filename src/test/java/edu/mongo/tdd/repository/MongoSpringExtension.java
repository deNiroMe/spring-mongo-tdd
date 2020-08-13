package edu.mongo.tdd.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class MongoSpringExtension implements BeforeEachCallback,AfterEachCallback {

    //path to where our json test files are stored
    private static Path JSON_FILE_PATH = Paths.get("src","test","resources","data");

    // used to moad json data as list of objects
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * this callback method will be called after each test execution,
     * It is responsible of dropping the test's MongoDB collection,
     * so that the next test that run is clean.
     * @param context the ExtensionContext, which provides access to the test method
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        context.getTestMethod().ifPresent(method -> {

            //load test file using annotation argument
            MongoTestDataFile mongoTestDataFile = method.getAnnotation(MongoTestDataFile.class);

            // Get MongoTemplate from context to drop the test collection
            getMongoTemplate(context).ifPresent(mongoTemplate -> mongoTemplate.dropCollection(mongoTestDataFile.collectionName()));
        });

    }

    /**
     * this callback method will be called before each test execution,
     * It is responsible of importing the JSON document, defined by the MongoDataFile annotation,
     * into the embedded MongoDB, through the provided MongoTemplate.
     * @param context the ExtensionContext, which provides access to the test method
     */
    @Override
    public void beforeEach(ExtensionContext context) {

        context.getTestMethod().ifPresent(method -> {
            //load test file using annotation argument
            MongoTestDataFile mongoTestDataFile = method.getAnnotation(MongoTestDataFile.class);

            // Load the MongoTemplate to import test data
            getMongoTemplate(context).ifPresent(mongoTemplate -> {
                try {
                    //Load list of objects using jackson object mapper
                    List objects = objectMapper.readValue(JSON_FILE_PATH.resolve(mongoTestDataFile.value()).toFile(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class,mongoTestDataFile.classType()));

                    // save each object into mongoDB
                    objects.forEach(mongoTemplate::save);
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        });

    }

    /**
     * helper method that uses reflexion to invoke the getmongoTemplate method on the test instance.
     * @param context the ExtensionContext, which provides access to the test method
     * @return An optional MongoTemplate if it exists
     */
    private Optional<MongoTemplate> getMongoTemplate(ExtensionContext context) {

        Optional<Class<?>> testClass = context.getTestClass();

        if (testClass.isPresent()) {
            Class<?> c = testClass.get();
            try {
                //Find the getMongoTemplate method on the test class
                Method method = c.getMethod("getMongoTemplate", null);

                // Invoke the getMongoTemplate on the test class
                Optional<Object> testIntance = context.getTestInstance();
                if (testIntance.isPresent()) {
                    return Optional.of((MongoTemplate) method.invoke(testIntance.get(), null));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

}
