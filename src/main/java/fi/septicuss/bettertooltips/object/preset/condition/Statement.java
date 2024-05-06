package fi.septicuss.bettertooltips.object.preset.condition;

import fi.septicuss.bettertooltips.object.preset.condition.composite.CompositeCondition;

public class Statement {

	private Outcome outcome;
	private CompositeCondition condition;

	public Statement(CompositeCondition condition, Outcome outcome) {
		this.condition = condition;
		this.outcome = outcome;
	}

	public Statement(CompositeCondition condition) {
		this(condition, null);
	}

	public Outcome getOutcome() {
		return outcome;
	}

	public boolean hasOutcome() {
		return (outcome != null);
	}

	public CompositeCondition getCondition() {
		return condition;
	}

	public enum Outcome {

		REQUIRED(true), CANCEL(false);

		private boolean bool;

		private Outcome(boolean bool) {
			this.bool = bool;
		}

		public boolean asBoolean() {
			return bool;
		}

		public static Outcome parseOutcome(String value) {
			for (var outcome : values()) {
				if (String.valueOf(outcome.asBoolean()).equalsIgnoreCase(value) || outcome.toString().equalsIgnoreCase(value))
					return outcome;
			}
			
			return null;
		}

		public static boolean isOutcome(String value) {
			if (value == null)
				return false;
			if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
				return true;

			for (var outcome : values())
				if (outcome.toString().equalsIgnoreCase(value))
					return true;

			return false;
		}

	}

}
