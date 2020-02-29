package thirscript;

import java.util.HashMap;
import java.util.Map;

public class Var
{
    final boolean constant;
    ThObject value;

    public Var(ThObject value)
    {
        this(value, false);
    }

    public Var(ThObject value, boolean constant)
    {
        this.constant = constant;
        this.value = value;
    }

    public ThObject getValue()
    {
        return value;
    }
    
    public void setValue(ThObject value)
    {
        if (constant) throw new UnsupportedOperationException("Cannot set constant");
        this.value = value;
    }

    public boolean isConst()
    {
        return constant;
    }

    private static final Map<ThObject, Var> cache = new HashMap<>();

    public static Var newVar(ThObject value, boolean constant)
    {
        if(constant) return constant(value);
        return new Var(value, constant);
    }
    
    public static Var constant(ThObject value)
    {
        Var v = cache.get(value);
        if (v == null) cache.put(value, v = new Var(value, true));
        return v;
    }
}
