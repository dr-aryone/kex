package utilities;

public class ParseContext<T> {
	public String message;
	public T parsedData;
	
	public ParseContext(String message, T parsedData) {
		this.message = message;
		this.parsedData = parsedData;
	}
}
