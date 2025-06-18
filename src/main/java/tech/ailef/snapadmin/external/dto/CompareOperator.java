/* 
 * SnapAdmin - An automatically generated CRUD admin UI for Spring Boot apps
 * Copyright (C) 2023 Ailef (http://ailef.tech)
 * 

 */


package tech.ailef.snapadmin.external.dto;

/**
 * A list of operators that are used in faceted search. 
 *
 */
public enum CompareOperator {
	GT {
		@Override
		public String getDisplayName() {
			return "Μεγαλύτερο από";
		}
	},
	LT {
		@Override
		public String getDisplayName() {
			return "Μικρότερο από";
		}
	},
	EQ {
		@Override
		public String getDisplayName() {
			return "Ίσον";
		}
	},
	STRING_EQ {
		@Override
		public String getDisplayName() {
			return "Ίσον";
		}
	},
	BEFORE {
		@Override
		public String getDisplayName() {
			return "Πριν";
		}
	},
	AFTER {
		@Override
		public String getDisplayName() {
			return "Μετά";
		}
	},
	CONTAINS {
		@Override
		public String getDisplayName() {
			return "Περιέχει";
		}
	};
	
	public abstract String getDisplayName();
	
	public String toString() {
		return this.name().toLowerCase();
	}
}
