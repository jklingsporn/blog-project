package io.github.jklingsporn.blog.vertxpebble;

import io.github.jklingsporn.templ.PebbleTemplateEngine;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;

/**
 * Created by jklingsporn on 29.12.15.
 */
public class WebserverVerticle extends AbstractVerticle{

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WebserverVerticle.class);

    private final Router router;

    public WebserverVerticle(Router router) {
        this.router = router;
    }

    @Override
    public void start() throws Exception {
        super.start();
        router.route("/resources/*").handler(StaticHandler.create("webroot/resources"));
        router.route().handler(FaviconHandler.create("webroot/favicon.ico"));
        PebbleTemplateEngine templateEngine = new PebbleTemplateEngine();
        TemplateHandler templateHandler = TemplateHandler.create(templateEngine, "webroot/templates","text/html");
        router.route("/templates/*").handler(templateHandler);
        router.route().handler(h->h.reroute("/templates/chat_client.html"));
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
