package app.ui.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Encoding filter.
 *
 * @author Alex Odulo
 */
public class EncodingFilter implements Filter {
    private static final Logger log = Logger.getLogger(EncodingFilter.class);

    private String encoding;

    /**
     * Access Filter destruction
     */
    @Override
    public void destroy() {
        // do nothing
    }

    /**
     * Encoding Filter execution
     *
     * @param request  ServletRequest
     * @param response ServletResponse
     * @param chain    FilterChain
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.trace("Request uri --> " + httpRequest.getRequestURI());

        String requestEncoding = request.getCharacterEncoding();
        if (requestEncoding == null) {
            log.trace("Request encoding = null, set encoding --> " + encoding);
            request.setCharacterEncoding(encoding);
        }

        chain.doFilter(request, response);
    }

    /**
     * Filter initialization
     *
     * @param fConfig FilterConfig
     * @throws ServletException ServletException
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        encoding = fConfig.getInitParameter("encoding");
        log.trace("Encoding from web.xml --> " + encoding);
    }

}