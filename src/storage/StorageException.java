package storage;

public class StorageException extends Exception {
	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
		super(message);
	}
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
	@Override
	public String getMessage() {
		String cause;
		if (getCause() != null)
			cause = "; caused by " + getCause().toString();
		else
			cause = "";
		return super.getMessage() + cause;
	}
}
