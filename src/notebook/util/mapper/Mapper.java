package notebook.util.mapper;

public interface Mapper<E, T> {
    T toInput(E e);

    E toOutput(T t);
}