/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package com.github.adamusak.adventura.ui;

import java.util.Observable;
import java.util.Observer;

import com.github.adamusak.adventura.logika.IHra;
import com.github.adamusak.adventura.logika.Prostor;
import com.github.adamusak.adventura.logika.Vec;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Kontroler, který zprostředkovává komunikaci mezi grafikou a logikou adventury
 * 
 * @author Filip Vencovsky, Adam Novak
 *
 */
public class HomeController extends GridPane implements Observer {

	@FXML
	private JFXTextField vstupniText;
	@FXML
	private JFXTextArea vystup;
	@FXML
	private JFXListView<Vec> seznamVeciMistnost;
	@FXML
	private JFXListView<Prostor> seznamVychodu;
	@FXML
	private ImageView uzivatel;

	private IHra hra;

	/**
	 * metoda čte příkaz ze vstupního textového pole a zpracuje ho
	 */
	@FXML
	public void odesliPrikaz() {
		String vystupPrikazu = hra.zpracujPrikaz(vstupniText.getText());
		vystup.appendText("\n\n-------" + vstupniText.getText() + "-------\n");
		vystup.appendText(vystupPrikazu);
		vstupniText.setText("");
		
		if (hra.konecHry()) {
			vystup.appendText("\n\n-------Konec hry-------\n");
			vstupniText.setDisable(true);
		}
	}

	/**
	 * Metoda bude soužit pro předání objektu se spuštěnou hrou kontroleru a zobrazí
	 * stav hry v grafice.
	 * 
	 * @param objekt
	 *            spuštěné hry
	 */
	public void inicializuj(IHra hra) {
		//scene.getStylesheets().add(getClass().getResource("css/scene.css").toExternalForm());
		vystup.setText(hra.vratUvitani());
		vystup.setEditable(false);
		this.hra = hra;
		seznamVeciMistnost.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVeci());
		seznamVychodu.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
		uzivatel.setX(hra.getHerniPlan().getAktualniProstor().getX());
		uzivatel.setY(hra.getHerniPlan().getAktualniProstor().getY());
		hra.getHerniPlan().addObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		seznamVeciMistnost.getItems().clear();
		seznamVychodu.getItems().clear();
		seznamVeciMistnost.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVeci());
		seznamVychodu.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
		uzivatel.setX(hra.getHerniPlan().getAktualniProstor().getX());
		uzivatel.setY(hra.getHerniPlan().getAktualniProstor().getY());

	}

}