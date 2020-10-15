package app.ui.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Main interface for the Command pattern implementation.
 *
 * @author
 */
public abstract class Command implements Serializable {

    private static final long serialVersionUID = 3088828812057520817L;

    /**
     * Execution method for command.
     *
     * @return Address to go once the command is executed.
     */
    public abstract String executeGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;

    /**
     * Execution method for command.
     *
     * @return Address to go once the command is executed.
     */
    public abstract String executePost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }
}