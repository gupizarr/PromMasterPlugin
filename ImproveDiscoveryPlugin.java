package PromMasterPlugin;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
 
@Plugin(name = "ImproveDiscover",
        parameterLabels = { "Father", "Mother", "Procreation Configuration" },
        returnLabels = { "Child" },
        returnTypes = { Person.class })
public class ImproveDiscoveryPlugin {
  @UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = { 0, 1, 2 })
  public static Person procreate(final PluginContext context,
                                 final Person father,
                                 final Person mother,
                                 final ImproveDiscoveryConfiguration config) {
    Person child = new Person();
    child.setAge(0);
    child.setName(new Name(config.getName(), father.getName().getLast()));
    return child;
  }
 
  @UITopiaVariant(affiliation = "PUC",
                  author = "Gustavo Pizarro",
                  email = "gupizarr@uc.cl",
                  uiLabel = UITopiaVariant.USEPLUGIN)
  @PluginVariant(requiredParameterLabels = { 0, 1 })
  public static Person procreate(final UIPluginContext context,
                                 final Person father,
                                 final Person mother) {
    ImproveDiscoveryConfiguration config = new ImproveDiscoveryConfiguration("Config");
    
    return procreate(context, father, mother, config);
  }
}
 

