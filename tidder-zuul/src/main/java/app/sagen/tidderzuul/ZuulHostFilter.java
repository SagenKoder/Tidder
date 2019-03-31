package app.sagen.tidderzuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ZuulHostFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(ZuulHostFilter.class);

    static final String HEADER_HOST = "Host";

    public String filterType() {
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }

    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeaders(HEADER_HOST).hasMoreElements();
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String host = ctx.getRequest().getHeaders(HEADER_HOST).nextElement();
        ctx.getZuulRequestHeaders().put(HEADER_HOST, host);

        logger.error("Request from host: " + host);
        return null;
    }
}
