import org.apache.log4j.Logger;

/**
 * Created by zxlvoid on 2017/12/27 0027.
 * 日志生产
 */
public class LoggerGenerator {

    private static Logger logger = Logger.getLogger(LoggerGenerator.class.getName());

    public static void main(String[] args) throws InterruptedException {

        int index = 0;
        while (true) {
            Thread.sleep(1000);
            logger.info("current number is:" + index++);
        }
    }

}
