/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package com.github.adamusak.adventura.ui;

import java.util.Observable;
import java.util.Observer;
import com.github.adamusak.adventura.logika.Hra;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
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
	private JFXButton InfoHra;
	@FXML
	public JFXButton KonecHry;
	@FXML
	public JFXButton NovaHra;
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
		} else {
			String input = ("seber " + seznamVeciMistnost.selectionModelProperty().get().getSelectedItem().toString());
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
		}
		;

	}

	@FXML
	public void Jdi() {
		Prostor kam = seznamVychodu.getSelectionModel().getSelectedItem();
		if (kam == null) {
			String vystupPrikazu = hra.zpracujPrikaz("jdi");
			vystup.appendText("\n\n-------jdi-------\n");
			vystup.appendText(vystupPrikazu);
		} else {
			String input = ("jdi " + seznamVychodu.selectionModelProperty().get().getSelectedItem().toString());
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
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
		drawer.open();
		vystup.setText(hra.vratUvitani());
		vystup.setEditable(false);
		this.hra = hra;
		seznamVeciMistnost.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVeci());
		seznamVychodu.getItems().addAll(hra.getHerniPlan().getAktualniProstor().getVychody());
		uzivatel.setX(hra.getHerniPlan().getAktualniProstor().getX());
		uzivatel.setY(hra.getHerniPlan().getAktualniProstor().getY());
		hra.getHerniPlan().addObserver(this);

		/* Nastavení animace pro menu ikony */
		HamburgerBasicCloseTransition transition = new HamburgerBasicCloseTransition(hamburger);
		transition.setRate(-1);
		hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			transition.setRate(transition.getRate() * -1);
			transition.play();
			drawer.toggle();
		});
		/* Přidání funkcí tlačítkům */
		Napoveda.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			/*skrytí menu*/
			transition.setRate(transition.getRate() * -1);
			transition.play();
			drawer.toggle();
			
			/* zobrazení v textarea */
			String input = ("napoveda");
			String vystupPrikazu = hra.zpracujPrikaz(input);
			vystup.appendText("\n\n-------" + input + "-------\n");
			vystup.appendText(vystupPrikazu);
			/*HamburgerBasicCloseTransition zavrit = new HamburgerBasicCloseTransition(hamburger);
			zavrit.setRate(-1);
			zavrit.play();
			drawer.toggle();*/
			
			
			/* zobrazení v html */
			Stage stage = new Stage();
			stage.setTitle("Nápověda");
			WebView webview = new WebView();
			webview.getEngine().load(Application.class.getResource("html/napoveda.html").toExternalForm());
			stage.setScene(new Scene(webview, 720, 480));
			stage.setMinWidth(720);
			stage.setMinHeight(480);
			stage.setMaxWidth(720);
			stage.setMaxHeight(480);
			stage.show();
		});
		
		InfoHra.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			/*skrytí menu*/
			transition.setRate(transition.getRate() * -1);
			transition.play();
			drawer.toggle();
			/*zobrazení v HTML*/
			Stage stage = new Stage();
			stage.setTitle("O hře");
			WebView webview = new WebView();
			webview.getEngine().load(Application.class.getResource("html/hra.html").toExternalForm());
			stage.setScene(new Scene(webview, 720, 480));
			stage.setMinWidth(720);
			stage.setMinHeight(480);
			stage.setMaxWidth(720);
			stage.setMaxHeight(480);
			stage.show();
		});

		NovaHra.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			try {
				NoveOkno();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		KonecHry.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			Stage stage = (Stage) KonecHry.getScene().getWindow();
			stage.close();
		});
	}

	public void NoveOkno() throws Exception {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Home.fxml"));
		Parent root = loader.load();
		HomeController controller = loader.getController();
		controller.inicializuj(new Hra());
		stage.setTitle("Adventura");
		stage.setMinWidth(300);
		stage.setMinHeight(300);
		stage.setScene(new Scene(root));
		Stage stageold = (Stage) KonecHry.getScene().getWindow();
		stage.show();
		stageold.close();
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