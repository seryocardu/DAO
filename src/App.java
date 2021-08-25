// import java.util.UUID;

public class App {
    public static void main(String[] args) throws Exception {
        Pizza pizza = new Pizza();
        pizza.id = 12;
        pizza.name = "Hawaiana";
        pizza.url = "url";
        EntityManagerImp.buildConnection(ConfigurationImp.getConfiguration())
                .addStatement(pizza, "INSERT INTO pizza(id, name, url) VALUES(?,?,?)", (statement, entity) -> {
                    statement.setInt(1, entity.id);
                    statement.setString(2, entity.name);
                    statement.setString(3, entity.url);
                }).save();

        // pizza = EntityManagerImp.buildConnection(ConfigurationImp.getConfiguration())
        //         .addStatement(pizza, "SELECT id, name, url FROM pizza WHERE id=?", (statement, entity) -> {
        //             statement.setBytes(1, Converter.fromUUIDtoByteArray(UUID.randomUUID()));
        //         })
        //         .select(Pizza.class, (resultSet, entity) -> {
        //             entity.setId(resultSet.getBytes("id"));
        //             entity.setName(resultSet.getString("name"));
        //             entity.setPrice(resultSet.getDouble("price"));
        //         }).orElseThrow();

    }
}