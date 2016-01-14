package de.dkutzer.mymongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import mockit.Deencapsulation;

import org.bson.Document;
import org.junit.*;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;


public class MyMongoDBPersonControllerTest {

	/** The mongo client. */
	static MongoClient mongoClient;

	/** The mongod. */
	static MongodProcess mongod;

	/** The mongod executable. */
	static MongodExecutable mongodExecutable;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		try {
			final IMongodConfig mongodConfig = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION).build();

			MongodStarter runtime = MongodStarter.getDefaultInstance();

			mongodExecutable = runtime.prepare(mongodConfig);
			mongod = mongodExecutable.start();

			mongoClient = new MongoClient(new ServerAddress(mongodConfig.net()
					.getServerAddress(), mongodConfig.net().getPort()));

	        Deencapsulation.setField(MyMongoDBPersonController.class, "mongoClient", mongoClient);

		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		if (mongoClient != null) {
			mongoClient.close();
		}

		if (mongod != null) {
			mongod.stop();
		}
		if (mongodExecutable != null) {
			mongodExecutable.stop();
		}
	}

	@After
	public void tearDown() throws Exception {
		mongoClient.dropDatabase("tutorial");

	}

	@Before
	public void before() throws Exception {
		final MongoCollection<Document> collection = mongoClient.getDatabase(
				"tutorial").getCollection("persons");
		Deencapsulation.setField(MyMongoDBPersonController.class, "collection",
				collection);

	}

	@Test
	public void test() throws Exception {

		List<PersonDTO> allPersons = MyMongoDBPersonController.findAllPersons();
		assertThat(allPersons).isEmpty();

		MyMongoDBPersonController.insertPerson(new PersonDTO("Achim", "Menzel", 1));
		allPersons = MyMongoDBPersonController.findAllPersons();
		assertThat(allPersons).hasSize(1);

		MyMongoDBPersonController.insertPerson(new PersonDTO("Dietmar",
				"Wischmeyer", 2));
		allPersons = MyMongoDBPersonController.findAllPersons();
		assertThat(allPersons).hasSize(2);

		List<PersonDTO> findPersonByName = MyMongoDBPersonController
				.findPersonByName("Achim");
		assertThat(findPersonByName).hasSize(1);
		final PersonDTO personDTO = findPersonByName.get(0);
		assertThat(personDTO.lastName).isEqualTo("Menzel");
		assertThat(personDTO.name).isEqualTo("Achim");
		assertThat(personDTO.age).isEqualTo(1);

	}

}
