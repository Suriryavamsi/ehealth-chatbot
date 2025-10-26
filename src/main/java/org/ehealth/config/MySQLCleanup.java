package org.ehealth.config;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class MySQLCleanup {

    @PreDestroy
    public void cleanup() {
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
            System.out.println("MySQL AbandonedConnectionCleanupThread stopped.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
