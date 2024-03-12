package com.beanfarm;

import com.beanfarm.auth.AuthCommand;
import com.beanfarm.auth.Credentials;
import com.beanfarm.beanfarmRunner.BeanfarmController;
import com.beanfarm.search.SearchCommand;
import io.micronaut.configuration.picocli.PicocliRunner;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "beanfarm", description = "...",
        mixinStandardHelpOptions = true, subcommands = {SearchCommand.class, AuthCommand.class, BeanfarmController.class})
public class BeanfarmCommand implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) throws Exception {
        Credentials.init();
        PicocliRunner.run(BeanfarmCommand.class, args);
    }

    public void run() {
        // business logic here

        System.out.println("@|bold,fg(green) Welcome to your virtual bean farm @");
        //PicocliRunner.run(AuthCommand.class, "auth");
        

}
}
