package app.ui.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for custom tag library
 *
 * @author Alex Odulo
 */
public class TimeTag extends TagSupport {

    private static final long serialVersionUID = -5246409001497897805L;
    private String type;

    /**
     * Get current Date to tag
     *
     * @return return value for doStartTag
     * @throws JspException JspException
     */
    @Override
    public int doStartTag() throws JspException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(type);
        JspWriter out = pageContext.getOut();

        LocalDateTime currentTime = LocalDateTime.now(Clock.systemDefaultZone());

        try {
            out.print(currentTime.format(formatter));
        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    /**
     * Get type from tag
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Set type to tag
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }

}
