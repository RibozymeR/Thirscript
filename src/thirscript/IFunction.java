package thirscript;

import java.util.List;

public interface IFunction
{
    boolean can_eval(List<ThObject> args);

    ThObject eval(List<ThObject> args);
}
