package task.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hongcheng on 8/27/15.
 */
public class ExampleSpringOne {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("taskConfigOne.xml");
        ApplicationContext ctx_2 = new ClassPathXmlApplicationContext("taskConfigOne.xml");

        try {
            Thread.sleep(20000);
        } catch (Exception e) {

        }
    }
}
