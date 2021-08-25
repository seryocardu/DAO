public class ConfigurationImp implements Configuration {

    private static Configuration configuration = null;

    public static Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new ConfigurationImp();
        }
        return configuration;
    }

    public String getUser() {
        return System.getenv("user");
    }

    public String getPassword() {
        return System.getenv("pass");
    }

    public String getUrl() {
        return System.getenv("url");

    }
}