package thirscript;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ThInteger extends ThObject
{
    /**
     *  Int = new(Object){
     *      _native := true
     *  }
     */

    public static final ThInteger TRUE = valueOf(-1);
    public static final ThInteger FALSE = valueOf(0);

    private static Map<Long, ThInteger> cache = new TreeMap<>();

    public final long value;

    private ThInteger(long value)
    {
        super(Set.of(OBJECT), Collections.emptyMap());

        this.value = value;
        vars.put("_native", Var.constant(ThInteger.TRUE));
        // vars.put("_value", Var.constant(this));
        // vars.put("true", Var.constant(value == 0 ? ThInteger.FALSE : ThInteger.TRUE)); <- function, not value
    }

    public long istrue()
    {
        return value;
    }

    public static ThInteger valueOf(long v)
    {
        if (cache.containsKey(v)) return cache.get(v);

        ThInteger i = new ThInteger(v);
        cache.put(v, i);
        return i;
    }
}
