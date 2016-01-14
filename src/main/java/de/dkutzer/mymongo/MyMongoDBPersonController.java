package de.dkutzer.mymongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class MyMongoDBPersonController {

	private static MongoClient mongoClient;
	private static MongoCollection<Document> collection;

	public static void init() {
		final MongoClientOptions mongoClientOptions = MongoClientOptions
				.builder().connectTimeout(60000).build();
		mongoClient = new MongoClient(new ServerAddress("localhost", 27017),
				mongoClientOptions);
		collection = mongoClient.getDatabase("tutorial").getCollection(
				"persons");
	}

	public static void close() {
		mongoClient.close();
	}

	public static void insertPerson(PersonDTO person) {
		final Document personDoc = new Document();
		personDoc.put("name", person.name);
		personDoc.put("lastname", person.lastName);
		personDoc.put("age", person.age);
		collection.insertOne(personDoc);
	}

	public static List<PersonDTO> findAllPersons() {
		final List<PersonDTO> result = new ArrayList<PersonDTO>();
		final FindIterable<Document> find = collection.find();
		find.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(new PersonDTO(document.getString("name"), document
						.getString("lastname"), document.getInteger("age")));
			}
		});
		return result;
	}

	public static List<PersonDTO> findPersonByName(String name) {
		final List<PersonDTO> result = new ArrayList<PersonDTO>();
		final FindIterable<Document> find = collection.find(new Document(
				"name", name));
		find.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(new PersonDTO(document.getString("name"), document
						.getString("lastname"), document.getInteger("age")));
			}
		});
		return result;
	}

}
