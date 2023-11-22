package io.hawt.commits;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.github.GitHubType;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.hawt.Constants;
import io.hawt.processor.FileProcessor;

@Component
public class CommitRouter extends RouteBuilder implements Constants {

    @Value("${github.token}")
    private String githubAuthToken;

    @Value("${github.repo.owner}")
    private String githubRepoOwner;

    @Value("${github.repo.name}")
    private String githubRepoName;

    @Value("${github.repo.branch}")
    private String githubRepoBranch;

    @Value( "${github.request.delay}" )
    private String delay;

    @Override
    public void configure() {
        restConfiguration()
            .component("servlet")
            .enableCORS(true)
            .bindingMode(RestBindingMode.json);

        StringBuilder params = new StringBuilder();
        params
            .append("repoName=" + githubRepoName).append("&")
            .append("repoOwner=" + githubRepoOwner).append("&")
            .append("oauthToken=" + githubAuthToken).append("&")
            .append("delay=500");

        from("github:" + GitHubType.COMMIT + "/" + githubRepoBranch + "?" + params)
            .group("io.hawt.commits")
            .routeId("commitToFiles")
            .process(new FileProcessor(JSON, delay))
            .delay(250) // artificial slow down
            .log("Commit: ${body}")
            .to("file://" + ENHANCED_DEST_DIR);

        /**
         *  Adds rest route for commit files
         */
        List<String> fileList = new ArrayList<String>();
        from("direct:getCommits")
            .group("io.hawt.rest")
            .routeId("commitFilesToRest")
            .log("Commit From File: ${body}")
            .loopDoWhile(body().isNotNull())
            .pollEnrich("file://" + ENHANCED_DEST_DIR + "?noop=true&recursive=true&idempotent=false&include=.*.json")
            .choice()
                .when(body().isNotNull())
                    .process( new Processor(){
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            File file = exchange.getIn().getBody(File.class);
                            fileList.add(file.getName());
                        }
                    })
                .otherwise()
                    .process( new Processor(){
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            if (fileList.size() != 0)
                                exchange.getMessage().setBody(String.join("\n", fileList));
                            fileList.clear();
                        }
                    })
                .end()
            .to("bean:serviceBean?method=commit(${body}");

        rest("/commits")
            .get()
            .routeId("restCommits")
            .produces("application/json")
            .to("direct:getCommits");
    }
}
