import java.util.ArrayList;
import java.util.List;
// import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class EntityManagerImp implements EntityManager {
    private List<Runables> runables = new ArrayList<Runables>();
    private Configuration configuration = null;

    @Override
    public void save() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                this.configuration.getUrl(), 
                this.configuration.getUser(),
                this.configuration.getPassword());

            connection.setAutoCommit(false);
            
            for (Runables runable : this.runables) {
                PreparedStatement statement = connection.prepareStatement(runable.getSQL());
                runable.run(statement);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            runables.clear();
        }

    }

    @Override
    public <T> EntityManager addStatement(T entity, String sql, Statement<T> statement) {
        Runables runable = new RunablesImp<T>(sql, entity, statement);
        this.runables.add(runable);
        return this;
    }

    @Override
    public <T> EntityManager addRangeStatement(Iterable<T> iterable, String sql, Statement<T> statement) {
        for (T t : iterable) {
            Runables runable = new RunablesImp<T>(sql, t, statement);
            this.runables.add(runable);
        }
        return this;
    }

    public static EntityManager buildConnection(Configuration configuration) {
        return new EntityManagerImp(configuration);
    }

    private EntityManagerImp(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T Select(ResultSet resultSet) {
        // TODO Auto-generated method stub
        return null;
    }

    // @Override
    // public <T> T Select(ResultSet resultSet) {
    //     Connection connection = null;
    //     try {
    //         connection = DriverManager.getConnection(
    //             this.configuration.getUrl(), 
    //             this.configuration.getUser(),
    //             this.configuration.getPassword());

    //         connection.setAutoCommit(false);

    //         for (Runables runable : this.runables) {
    //             PreparedStatement statement = connection.prepareStatement(runable.getSQL());
    //             runable.run(statement);
    //             resultSet = statement.executeQuery();
    //         }
    //         connection.commit();
    //     } catch (SQLException e) {
    //         try {
    //             connection.rollback();
    //         } catch (SQLException gException) {
    //             } e.printStackTrace();
    //         } finally {
                
    //         runables.clear();
    //         try {
    //             if (!(connection == null)) {
    //                 connection.close();
    //             }
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
            
    //     }return entity;
    // }
}

// public <T> T Select(ResultSet){

// Class<T> aClass=T.class(); return aClass.newInstance(); }
