/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	this.txtResult.clear();
    	Album a1= this.cmbA1.getSelectionModel().getSelectedItem();
    	if(a1 != null) {
    		List<Album> result = this.model.getSuccessoriNodoOrdineBilancioDecr(a1);
    		if(result.isEmpty()) {
    			this.txtResult.setText("Nessuna adiacenza trovata");
    			return;
    		}
    		for(Album a: result) {
    			this.txtResult.appendText(a.getTitle()+", bilancio="+a.getBilancio()+"\n");
    		}
    	}else {
    		this.txtResult.setText("Scegliere a1");
    		return;
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
//    	 Alla pressione del bottone “Calcola Percorso”, trovare e
//    	 stampare (se esiste) un cammino semplice sul grafo calcolato
//    	 nel punto 1 che abbia le seguenti caratteristiche:
//    	 • parta da a1 (selezionato al punto 1d) e termini in a2;
//    	 • attraversi solo archi con peso maggiore o uguale a x;
//    	 • tocchi il maggior numero di vertici che hanno un “bilancio” maggiore di
//    	 	quello del vertice di partenza a1 (per il calcolo del “bilancio”
//    		di un vertice si veda il punto 1d).

    	if(this.model.getGrafo().vertexSet().isEmpty()) {
    		this.txtResult.setText("Grafo non creato");
    		return;
    	}
    	
    	this.cmbA2.getItems().addAll(this.model.getGrafo().vertexSet());
    	
    	Album a1= this.cmbA1.getSelectionModel().getSelectedItem();
    	
    	String input = txtX.getText();    	
    	
    	if (input == "") {
    		txtResult.setText("Input string for X is empty.");
    		return;
    	}
    	
    	try {
    		int x = Integer.parseInt(input);
    		Album a2 = this.cmbA2.getValue();
    		if(a2 != null && a1 != null){
    			List<Album> cammino = this.model.calcolaCammino(x, a1, a2);
    			if(cammino.isEmpty()) {
    				this.txtResult.setText("Non è stato trovato un cammino semplice");
    				return;
    			}else {
    				this.txtResult.appendText("E' stato trovato un cammino semplice:\n");
    				for(Album a: cammino) {
    					this.txtResult.appendText(a+"\n");
    				}
    			}
    		}else {
    			this.txtResult.setText("Scegliere un album.");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire una soglia, x.");
    		return;
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.txtX.clear();
    	this.cmbA1.getSelectionModel().clearSelection();
    	this.cmbA2.getSelectionModel().clearSelection();
    	
    	String input = txtN.getText();    	
    	if (input == "") {
    		txtResult.setText("Input string for N is empty.");
    		return;
    	}
    	
    	try {
    		int n = Integer.parseInt(input);
    		this.model.creaGrafo(n);
    		this.txtResult.appendText("Grafo creato!\n");
    		this.txtResult.appendText("#Vertici: "+this.model.getGrafo().vertexSet().size()+"\n");
    		this.txtResult.appendText("#Archi: "+this.model.getGrafo().edgeSet().size()+"\n");
    		
    		List<Album> albums = new ArrayList<>(this.model.getGrafo().vertexSet());
    		Collections.sort(albums);
    		this.cmbA1.getItems().addAll(albums);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire n");
    		return;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
