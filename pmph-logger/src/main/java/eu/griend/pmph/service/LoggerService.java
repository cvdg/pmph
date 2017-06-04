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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class LoggerService implements CommandLineRunner, IMqttMessageListener {
	private static final DateFormat FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
	private final static String BROKER = "tcp://rpia0009725:1883";

	private MqttClient mqttClient = null;
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		StringBuilder tmp = new StringBuilder();
		Date now = new Date();
		
		tmp.append(FORMAT.format(now));
		tmp.append(" ");
		tmp.append(topic);
		tmp.append(" ");
		tmp.append(message.toString());
		
		System.out.println(tmp.toString());
	}

	@Override
	public void run(String... args) throws MqttException {
		this.mqttClient = new MqttClient(BROKER, UUID.randomUUID().toString());
		this.mqttClient.connect();
		this.mqttClient.subscribe("#", this);
	}
	
	@PreDestroy
	public void stop() throws MqttException {
		this.mqttClient.unsubscribe("#");
		this.mqttClient.disconnect();
		this.mqttClient.close();
	}
}
