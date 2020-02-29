package thirscript;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import thirscript.expr.Expr;

public class ThFunction extends ThObject implements IFunction
{
    /**
     *  Function = new(Object){
     *      _native := -1
     *  }
     */

    final Expr func;
    final List<String> arg_names;

    public ThFunction(Expr func, List<String> arg_names)
    {
        super(Set.of(OBJECT), Collections.emptyMap());
        this.func = func;
        this.arg_names = Collections.unmodifiableList(arg_names);
    }

    public boolean can_eval(List<ThObject> arg_types)
    {
        if (arg_types.size() != arg_names.size()) return false;
        // TODO should not always be true ?
        return true;
    }

    public ThObject eval(List<ThObject> args)
    {
        Map<String, Var> new_env = new HashMap<>();
        /*
         * new_env.remove("_"); if (thiso != null) new_env.put("_", Var.constant(thiso));
         */

        for (int i = 0; i < arg_names.size(); ++i)
            new_env.put(arg_names.get(i), new Var(args.get(i)));

        return func.eval(new_env);
    }
}
