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
package eu.griend.pmph.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SystemEntity implements Serializable {
	private static final long serialVersionUID = -7135029958633398821L;

	private String systemName = null;

	private LocalDateTime startDateTime = null;

	private LocalDateTime stopDateTime = null;

	private LocalDateTime aliveDateTime = null;

	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public LocalDateTime getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getStopDateTime() {
		return this.stopDateTime;
	}

	public void setStopDateTime(LocalDateTime stopDateTime) {
		this.stopDateTime = stopDateTime;
	}

	public LocalDateTime getAliveDateTime() {
		return this.aliveDateTime;
	}

	public void setAliveDateTime(LocalDateTime aliveDateTime) {
		this.aliveDateTime = aliveDateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aliveDateTime == null) ? 0 : aliveDateTime.hashCode());
		result = prime * result + ((startDateTime == null) ? 0 : startDateTime.hashCode());
		result = prime * result + ((stopDateTime == null) ? 0 : stopDateTime.hashCode());
		result = prime * result + ((systemName == null) ? 0 : systemName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemEntity other = (SystemEntity) obj;
		if (aliveDateTime == null) {
			if (other.aliveDateTime != null)
				return false;
		} else if (!aliveDateTime.equals(other.aliveDateTime))
			return false;
		if (startDateTime == null) {
			if (other.startDateTime != null)
				return false;
		} else if (!startDateTime.equals(other.startDateTime))
			return false;
		if (stopDateTime == null) {
			if (other.stopDateTime != null)
				return false;
		} else if (!stopDateTime.equals(other.stopDateTime))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SystemEntity [systemName=" + systemName + ", startDateTime=" + startDateTime + ", stopDateTime="
				+ stopDateTime + ", aliveDateTime=" + aliveDateTime + "]";
	}
}
