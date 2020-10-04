package com.bv.kafkaui.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value("${KAFKA_BOOTSTRAP_SERVERS}")
	private String bootstrapServers;

	@Value("${consumer.group-id}")
	private String groupId;

	@Value("${consumer.no-of-threads}")
	private int concurrency;

	@Value("${consumer.auto-offset-reset}")
	private String autoOffsetResetConfig;

	@Value("${consumer.session-timeout-ms}")
	private String sessionTimeOutMs;

	@Value("${consumer.max-poll-records}")
	private String maxPollRecords;

	@Value("${KAFKA_SECURITY_PROTOCOL:#{null}}")
	private String securityProtocol;

	@Value("${KAFKA_SASL_MECHANISM:#{null}}")
	private String saslMechanism;

	@Value("${KAFKA_LOGIN_USER_NAME:#{null}}")
	private String loginUserName;

	@Value("${KAFKA_LOGIN_PASSWORD:#{null}}")
	private String loginPassword;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeOutMs);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);

		if (securityProtocol != null)
			props.put("security.protocol", securityProtocol);

		if (saslMechanism != null)
			props.put("sasl.mechanism", saslMechanism);

		if (loginUserName != null && loginPassword != null)
			props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
					+ loginUserName + "\" password=\"" + loginPassword + "\";");

		return props;
	}

	@Bean
	public Map<String, Object> adminConfigs() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		if (securityProtocol != null)
			props.put("security.protocol", securityProtocol);

		if (saslMechanism != null)
			props.put("sasl.mechanism", saslMechanism);
		
		

		if (loginUserName != null && loginPassword != null) {
			
			String jassModule =  "org.apache.kafka.common.security.plain.PlainLoginModule";
			if(saslMechanism!= null && saslMechanism.equals("SCRAM-SHA-256")) {
				jassModule = "org.apache.kafka.common.security.scram.ScramLoginModule";
			}
			
			props.put("sasl.jaas.config", jassModule + " required username=\""
					+ loginUserName + "\" password=\"" + loginPassword + "\";");			
		}

		return props;
	}

	@Bean
	public KafkaAdmin admin() {

		return new KafkaAdmin(adminConfigs());
	}

	@Autowired
	@Bean
	public AdminClient client(KafkaAdmin kafkaAdmin) {
		return AdminClient.create(kafkaAdmin.getConfig());
	}
}