import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityManagerImp implements EntityManager {

    private List<Runables> runables = new ArrayList<Runables>();
    private Configuration configuration = null;

    public EntityManagerImp(Configuration configuration){
        this.configuration = configuration;
    }

    public static EntityManager buildConnection(Configuration configuration){
        return new EntityManagerImp(configuration);
    }

    @Override
    public <T> EntityManager addStatement(T entity, String sql, Statement<T> statement) {
        Runables runable = new RunablesImp<T>(sql, entity, statement);
        this.runables.add(runable);
        return this;
    }

    @Override
    public <T> EntityManager addRangeStatement(Iterable<T> iterable, String sql, Statement<T> statement) {
        
        for(T entity : iterable){
            Runables runable = new RunablesImp<T>(sql, entity, statement);
            this.runables.add(runable);
        }
        return this;
    }

    @Override
    public <T> Optional<T> select(Class<T> clazz, Resultset<T> resultset) {
        
        Connection connection = null;
        T entity = null;

        try{
            connection = DriverManager.getConnection(
                this.configuration.getUrl(),
                this.configuration.getUser(),
                this.configuration.getPassword()
            );
 
            Runables runable = this.runables.get(0);

            PreparedStatement statement = connection.prepareStatement(runable.getSQL());
            runable.run(statement);

            ResultSet resultSetSQL = statement.executeQuery();

            while(resultSetSQL.next()){

                entity = clazz.getConstructor().newInstance();
                resultset.run(resultSetSQL, entity);
            }

        }catch(SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
            e.printStackTrace();
        }finally{

            this.runables.clear();
            try {
                if(!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return Optional.of(entity);
    }

    @Override
    public void save() {

        Connection connection = null;

        try{
            connection = DriverManager.getConnection(
                this.configuration.getUrl(),
                this.configuration.getUser(),
                this.configuration.getPassword()
            );
            connection.setAutoCommit(false);

            for(Runables runable : this.runables){
                
                PreparedStatement statement = connection.prepareStatement(runable.getSQL());
                runable.run(statement);
                statement.executeUpdate();
            }
            connection.commit();

        }catch(SQLException e){
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally{

            this.runables.clear();
            try {
                if(!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }  
}