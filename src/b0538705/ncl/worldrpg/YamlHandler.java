package b0538705.ncl.worldrpg;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlHandler {

	public static Scenario loadScenario(InputStream is)
	{
		Yaml yaml = new Yaml(new Constructor(Scenario.class));
		return (Scenario) yaml.load(is);		

	}

}
