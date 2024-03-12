package com.beanfarm.search;

import com.beanfarm.api.BeanfarmHttpClient;
import com.beanfarm.api.Question;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;

@Command(name = "search", description = "Search questions matching criteria.", mixinStandardHelpOptions = true)
final public class SearchCommand implements Runnable {

    @Option(names = {"-q", "--query"}, description = "Search Phrase.")
    String query = "";

    @Option(names = {"-t", "--tag"}, description = "Search inside specific tag.")
    String tag = "";

    @Option(names = {"-n", "--limit"}, description = "Limit results. Default 10.")
    int limit = 10;

    @Option(names = {"-s", "--sort"}, description = "Available values: relevance, votes, creation, activity. Default: relevance")
    String sort = "relevance";

    @Option(names = { "--verbose"}, description = "Print verbose output.")
    Boolean verbose = false;

    @Inject
    BeanfarmHttpClient client;

    @Override
    public void run(){
        var response = client.search(query,tag,limit,sort);
        response.items.stream().map(SearchCommand::formatQuestion).forEach(System.out::println);
    if (verbose) {
        System.out.printf(
            "\nItems size: %d | Quota max: %d | Quota remaining: %d | Has more: %s\n",
            response.items.size(),
            response.quotaMax,
            response.quotaRemaining,
            response.hasMore
    );
    }
    }
        static private String formatQuestion(final Question question) {
        return Ansi.AUTO.string(String.format(
                "@|bold,fg(green) %s|@ %d|%d @|bold,fg(yellow) %s|@\n      %s",
                question.accepted ? "✔" : "",
                question.score,
                question.answers,
                question.title,
                question.link
        ));
        }

}