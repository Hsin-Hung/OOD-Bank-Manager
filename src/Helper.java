import java.util.List;

public class Helper {
    public static <T> T getLastItem(List<T> list) {
        return list.get(list.size() - 1);
    }
}
