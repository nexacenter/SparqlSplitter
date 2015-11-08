package it.linkeddata.sparqlGoodies.splitter;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

public class Query {

	Model queryModel;
	Dataset dataset;

	public Query(String sparqlUrl, String namedGraph) {

		/* defining sparql type */

		if (sparqlUrl.startsWith("http")) {

		} else if (sparqlUrl.startsWith("virtuoso")) {

		} else {
			System.out.println("------------------------------------------ " + namedGraph);
			dataset = TDBFactory.createDataset(sparqlUrl);
			dataset.begin(ReadWrite.READ);
			queryModel = dataset.getDefaultModel();// NamedModel(namedGraph);
			System.out.println("------------------------------------------ end");
		}
	}

	public Object doQuery(String sparqlQuery) {

		String sparqlTest = sparqlQuery.toLowerCase();
		QueryExecution q = QueryExecutionFactory.create(sparqlQuery, queryModel);

		if (sparqlTest.contains("construct")) {
			Model m = q.execConstruct();
			q.close();
			return m;
		} else if (sparqlTest.contains("ask")) {
			boolean b = q.execAsk();
			q.close();
			return b;
		} else if (sparqlTest.contains("describe")) {
			Model m = q.execDescribe();
			q.close();
			return m;
		} else {
			ResultSet r = q.execSelect();
			q.close();
			return r;
		}

	}

	public void done() {
		try {
			queryModel.close();
		} catch (Exception e) {
		}
		try {
			dataset.close();
		} catch (Exception e) {
		}
	}
}
