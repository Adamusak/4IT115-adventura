/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package com.github.adamusak.adventura.ui;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import com.github.adamusak.adventura.logika.IHra;
import com.github.adamusak.adventura.logika.Prostor;
import com.github.adamusak.adventura.logika.Vec;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
	private JFXListView<String> Batoh;
	@FXML
	private ImageView uzivatel;
	@FXML
	private JFXHamburger hamburger;
	@FXML
	private JFXButton Napoveda;
	@FXML
	public JFXButton KonecHry;
	@FXML
	JFXDrawer drawer;

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
	@FXML
	public void Seber() {
		Vec koho = seznamVeciMistnost.getSelectionModel().getSelectedItem();
		if (koho == null) {
			String vystupPrikazu = hra.zpracujPrikaz("seber");
			vystup.appendText("\n\n-------seber-------\n");
			vystup.appendText(vystupPrikazu);
		}
		else {
			String input = ("seber " + seznamVeciMistnost.selectionModelProperty().get().getSelectedItem().toString());
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
		};
		
	}
	@FXML
	public void Jdi() {
		Prostor kam = seznamVychodu.getSelectionModel().getSelectedItem();
		if (kam == null) {
			String vystupPrikazu = hra.zpracujPrikaz("jdi");
			vystup.appendText("\n\n-------jdi-------\n");
			vystup.appendText(vystupPrikazu);
		}
		else {
			String input = ("jdi " + seznamVychodu.selectionModelProperty().get().getSelectedItem().toString());
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
		}
	}
	@FXML
	public void Napoveda() {
		/*String input = ("napoveda");
		String vystupPrikazu = hra.zpracujPrikaz(input);
		vystup.appendText("\n\n-------" + input + "-------\n");
		vystup.appendText(vystupPrikazu);
		
		HamburgerBasicCloseTransition zavrit = new HamburgerBasicCloseTransition(hamburger);
		zavrit.setRate(0);

		zavrit.play();
		drawer.toggle();*/
	}


	/**
	 * Metoda bude soužit pro předání objektu se spuštěnou hrou kontroleru a zobrazí
	 * stav hry v grafice.
	 * 
	 * @param objekt
	 *            spuštěné hry
	 */
	public void inicializuj(IHra hra) {
		// scene.getStylesheets().add(getClass().getResource("css/scene.css").toExternalForm());
		drawer.open();
		vystup.setText(hra.vratUvitani());
		vystup.setEditable(false);
		this.hra = hra;
		seznamVeciMistnost.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVeci());
		seznamVychodu.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
		uzivatel.setX(hra.getHerniPlan().getAktualniProstor().getX());
		uzivatel.setY(hra.getHerniPlan().getAktualniProstor().getY());
		hra.getHerniPlan().addObserver(this);
		
		/*Nastavení animace pro menu ikony*/
		HamburgerBasicCloseTransition transition = new HamburgerBasicCloseTransition(hamburger);
		transition.setRate(-1);
		hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
		        transition.setRate(transition.getRate()*-1);
		        transition.play();
		        drawer.toggle();
		});
		
		Napoveda.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
			String input = ("napoveda");
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
			transition.setRate(transition.getRate()*-1);
	        transition.play();
	        drawer.toggle();
	});
		
		KonecHry.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
			Stage stage = (Stage) KonecHry.getScene().getWindow();
		    stage.close();
	});
		
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		seznamVeciMistnost.getItems().clear();
		seznamVychodu.getItems().clear();
		seznamVeciMistnost.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVeci());
		seznamVychodu.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
		uzivatel.setX(hra.getHerniPlan().getAktualniProstor().getX());
		uzivatel.setY(hra.getHerniPlan().getAktualniProstor().getY());

		String vystupPrikazu = hra.zpracujPrikaz("obsahBatohu");
		String[] ItemyBatohu = vystupPrikazu.split(":");
		Batoh.getItems().clear();
		if (ItemyBatohu[1].contains(", ")) {
			String[] Itemy = ItemyBatohu[1].split(", ");
			Batoh.getItems().addAll(Itemy);
		}
	}
}