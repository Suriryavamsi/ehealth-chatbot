@SpringBootApplication
public class EhealthChatApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EhealthChatApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EhealthChatApplication.class);
    }
}
