package unitTests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import components.Inverter;
import components.Panel;

import exceptions.*;

import solar.Calculator;
import solar.TariffCalculation;

public class TariffTests {

	private Panel panel;
	private final String PMANUFACTURER = "ABC";
	private final String PMODEL = "CBA";
	private final int PANELRATING = 500;

	private Inverter inverter;
	private final String IMANUFACTURER = "DEF";
	private final String IMODEL = "FED";
	private final int INVERTOREFFICIENCY = 100;

	private Calculator calculator;
	private final int CONSUMPTION = 1;
	private final int PANELANGLE = 45;

	private TariffCalculation tariff;
	private final float TARIFFVALUE = 5;

	@Before
	@Test
	public void initialise() throws CalculatorException, TariffException, PanelException, InverterException {
		panel = new Panel(PMANUFACTURER, PMODEL, PANELRATING);
		inverter = new Inverter(IMANUFACTURER, IMODEL, INVERTOREFFICIENCY);
		calculator = new Calculator(panel, inverter, 1, CONSUMPTION, PANELANGLE, 0, 0);
		tariff = new TariffCalculation(calculator, TARIFFVALUE);
	}

	@Test
	public void testCalAnnualElectric() throws TariffException {
		assertEquals(tariff.calAnnualElectric(), 172.18223571777344, 2);
	}

	@Test
	public void testCalAnnualSaving() throws TariffException {
		assertEquals(tariff.calAnnualSaving(), 860.911193847562, 2);
	}
}