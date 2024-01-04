package coop.MongoToFirebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
@SpringBootApplication
public class MongoToFirebaseApplication {

	public static void main(String[] args) throws IOException {

		ClassLoader classLoader=MongoToFirebaseApplication.class.getClassLoader();
		File file=new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());

		FileInputStream serviceAccount =
				new FileInputStream(file.getAbsolutePath());



		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://daily-report-152db-default-rtdb.europe-west1.firebasedatabase.app")
				.build();

		FirebaseApp.initializeApp(options);

		SpringApplication.run(MongoToFirebaseApplication.class, args);
	}

	@Bean
	public MongoTemplate mongoTemplate(){
		return new MongoTemplate(mongoDatabaseFactory());
	}
	@Bean
	public MongoDatabaseFactory mongoDatabaseFactory() {
		return new SimpleMongoClientDatabaseFactory(mongoClient(),"t24_to_mongo");
	}
	@Bean
	public MongoClient mongoClient() {
		return   MongoClients.create("mongodb://10.1.125.52:27017");
	}


}
