package my.challenge.mafia.repository.exception;

import org.springframework.dao.DataAccessException;

public class DuplicateInsertException extends DataAccessException {

    public DuplicateInsertException() {
        super("DuplicateInsertException Occur");
    }

    public DuplicateInsertException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
