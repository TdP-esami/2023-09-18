package it.polito.tdp.gosales;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.gosales.model.Arco;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.RetailersExt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnRicorsione;

    @FXML
    private Button btnVolumi;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<String> cmbNazione;
    
    @FXML
    private TextField txtN;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaVolumi(ActionEvent event) {
    	List<RetailersExt> retailersVolume = this.model.calcolaVolume();
    	this.txtResult.appendText("\nIl volume di vendita dei retailers nel grafo è: \n");
    	for(RetailersExt rext : retailersVolume) {
    		this.txtResult.appendText(rext + "\n");
    	}
    	this.btnRicorsione.setDisable(false);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Integer anno = this.cmbAnno.getValue();
    	if (anno==null) {
    		this.txtResult.setText("Selezionare un anno\n");
    		return;
    	}
    	String nazione = this.cmbNazione.getValue();
    	if (nazione==null) {
    		this.txtResult.setText("Selezionare una nazione\n");
    		return;
    	}
    	this.model.creaGrafo(nazione, anno);
    	
    	
    	//stampa informazioni
    	List<Arco> archi = this.model.getArchi();
    	this.txtResult.setText("Grafo creato.\n");
    	this.txtResult.appendText("Ci sono " + this.model.getVertici().size() + " vertici.\n");
    	this.txtResult.appendText("Ci sono " + archi.size() + " archi.\n\n");
    	
//    	this.txtResult.appendText("Gli archi sono:\n");
//    	for(Arco a : archi) {
//    		this.txtResult.appendText(a + "\n");
//    	}
    	
    	this.btnVolumi.setDisable(false);
    	this.btnRicorsione.setDisable(true);
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	int N =0;
    	try {
    		N = Integer.parseInt(this.txtN.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("N deve essere un numero intero.");
    		return;
    	}
    	if (N<2) {
    		this.txtResult.setText("N deve essere almeno 2.");
    		return;
    	}
    	
    	List<Arco> archi = this.model.calcolaCamminoChiuso(N);
    	this.txtResult.appendText("\nIl peso del cammino è : " + this.model.getPesoCamminoChiuso() + "\n");
    	this.txtResult.appendText("Gli archi del cammino sono:\n");
    	for(Arco a : archi) {
    		this.txtResult.appendText(a + "\n");
    	}
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicorsione != null : "fx:id=\"btnRicorsione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnVolumi != null : "fx:id=\"btnVolumi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        this.btnVolumi.setDisable(true);
        this.btnRicorsione.setDisable(true);
    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbNazione.getItems().setAll(this.model.getCountries());
    	this.cmbAnno.getItems().setAll(this.model.getYears());
    	
    }

}
