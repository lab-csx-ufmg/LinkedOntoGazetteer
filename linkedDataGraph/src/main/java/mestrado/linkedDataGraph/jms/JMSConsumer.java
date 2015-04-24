package mestrado.linkedDataGraph.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.JsonSyntaxException;

public class JMSConsumer<T> {

	private final MessageConsumer consumer;
	private final Session session;
	private final Connection connection;
	private final Class<T> clazz;
	
	public JMSConsumer(Class<T> clazz, String queueName) throws JMSException {
		// Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://191.238.50.188:61616");

        // Create a Connection
        this.connection = connectionFactory.createConnection();
        this.connection.start();

        // Create a Session
        this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = this.session.createQueue(queueName);

        // Create a MessageProducer from the Session to the Topic or Queue
        this.consumer = this.session.createConsumer(destination);
        
        this.clazz = clazz;
	}
	
	public T receive() throws JsonSyntaxException, JMSException {
		Message msg = this.consumer.receive();
		if(msg instanceof TextMessage) {
			return GsonBuilder.getGson().fromJson(((TextMessage) msg).getText(), this.clazz);
		} else {
			return null;
		}
	}
	
}
