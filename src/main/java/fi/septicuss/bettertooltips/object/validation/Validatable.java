package fi.septicuss.bettertooltips.object.validation;

public interface Validatable {

	public boolean isValid();
	
	default Validity validity() {
		return Validity.TRUE;
	}

}
