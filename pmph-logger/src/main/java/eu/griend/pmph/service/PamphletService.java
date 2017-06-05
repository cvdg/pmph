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

import java.util.UUID;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PamphletService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PamphletService.class);
	private final static String BROKER = "tcp://rpia0009725:1883";

	private MqttClient mqttClient = null;

	public MqttClient getMqttClient() {
		LOGGER.trace("getMqttClient() - start");

		try {
			if (this.mqttClient == null) {

				String tmpdir = System.getProperty("java.io.tmpdir");
				MqttClientPersistence persistence = new MqttDefaultFilePersistence(tmpdir);
				this.mqttClient = new MqttClient(BROKER, UUID.randomUUID().toString(), persistence);
				this.mqttClient.connect();

				LOGGER.info("connect - {}", BROKER);

			}
		} catch (MqttException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		LOGGER.trace("getMqttClient() - stop");

		return this.mqttClient;
	}

	/**
	 * Run at exit
	 */
	@PreDestroy
	public void stop() {
		LOGGER.trace("stop() - start");

		try {
			if (this.mqttClient != null) {
				this.mqttClient.disconnect();
				this.mqttClient.close();

				LOGGER.info("disconnect - {}", BROKER);
			}
		} catch (MqttException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		LOGGER.trace("stop() - stop");
	}
}
