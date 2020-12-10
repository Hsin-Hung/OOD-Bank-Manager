/**
 * Helper method used to pass function callbacks as arguments.
 * @param <T> The return type of the function
 * @param <U> The argument type of the single argument
 */
public interface SingleArgMethod<T, U> {
    public T apply(U arg);
}
