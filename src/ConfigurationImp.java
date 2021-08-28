public class ConfigurationImp implements Configuration {

    private static Configuration configuration = null;

    public static Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new ConfigurationImp();
        }
        return configuration;
    }

    @Override
    public String getUser() {
        return System.getenv("userMySQL");
    }

    @Override
    public String getPassword() {
        return System.getenv("passwordMySQL");
    }

    @Override
    public String getUrl() {
        return System.getenv("urlMySQL");

    }
}