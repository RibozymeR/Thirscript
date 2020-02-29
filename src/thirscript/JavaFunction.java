package thirscript;

import java.lang.invoke.MethodHandle;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongFunction;

public class JavaFunction extends ThObject implements IFunction
{
    private static final Map<Class<?>, LongFunction<?>> numbers;
    static
    {
        numbers = new HashMap<>();
        numbers.put(boolean.class, l -> Boolean.valueOf(l != 0));
        numbers.put(Boolean.class, l -> Boolean.valueOf(l != 0));

        numbers.put(byte.class, l -> Byte.valueOf((byte) l));
        numbers.put(Byte.class, l -> Byte.valueOf((byte) l));
        numbers.put(char.class, l -> Character.valueOf((char) l));
        numbers.put(Character.class, l -> Character.valueOf((char) l));
        numbers.put(short.class, l -> Short.valueOf((short) l));
        numbers.put(Short.class, l -> Short.valueOf((short) l));

        numbers.put(int.class, l -> Integer.valueOf((int) l));
        numbers.put(Integer.class, l -> Integer.valueOf((int) l));
        numbers.put(long.class, Long::valueOf);
        numbers.put(Long.class, Long::valueOf);

        numbers.put(float.class, Float::valueOf);
        numbers.put(Float.class, Float::valueOf);
        numbers.put(double.class, Double::valueOf);
        numbers.put(Double.class, Double::valueOf);

        numbers.put(BigInteger.class, BigInteger::valueOf);
    }

    final MethodHandle func;

    public JavaFunction(MethodHandle func)
    {
        super(Set.of(OBJECT), Collections.emptyMap());
        this.func = func;
    }

    public boolean can_eval(List<ThObject> arg_types)
    {
        if (arg_types.size() != func.type().parameterCount()) return false;

        // TODO should not always be true
        return true;
    }

    public ThObject eval(List<ThObject> th_args)
    {
        Object[] args = new Object[th_args.size()];

        List<Class<?>> arg_types = func.type().parameterList();

        Arrays.setAll(args, i -> convert(th_args.get(i), arg_types.get(i)));

        try
        {
            func.invoke(args);
            return ThInteger.TRUE;
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
        return ThInteger.FALSE;
    }

    public static Object convert(ThObject thobj, Class<?> clazz)
    {
        // TODO replace null
        if (thobj == null) return null;

        LongFunction<?> f = numbers.get(clazz);
        if (f != null) return f.apply(((ThInteger) thobj).value);

        if (String.class.isAssignableFrom(clazz)) return ((ThString) thobj).value;

        if (ThObject.class.isAssignableFrom(clazz)) return thobj;

        throw new IllegalArgumentException("Object " + thobj + " not convertible to " + clazz);
    }
}
