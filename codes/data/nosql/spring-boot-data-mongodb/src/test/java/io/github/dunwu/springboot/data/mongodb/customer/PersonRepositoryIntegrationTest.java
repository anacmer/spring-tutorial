package io.github.dunwu.springboot.data.mongodb.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.querydsl.QSort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Integration test for {@link CustomerRepository}.
 *
 * @author Oliver Gierke
 */
@SpringBootTest
public class PersonRepositoryIntegrationTest {

    @Autowired
    @Qualifier("customerRepository")
    CustomerRepository repository;
    @Autowired
    MongoOperations operations;

    Customer dave, oliver, carter;

    @BeforeEach
    public void setUp() {

        repository.deleteAll();

        dave = repository.save(new Customer("Dave", "Matthews"));
        oliver = repository.save(new Customer("Oliver August", "Matthews"));
        carter = repository.save(new Customer("Carter", "Beauford"));
    }

    /**
     * Test case to show that automatically generated ids are assigned to the domain objects.
     */
    @Test
    public void setsIdOnSave() {

        Customer dave = repository.save(new Customer("Dave", "Matthews"));
        assertThat(dave.getId(), is(notNullValue()));
    }

    /**
     * Test case to show the usage of the Querydsl-specific {@link QSort} to define the sort order in a type-safe way.
     */
    // @Test
    // public void findCustomersUsingQuerydslSort() {
    //
    // 	QCustomer customer = QCustomer.customer;
    // 	List<Customer> result = repository.findByLastname("Matthews", new QSort(customer.firstname.asc()));
    //
    // 	assertThat(result, hasSize(2));
    // 	assertThat(result.get(0), is(dave));
    // 	assertThat(result.get(1), is(oliver));
    // }

    /**
     * Test case to show the usage of the geo-spatial APIs to lookup people within a given distance of a reference
     * point.
     */
    @Test
    public void exposesGeoSpatialFunctionality() {

        GeospatialIndex indexDefinition = new GeospatialIndex("address.location");
        indexDefinition.getIndexOptions().put("min", -180);
        indexDefinition.getIndexOptions().put("max", 180);

        operations.indexOps(Customer.class).ensureIndex(indexDefinition);

        Customer ollie = new Customer("Oliver", "Gierke");
        ollie.setAddress(new Address(new Point(52.52548, 13.41477)));
        ollie = repository.save(ollie);

        Point referenceLocation = new Point(52.51790, 13.41239);
        Distance oneKilometer = new Distance(1, Metrics.KILOMETERS);

        GeoResults<Customer> result = repository.findByAddressLocationNear(referenceLocation, oneKilometer);

        assertThat(result.getContent(), hasSize(1));

        Distance distanceToFirstStore = result.getContent().get(0).getDistance();
        assertThat(distanceToFirstStore.getMetric(), is(Metrics.KILOMETERS));
        assertThat(distanceToFirstStore.getValue(), closeTo(0.862, 0.001));
    }

}
