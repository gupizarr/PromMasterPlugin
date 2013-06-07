package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery;
/**
 * Configuration
 *  @author Gustavo Pizarro
*/

public class OLAPDiscoveryConfiguration {
  String name;
 
  public OLAPDiscoveryConfiguration(String name) {
    this.name = name;
  }
 
  public String getName() {
    return name;
  }
 
  public void setName(String name) {
    this.name = name;
  }
}

