package PromMasterPlugin;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
 
@Plugin(name = "Show ImproveDiscover",
        parameterLabels = { "Person" },
        returnLabels = { "Person Viewer" },
        returnTypes = { JComponent.class },
        userAccessible = false)
@Visualizer
public class PersonVisualizer 
{
  @PluginVariant(requiredParameterLabels = { 0 })
  public static JComponent visualize(final PluginContext context,
                                     final Person person) {
	  
  
    return new JLabel(person.getName().getFirst() + " " + person.getName().getLast());
  }
}