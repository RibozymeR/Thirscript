package thirscript;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import thirscript.expr.IntLiteral;

public class ThObject
{
    /**
    Object := new(){
        (_ := _)        // <- kinda?
        _native := 0    // <- has (non-visible) internal data
        true = -1
    }
     */
    public static final ThObject OBJECT;

    public static final Var NULL = Var.constant(null);

    static
    {
        OBJECT = new ThObject();
        OBJECT.vars.put("_native", Var.constant(ThInteger.FALSE));
        OBJECT.vars.put("true", new Var(new ThFunction(new IntLiteral(ThInteger.TRUE), List.of())));
    }

    public final Set<ThObject> protos;
    public final Map<String, Var> vars;

    // bootstrapping compiler
    private ThObject()
    {
        protos = Collections.emptySet();
        vars = new HashMap<>();
        vars.put("_", Var.constant(this));
    }

    public ThObject(Collection<ThObject> protos, Map<String, Var> new_vars)
    {
        this.protos = protos == null || protos.isEmpty() ? Set.of(OBJECT) : Set.copyOf(protos);

        vars = new HashMap<>(new_vars);
        vars.put("_native", Var.constant(ThInteger.FALSE));
        vars.put("_", Var.constant(this));
    }

    public long istrue()
    {
        ThObject testf = getVar("true").getValue();
        if (!(testf instanceof IFunction) || !((IFunction) testf).can_eval(Collections.emptyList()))
            throw new RuntimeException("Member true has to be a function");

        ThObject testr = ((IFunction) testf).eval(Collections.emptyList());
        if (!(testr instanceof ThInteger)) throw new RuntimeException("Function true has to return integer");

        return ((ThInteger) testr).value;
    }

    public Var getVar(String name)
    {
        if (vars.containsKey(name)) return vars.get(name);

        for (ThObject proto : protos)
        {
            Var v = proto.getVar(name);
            if (v != null) return v;
        }
        return null;
    }
}
