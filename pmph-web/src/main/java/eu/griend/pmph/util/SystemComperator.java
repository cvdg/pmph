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
package eu.griend.pmph.util;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.griend.pmph.entity.SystemEntity;

public class SystemComperator implements Comparator<SystemEntity> {
	private final static Logger LOGGER = LoggerFactory.getLogger(SystemComperator.class);
	@Override
	public int compare(SystemEntity se0, SystemEntity se1) {
		LOGGER.trace("compare() - start");
		
		int seq0 = Integer.parseInt(se0.getSystemName().substring(4, 8));
		int seq1 = Integer.parseInt(se1.getSystemName().substring(4, 8));

		LOGGER.trace("compare() - stop");
		
		return seq0 - seq1;
	}

}
