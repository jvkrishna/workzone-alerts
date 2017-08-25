package com.intrans.reactor.workzone.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * Constants class for storing workzone alters related values.
 * 
 * @author Vamsi Krishna J <br />
 *         <b>Date:</b> Mar 28, 2017
 *
 */
public class WorkzoneConstants {

	public enum WorkzoneAlertStatus {
		BLANK, SLOW, STOP;

		public static WorkzoneAlertStatus getWorkzoneAlertStatus(String alert) {
			for (WorkzoneAlertStatus alertEnum : values()) {
				if (StringUtils.equalsIgnoreCase(alert.trim(), alertEnum.toString())) {
					return alertEnum;
				}
			}
			// TODO Logging
			//System.out.println("No enum found with this alert, return default BLANK alert.");
			return WorkzoneAlertStatus.BLANK;
		}
	}

	public enum Direction {
		NORTH("n", "nb"), SOUTH("s", "sb"), EAST("e", "eb"), WEST("w", "wb");
		private String alias;
		private String boundAlias;

		private Direction(String alias, String boundAlias) {
			this.alias = alias;
			this.boundAlias = boundAlias;
		}

		public String getAlias() {
			return alias;
		}

		public String getBoundAlias() {
			return boundAlias;
		}

		public static Direction getDirectionByAlias(String alias) {
			for (Direction direction : values()) {
				if (direction.getAlias().equals(alias)) {
					return direction;
				}
			}
			throw new IllegalArgumentException("No Direction found with given alias.");
		}

		public static Direction getDirectionByBoundAlias(String boundAlias) {
			for (Direction direction : values()) {
				if (StringUtils.equalsIgnoreCase(direction.getBoundAlias(), boundAlias)) {
					return direction;
				}
			}
			throw new IllegalArgumentException("No Direction found with given bound alias.");
		}

	}

	public enum AlertSMSType {
		ALERT, CLEARANCE;
	}
}
