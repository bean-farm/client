package com.beanfarm;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanfarmCommandTest {

    @Test
    public void testWithCommandLineOption() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
           
            PicocliRunner.run(BeanfarmCommand.class, ctx, new String[] {"-v"});
            out.println(baos.toString());
            // beanfarm
            
            assertTrue(baos.toString().contains("Hi!"));
        }
    }

    @Test
    public void testSearchCommand() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { "search", "-q", "merge maps", "-t", "java", "--verbose" };
            PicocliRunner.run(BeanfarmCommand.class, ctx, args);
            out.println(baos.toString());
            // beanfarm
           Pattern pattern = Pattern.compile("\\$/✔\\? \\d+\\|\\d+ [^\\n]+\\n {6}https://stackoverflow.com/questions/\\d+/[a-z0-9\\-]+/\\$");
            
            assertTrue(pattern.matcher(baos.toString()).find());
        }
    }
    
}
