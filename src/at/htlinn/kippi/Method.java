package at.htlinn.kippi;

public abstract class Method<T>
{
    public abstract T call(T... arguments);
}
