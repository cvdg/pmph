/*
 * Copyright (C) 2017 C.A. van de Griend <c.vande.griend@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.griend.pmph.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PamphletService implements CommandLineRunner {
	private final static Logger LOGGER = LoggerFactory.getLogger(PamphletService.class);
	private final static String BROKER = "tcp://rpia0009725:1883";
	private final static String START = "start";
	private final static String ALIVE = "alive";
	private final static String STOP = "stop";
	
	private String topic = null;

	private MqttClient client = null;

	public PamphletService() throws MqttException, UnknownHostException {
		LOGGER.debug("StartService() - start");

		this.topic = "pmph/" + InetAddress.getLocalHost().getHostName() + "/state";
		
		LOGGER.debug("StartService() - stop");
	}

	/**
	 * Run at start
	 * 
	 * @throws MqttException
	 * @throws MqttSecurityException
	 */
	@Override
	public void run(String... args) throws MqttSecurityException, MqttException {
		LOGGER.debug("run() - start");

		client = new MqttClient(BROKER, UUID.randomUUID().toString());
		client.connect();

		MqttMessage message = new MqttMessage(START.getBytes());
		client.publish(this.topic, message);

		LOGGER.debug("run() - stop");
	}

	@Scheduled(fixedRate = 60000)
	public void alive() throws MqttSecurityException, MqttException {
		LOGGER.debug("alive() - start");

		if (client != null) {
			MqttMessage message = new MqttMessage(ALIVE.getBytes());
			client.publish(this.topic, message);
		}

		LOGGER.debug("alive() - stop");
	}

	/**
	 * Run at exit
	 * 
	 * @throws MqttException
	 * @throws MqttPersistenceException
	 */
	@PreDestroy
	public void stop() throws MqttPersistenceException, MqttException {
		LOGGER.debug("stop() - start");

		MqttMessage message = new MqttMessage(STOP.getBytes());
		client.publish(this.topic, message);
		client.disconnect();

		LOGGER.debug("stop() - stop");
	}

}
