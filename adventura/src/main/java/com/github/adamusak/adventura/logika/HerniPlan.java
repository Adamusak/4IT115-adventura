package com.github.adamusak.adventura.logika;

import java.util.Observable;

/**
 * Class HerniPlan - třída představující mapu a stav adventury.
 * 
 * Tato třída inicializuje prvky ze kterých se hra skládá: vytváří všechny
 * prostory, propojuje je vzájemně pomocí východů a pamatuje si aktuální
 * prostor, ve kterém se hráč právě nachází.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova, Alena
 *         Buchalcevova, Adam Novak
 * @version z kurzu 4IT101 pro školní rok 2014/2015
 */
public class HerniPlan extends Observable {

	private Prostor aktualniProstor;
	private Prostor viteznyProstor;

	/**
	 * Konstruktor který vytváří jednotlivé prostory a propojuje je pomocí východů.
	 * Jako výchozí aktuální prostor nastaví halu.
	 */
	public HerniPlan() {
		zalozProstoryHry();

	}

	/**
	 * Vytváří jednotlivé prostory a propojuje je pomocí východů. Jako výchozí
	 * aktuální prostor nastaví domeček.
	 */
	private void zalozProstoryHry() {
		// vytvářejí se jednotlivé prostory
		Prostor domecek = new Prostor("domeček", "domeček, ve kterém bydlí Karkulka", 10, 10);
		Prostor chaloupka = new Prostor("chaloupka", "chaloupka, ve které bydlí babička Karkulky", 15, 15);
		Prostor jeskyne = new Prostor("jeskyně", "stará plesnivá jeskyně", 20, 20);
		Prostor les = new Prostor("les", "les s jahodami, malinami a pramenem vody", 25, 25);
		Prostor hlubokyLes = new Prostor("hluboký_les", "temný les, ve kterém lze potkat vlka", 30, 30);

		// přiřazují se průchody mezi prostory (sousedící prostory)
		domecek.setVychod(les);
		les.setVychod(domecek);
		les.setVychod(hlubokyLes);
		hlubokyLes.setVychod(les);
		hlubokyLes.setVychod(jeskyne);
		hlubokyLes.setVychod(chaloupka);
		jeskyne.setVychod(hlubokyLes);
		chaloupka.setVychod(hlubokyLes);

		aktualniProstor = domecek; // hra začíná v domečku
		viteznyProstor = chaloupka;
		les.vlozVec(new Vec("maliny", true)); // parametry (název, přenositelnost¨)
		les.vlozVec(new Vec("strom", false));
	}

	/**
	 * Metoda vrací odkaz na aktuální prostor, ve kterém se hráč právě nachází.
	 *
	 * @return aktuální prostor
	 */

	public Prostor getAktualniProstor() {
		return aktualniProstor;
	}

	/**
	 * Metoda nastaví aktuální prostor, používá se nejčastěji při přechodu mezi
	 * prostory
	 *
	 * @param prostor
	 *            nový aktuální prostor
	 */
	public void setAktualniProstor(Prostor prostor) {
		aktualniProstor = prostor;
		setChanged();
		notifyObservers();
	}

	/**
	 * Metoda vrací odkaz na vítězný prostor.
	 *
	 * @return vítězný prostor
	 */

	public Prostor getViteznyProstor() {
		return viteznyProstor;
	}

}
