/*
 * pmph - Pamphlet
 *   
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
package eu.griend.pmph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 
 * @author cvdg
 *
 */
@SpringBootApplication
public class PamphletLogger {
	private final static Logger LOGGER = LoggerFactory.getLogger(PamphletLogger.class);
	
	public static void main(String[] args) {
		try {
			SpringApplication app = new SpringApplication(PamphletLogger.class);
			app.setBannerMode(Banner.Mode.OFF);
			app.run(args);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}
}
