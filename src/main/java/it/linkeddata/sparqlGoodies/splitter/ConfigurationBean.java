package it.linkeddata.sparqlGoodies.splitter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.jena.riot.RDFDataMgr;
import org.springframework.web.context.ServletContextAware;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class ConfigurationBean implements ServletContextAware {
	private static Model confModel = null;
	private static ServletContext context;

	private static String staticResources, confFile;

	public ConfigurationBean() throws IOException, Exception {

	}

	public void populateBean() throws IOException, Exception {
		System.out.println("Initializing configuration " + confFile);
		File configFile = new File(confFile);
		if (!configFile.isAbsolute()) {
			configFile = new File(context.getRealPath("/") + "/WEB-INF/" + confFile);
		}
		if (!configFile.exists()) {
			throw new Exception("Configuration file not found (" + configFile.getAbsolutePath() + ")");
		}
		confModel = RDFDataMgr.loadModel(configFile.getAbsolutePath());

		/* static resources suffix */
		staticResources = getSingleConfValue("staticResourcePrefixURL", "staticPath");
		if (staticResources.equals("")) {
			staticResources = "staticPath";
		}
		staticResources = (staticResources).replaceAll("/$", "").replaceAll("^/", "");
	}

	public String getSingleConfValue(String prop) {
		return getSingleConfValue(prop, null);
	}

	public String getSingleConfValue(String prop, String defaultValue) {
		NodeIterator iter = confModel.listObjectsOfProperty(confModel.createResource(confModel.getNsPrefixURI("conf") + "conf"), genProp(prop));
		while (iter.hasNext()) {
			RDFNode node = iter.next();
			return node.toString();
		}
		return defaultValue;
	}

	public String getSingleConfValueForSubject(String subject, String prop, String defaultValue) {
		NodeIterator iter = confModel.listObjectsOfProperty(confModel.createResource(subject), genProp(prop));
		while (iter.hasNext()) {
			RDFNode node = iter.next();
			System.out.println(node);
			return node.toString();
		}
		return defaultValue;
	}

	public String getSingleConfValue(String prop1, String prop, String defaultValue) {
		NodeIterator iter = confModel.listObjectsOfProperty(genProp(prop1));
		while (iter.hasNext()) {
			RDFNode node = iter.next();
			NodeIterator iter1 = confModel.listObjectsOfProperty(node.asResource(), genProp(prop));
			while (iter1.hasNext()) {
				RDFNode node1 = iter1.next();
				return node1.toString();
			}
		}
		return defaultValue;
	}

	public List<String> getMultiConfValue(String prop) {
		List<String> result = new ArrayList<String>();
		NodeIterator iter = confModel.listObjectsOfProperty(genProp(prop));
		while (iter.hasNext()) {
			RDFNode node = iter.next();
			result.add(node.toString());
		}
		return result;
	}

	public List<String> getMultiConfValue(String prop1, String prop) {
		List<String> result = new ArrayList<String>();
		NodeIterator iter = confModel.listObjectsOfProperty(genProp(prop1));
		while (iter.hasNext()) {
			RDFNode node = iter.next();
			NodeIterator iter1 = confModel.listObjectsOfProperty(node.asResource(), genProp(prop));
			while (iter1.hasNext()) {
				RDFNode node1 = iter1.next();
				result.add(node1.toString());
			}
		}
		return result;
	}

	Property genProp(String prop) {
		Property property = null;
		if (prop.startsWith("http")) {
			property = confModel.createProperty(prop);
		} else {
			property = confModel.createProperty(confModel.getNsPrefixURI(prop.contains(":") ? prop.replaceAll(":.+", "") : "conf"), prop.replaceAll(".+:", ""));
		}
		return property;
	}

	public String getStaticResources() {
		return staticResources;
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		context = arg0;
		try {
			populateBean();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setConfFile(String confFile1) {
		confFile = confFile1;
	}
}
