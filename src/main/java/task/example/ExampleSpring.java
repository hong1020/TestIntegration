package task.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hongcheng on 8/24/15.
 */
public class ExampleSpring {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("taskConfig.xml");
        ApplicationContext ctx_2 = new ClassPathXmlApplicationContext("taskConfig.xml");

        try {
            Thread.sleep(20000);
        } catch (Exception e) {

        }
    }
}
