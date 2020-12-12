/**
 * Helper method used implement the strategy pattern to pass function callbacks as arguments.
 * @param <T> The return type of the function
 * @param <U> The argument type of the single argument
 */
public interface SingleArgMethod<T, U> {
    T apply(U arg);
}
