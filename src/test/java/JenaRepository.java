import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.imageio.stream.FileImageInputStream;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

public class JenaRepository {

	private String assemblerFile;
	Dataset dataset;

	public Dataset getDataset() {
		return dataset;
	}

	public JenaRepository() {

	}

	public void init() {

		dataset = TDBFactory.createDataset("testTDB");
		dataset.begin(ReadWrite.WRITE);

	}

	/*
	 * chiude le connessioni con il repository
	 */
	public void close() {
		try {

			dataset.commit();
			TDB.sync(dataset);
			dataset.end();
			dataset.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void commit() throws Exception {
		try {
			dataset.commit();
			TDB.sync(dataset);
			dataset.end();
			dataset.begin(ReadWrite.WRITE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("errore jena");
		}

	}

	public static void main(String[] args) throws Exception {

		JenaRepository a = new JenaRepository();
		a.init();

		a.getDataset().getDefaultModel().read(new FileInputStream(new File("C:/Users/utente/Downloads/bpr-skos.rdf")), "http://example.com/prova");

		a.commit();

	}

	public String getAssemblerFile() {
		return assemblerFile;
	}
}
