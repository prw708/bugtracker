package com.penguinwebstudio.bugtracker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ContextConfiguration;

import com.mongodb.client.MongoClients;
import com.penguinwebstudio.tickets.Comment;
import com.penguinwebstudio.tickets.Ticket;
import com.penguinwebstudio.tickets.TicketController;
import com.penguinwebstudio.tickets.TicketDatabaseConfiguration;
import com.penguinwebstudio.user.User;
import com.penguinwebstudio.user.UserController;
import com.penguinwebstudio.user.UserDatabaseConfiguration;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@ContextConfiguration(classes = {TicketDatabaseConfiguration.class, UserDatabaseConfiguration.class})
@ComponentScan(basePackageClasses=UserController.class)
@ComponentScan(basePackageClasses=TicketController.class)
@SpringBootTest(classes = BugTrackerApplication.class)
@WithMockUser(username = "testuser", password = "Test1234", roles = "USER")
public class DatabaseTests {
	
    private static final String CONNECTION_STRING = "mongodb://%s:%d";
	private MongoTemplate mongoTemplate;
    private MongodExecutable mongodExecutable;
    
    @AfterEach
    void clean() {
        mongodExecutable.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        String ip = "localhost";
        int port = 27017;

        ImmutableMongodConfig mongodConfig = MongodConfig
            .builder()
            .version(Version.Main.PRODUCTION)
            .net(new Net(ip, port, Network.localhostIsIPv6()))
            .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "test");
        mongoTemplate.save(new User(
        		"testuser",
        		"Test1234",
        		"a@a.com",
        		new Date(),
        		false,
        		true,
        		false,
        		""
        ));
		mongoTemplate.save(new Ticket(
				"Ticket 1",
				"Reporter",
				"testuser",
				new Date(),
				"Medium",
				"Open",
				"Description"
		));
		mongoTemplate.save(new Ticket(
				"Ticket 2",
				"Reporter",
				"testuser",
				new Date(),
				"Medium",
				"Closed",
				"Description"
		));
    }
			
	@Test
	public void testOpenView() throws Exception {	
		Query query = new Query((CriteriaDefinition) Criteria.where("status").is("Open"));
		assertFalse(mongoTemplate.find(query, Ticket.class).isEmpty());
	}
	
	@Test
	public void testClosedView() throws Exception {
		Query query = new Query((CriteriaDefinition) Criteria.where("status").is("Closed"));
		assertFalse(mongoTemplate.find(query, Ticket.class).isEmpty());	
	}
	
	@Test
	public void testCreatePost() throws Exception {
		mongoTemplate.save(new Ticket(
				"Ticket 3",
				"Reporter",
				"testuser",
				new Date(),
				"Medium",
				"Open",
				"Description"
		));
		Query query = new Query((CriteriaDefinition) Criteria.where("title").is("Ticket 3"));
		assertFalse(mongoTemplate.find(query, Ticket.class).isEmpty());
	}

	@Test
	public void testDeletePost() throws Exception {
		Query query = new Query((CriteriaDefinition) Criteria.where("title").is("Ticket 1"));
		mongoTemplate.remove(query, Ticket.class);
		assertTrue(mongoTemplate.find(query, Ticket.class).isEmpty());
	}
	
	@Test
	public void testViewPost() throws Exception {
		Query query = new Query((CriteriaDefinition) Criteria.where("title").is("Ticket 1"));
		mongoTemplate.updateFirst(query, new Update().set("description", "Updated Description"), Ticket.class);
		assertEquals(mongoTemplate.find(query, Ticket.class).get(0).getDescription(), "Updated Description");
	}
	
	@Test
	public void testDiscussPost() throws Exception {
		Query query = new Query((CriteriaDefinition) Criteria.where("title").is("Ticket 1"));
		Ticket ticket = mongoTemplate.find(query, Ticket.class).get(0);
		Comment comment = new Comment(0, "New Comment", "testuser", new Date());
		ArrayList<Comment> thread = new ArrayList<Comment>();
		thread.add(comment);
		ArrayList<List<Comment>> comments = new ArrayList<List<Comment>>();
		comments.add(thread);
		Update update = new Update().push("comments").each(comments);
		mongoTemplate.updateFirst(query, update, Ticket.class);
		assertEquals(mongoTemplate.find(query, Ticket.class).get(0).countComments(), 1);
	}
	
	@Test
	public void testDiscussDeletePost() throws Exception {
		Query query = new Query((CriteriaDefinition) Criteria.where("title").is("Ticket 1"));
		Ticket ticket = mongoTemplate.find(query, Ticket.class).get(0);
		Comment comment = new Comment(0, "New Comment", "testuser", new Date());
		ArrayList<Comment> thread = new ArrayList<Comment>();
		thread.add(comment);
		ArrayList<List<Comment>> comments = new ArrayList<List<Comment>>();
		comments.add(thread);
		Update update = new Update().push("comments").each(comments);
		mongoTemplate.updateFirst(query, update, Ticket.class);
		update = new Update().set("comments", null);
		mongoTemplate.findAndModify(query, update, Ticket.class);
		assertEquals(mongoTemplate.find(query, Ticket.class).get(0).countComments(), 0);
	}
	
}
