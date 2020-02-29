package thirscript;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ThString extends ThObject
{
    /**
     *  String = new(Object){
     *      _native := -1
     *      length := /length/
     *  }
     */

    private static Map<String, ThString> cache = new TreeMap<>();

    public final String value;

    private ThString(String value)
    {
        super(Set.of(OBJECT), Collections.emptyMap());

        this.value = value;
        vars.put("_native", Var.constant(ThInteger.TRUE));
        // vars.put("_value", Var.constant(this));
        vars.put("length", Var.constant(ThInteger.valueOf(value.length())));
    }

    public static ThString valueOf(String v)
    {
        if (cache.containsKey(v)) return cache.get(v);

        ThString s = new ThString(v);
        cache.put(v, s);
        return s;
    }
}
