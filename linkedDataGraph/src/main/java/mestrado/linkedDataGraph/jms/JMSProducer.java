package mestrado.linkedDataGraph.jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSProducer<T> {

	private final MessageProducer producer;
	private final Session session;
	private final Connection connection;
	
	public JMSProducer(String queueName) throws JMSException {
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
        this.producer = this.session.createProducer(destination);
        this.producer.setDeliveryMode(DeliveryMode.PERSISTENT);
	}

	public void close() throws Exception {
		this.session.close();
		this.connection.close();
	}
	
	public void send(T msg) throws JMSException {
		TextMessage message = this.session.createTextMessage(msg.toString());
		this.producer.send(message);
	}
	
}
