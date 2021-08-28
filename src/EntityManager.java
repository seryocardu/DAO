import java.util.Optional;

public interface EntityManager {

    public void save();

    public <T> EntityManager addStatement(final T entity, String sql, Statement<T> statement);

    public <T> EntityManager addRangeStatement(final Iterable<T> iterable, String sql, Statement<T> statement);

    public <T> Optional<T> select(Class<T> clazz, Resultset<T> resultset);
}