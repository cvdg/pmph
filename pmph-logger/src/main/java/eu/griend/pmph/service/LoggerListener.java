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

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class LoggerListener implements CommandLineRunner, IMqttMessageListener {
	private final static Logger LOGGER = LoggerFactory.getLogger(LoggerListener.class);

	@Autowired
	private PamphletService pamphletService = null;

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		LOGGER.trace("messageArrived() - start");

		LOGGER.info("{} - {}", topic, message.toString());
		
		LOGGER.trace("messageArrived() - stop");
	}

	@Override
	public void run(String... args) {
		LOGGER.trace("run() - start");

		try {
			MqttClient mqttClient = this.pamphletService.getMqttClient();
			mqttClient.subscribe("#", this);
		} catch (MqttException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		LOGGER.trace("run() - stop");
	}

	@PreDestroy
	public void stop() {
		LOGGER.trace("stop() - start");

		try {
			MqttClient mqttClient = this.pamphletService.getMqttClient();
			mqttClient.unsubscribe("#");
		} catch (MqttException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		LOGGER.trace("stop() - stop");
	}
}
