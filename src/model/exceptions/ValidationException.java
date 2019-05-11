package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	Map<String, String> erros = new HashMap<String, String>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){
		return erros;
	}

	public void addError(String fieldName, String errorsMessage) {
		erros.put(fieldName, errorsMessage);
	}
}
