package edu.mongo.tdd.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that extends the functionality of MongoSpringExtension wich provides information about
 * the test MongoDB JSON file for this method as well as the collection name and the type of objects stored in the test file.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoTestDataFile {

    /**
     * Name of the MongoDB JSON test file with extension.
     */
    String value();

    /**
     * Class type of the objects stored in the MongoDB JSON test file.
     */
    Class classType();

    /**
     * Name of the MongoDB collection for the test objects.
     */
String collectionName();
}
