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
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PamphletScheduler {
	private final static Logger LOGGER = LoggerFactory.getLogger(PamphletScheduler.class);

	private static final DateFormat FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss zz");

	private final static String START_TOPIC = "pmph/{0}/start";
	private final static String ALIVE_TOPIC = "pmph/{0}/alive";
	private final static String STOP_TOPIC = "pmph/{0}/stop";
	
	@Autowired
	private PamphletService pamphletService = null;

	private boolean started = false;
	private long counter = 0L;


	/**
	 * Send an alive message every 6 minutes.
	 * 
	 * @throws MqttSecurityException
	 * @throws MqttException
	 * @throws UnknownHostException 
	 */
	@Scheduled(fixedRate = 600000)
	public void alive() throws MqttException, UnknownHostException {
		LOGGER.trace("alive() - start");

		if (!started) {
			this.started = true;
			String topic = MessageFormat.format(START_TOPIC, InetAddress.getLocalHost().getHostName());
			String date = FORMAT.format(new Date());

			MqttClient mqttClient = this.pamphletService.getMqttClient();
			MqttMessage start = new MqttMessage();
			start.setQos(1);
			start.setRetained(true);
			start.setPayload(date.getBytes());
			mqttClient.publish(topic, start);

			topic = MessageFormat.format(STOP_TOPIC, InetAddress.getLocalHost().getHostName());
			
			MqttMessage stop = new MqttMessage();
			stop.setQos(1);
			stop.setRetained(true);
			stop.clearPayload();
			mqttClient.publish(topic, stop);
		} else {
			String topic = MessageFormat.format(ALIVE_TOPIC, InetAddress.getLocalHost().getHostName());
			
			MqttClient mqttClient = this.pamphletService.getMqttClient();
			MqttMessage alive = new MqttMessage();
			alive.setQos(0);
			alive.setRetained(false);
			alive.setPayload(Long.toString(++this.counter).getBytes());
			mqttClient.publish(topic, alive);
		}

		LOGGER.trace("alive() - stop");
	}

	
	/**
	 * Run at exit
	 * 
	 * @throws MqttException
	 * @throws UnknownHostException 
	 * @throws MqttPersistenceException
	 */
	@PreDestroy
	public void stop() throws MqttException, UnknownHostException {
		LOGGER.debug("stop() - start");

		String topic = MessageFormat.format(STOP_TOPIC, InetAddress.getLocalHost().getHostName());
		String date = FORMAT.format(new Date());
		
		MqttClient mqttClient = this.pamphletService.getMqttClient();
		MqttMessage stop = new MqttMessage();
		stop.setQos(1);
		stop.setRetained(true);
		stop.setPayload(date.getBytes());
		mqttClient.publish(topic, stop);

		LOGGER.debug("stop() - stop");
	}
}
