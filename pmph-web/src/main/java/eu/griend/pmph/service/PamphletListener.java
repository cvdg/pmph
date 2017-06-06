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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.griend.pmph.entity.SystemEntity;
import eu.griend.pmph.util.SystemComperator;

@Component
public class PamphletListener implements ServletContextListener, IMqttMessageListener {
	private final static Logger LOGGER = LoggerFactory.getLogger(PamphletListener.class);

	private final static String BROKER = "tcp://rpia0009725:1883";

	private MqttClient mqttClient = null;

	private List<SystemEntity> systemEntities = new LinkedList<SystemEntity>();

	public List<SystemEntity> getSystemEntities() {
		List<SystemEntity> list = new ArrayList<SystemEntity>();
		list.addAll(this.systemEntities);
		Collections.sort(list, new SystemComperator());

		return list;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.trace("contextInitialized() - start");

		try {
			String tmpdir = System.getProperty("java.io.tmpdir");
			MqttClientPersistence persistence = new MqttDefaultFilePersistence(tmpdir);
			this.mqttClient = new MqttClient(BROKER, UUID.randomUUID().toString(), persistence);
			this.mqttClient.connect();

			LOGGER.info("connect - {}", BROKER);

			this.mqttClient.subscribe("pmph/#", this);

			LOGGER.info("subscribed: pmph/#");
		} catch (MqttException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		LOGGER.trace("contextInitialized() - stop");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.trace("contextDestroyed() - start");

		try {
			this.mqttClient.unsubscribe("pmph/#");

			LOGGER.info("unsubscribed: pmph/#");

			this.mqttClient.disconnect();

			LOGGER.info("disconnect - {}", BROKER);
			this.mqttClient.close();

			LOGGER.info("close - {}", BROKER);
		} catch (MqttException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		LOGGER.trace("contextDestroyed() - stop");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		LOGGER.trace("messageArrived() - start");

		SystemEntity newSystemEntity = new SystemEntity();
		String systemName = topic.split("/")[1];
		String action = topic.split("/")[2];
		String payload = message.toString();

		LocalDateTime localDateTime = null;

		LOGGER.info("**** topic: {}, payload: {}, system: {}, action: {}", topic, payload, systemName, action);

		try {
			if (payload != null) {
				localDateTime = LocalDateTime.parse(payload, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}
		} catch (DateTimeParseException e) {
			LOGGER.info("wrong format: {}", payload);
			localDateTime = LocalDateTime.now();
		}

		boolean found = false;

		for (int n = 0; n < this.systemEntities.size(); n++) {
			SystemEntity oldSystemEntity = this.systemEntities.get(n);

			if (oldSystemEntity.getSystemName().equals(systemName)) {
				newSystemEntity.setSystemName(oldSystemEntity.getSystemName());
				newSystemEntity.setStartDateTime(oldSystemEntity.getStartDateTime());
				newSystemEntity.setStopDateTime(oldSystemEntity.getStopDateTime());
				newSystemEntity.setAliveDateTime(oldSystemEntity.getAliveDateTime());

				LOGGER.info("found: {}", oldSystemEntity.toString());

				break;
			}
		}

		if (action.equals("start")) {
			newSystemEntity.setStartDateTime(localDateTime);
			newSystemEntity.setAliveDateTime(null);
			newSystemEntity.setStopDateTime(null);
		} else if (action.equals("alive")) {
			// newSystemEntity.setStartDateTime(localDateTime);
			newSystemEntity.setAliveDateTime(localDateTime);
			newSystemEntity.setStopDateTime(null);
		} else if (action.equals("stop")) {
			// newSystemEntity.setStartDateTime(localDateTime);
			newSystemEntity.setAliveDateTime(null);
			newSystemEntity.setStopDateTime(localDateTime);
		} else {
			LOGGER.warn("Unknown action: {} - {}", action, payload);
		}

		for (int n = 0; n < this.systemEntities.size(); n++) {
			SystemEntity oldSystemEntity = this.systemEntities.get(n);

			if (oldSystemEntity.getSystemName().equals(systemName)) {
				found = true;

				this.systemEntities.set(n, newSystemEntity);

				LOGGER.info("update: {}", newSystemEntity.toString());

				break;
			}
		}

		if (!found) {
			newSystemEntity.setSystemName(systemName);
			newSystemEntity.setStartDateTime(localDateTime);
			newSystemEntity.setAliveDateTime(null);
			newSystemEntity.setStopDateTime(null);

			this.systemEntities.add(newSystemEntity);

			LOGGER.info("add: {}", newSystemEntity.toString());
		}

		LOGGER.trace("messageArrived() - stop");
	}
}
