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
package eu.griend.pmph.client;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import org.junit.Test;

public class SillyTests {

	@Test
	public void test01() throws Exception {
//		String tmp = "2017-06-05 13:39:07 CEST";
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss zzz");
//		
//		LocalDateTime dt = LocalDateTime.parse(tmp, formatter);
//		
//		assertEquals(2017, dt.getYear());
	}

	// 2017-06-06 10:33:24,121 INFO  [pool-2-thread-1] eu.griend.pmph.service.PamphletScheduler - message start - 2017-06-06T10:33:23.925
	@Test
	public void test02() throws Exception {
		String tmp = "2017-06-06T10:33:23.925";
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		
		LocalDateTime dt = LocalDateTime.parse(tmp, formatter);
		
		assertEquals(2017, dt.getYear());
		assertEquals(Month.JUNE, dt.getMonth());
		assertEquals(6, dt.getDayOfMonth());
		assertEquals(10, dt.getHour());
		assertEquals(33, dt.getMinute());
		assertEquals(23, dt.getSecond());
		assertEquals(925, dt.get(ChronoField.MILLI_OF_SECOND));
		
	}
	
	// 2017-06-06 10:43:23,912 INFO  [pool-2-thread-1] eu.griend.pmph.service.PamphletScheduler - message alive - 2017-06-06T10:43:23.912
	@Test
	public void test03() throws Exception {
		String tmp = "2017-06-06T10:43:23.912";
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		
		LocalDateTime dt = LocalDateTime.parse(tmp, formatter);
		
		assertEquals(2017, dt.getYear());
		assertEquals(Month.JUNE, dt.getMonth());
		assertEquals(6, dt.getDayOfMonth());
		assertEquals(10, dt.getHour());
		assertEquals(43, dt.getMinute());
		assertEquals(23, dt.getSecond());
		assertEquals(912, dt.get(ChronoField.MILLI_OF_SECOND));
		
	}
	
}
