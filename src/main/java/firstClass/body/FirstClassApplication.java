package firstClass.body;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: wkit
 * @Date: 2019-10-12 18:51
 */
@Slf4j
@SpringBootApplication
public class FirstClassApplication {
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(FirstClassApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("Application Started");
        log.info("Access URLs");
        log.info("-------------------------------------------------");
        log.info("Local:\t\thttp://127.0.0.1:{}{}", env.getProperty("server.port"), env.getProperty("server.servlet" +
                ".context-path"));
        log.info("External:\thttp://{}:{}{}", InetAddress.getLocalHost().getHostAddress(), env.getProperty("server" +
                ".port"), env.getProperty("server.servlet.context-path"));
        log.info("-------------------------------------------------");

    }
}
