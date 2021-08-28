import java.util.UUID;

public class App {
    public static void main(String[] args) throws Exception {
        UUID pizzaID = UUID.randomUUID();
        Pizza pizza = new Pizza();
        pizza.setId(pizzaID);
        pizza.setName("Barbacoa");
        pizza.setUrl("url");

        EntityManagerImp.buildConnection(ConfigurationImp.getConfiguration())
                .addStatement(pizza, "INSERT INTO pizza(id, name, url) VALUES(?,?,?)", (statement, entity) -> {
                    statement.setBytes(1, Converter.fromUUIDtoByteArray(entity.getId()));
                    statement.setString(2, entity.getName());
                    statement.setString(3, entity.getUrl());
                })
                .save();

        System.out.println("Insertamos la pizza: " + pizza.getName());

        Pizza pizza1 = EntityManagerImp.buildConnection(ConfigurationImp.getConfiguration())
                                                 .addStatement(pizza, "SELECT id, name, url FROM pizza WHERE id=?", (statement, entity) -> {
                                                    statement.setBytes(1, Converter.fromUUIDtoByteArray(pizzaID));
                                                 })
                                                 .select(Pizza.class, (resultSet, entity) -> {
                                                    entity.setId(Converter.fromByteArrayToUUID(resultSet.getBytes("id")));
                                                    entity.setName(resultSet.getString("name"));
                                                    entity.setUrl(resultSet.getString("url"));
                                                 }).orElseThrow();

        System.out.println("Pizza obtenida: "+pizza1.getName()+", con url: "+ pizza1.getUrl());
    }
}