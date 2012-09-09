QUnit.begin = function() {
	console.log("Running Test Suite");
};


testNumericInputFields("Panel Length", "panelLength")
testNumericInputFields("Panel Width", "panelWidth")
testNumericInputFields("Panel Quantity", "panelQty")
testNumericInputFields("Panel Efficiency", "panelEfficiency")
testNumericInputFields("Inverter Efficiency", "inverterEfficiency")
//testNumericInputFields("Angle", "angle")
//testNumericInputFields("Sunlight", "sunlight")
testNumericInputFields("Consumption", "consumption")
//testAlphaNumericInputFields("Address", "address")


module("All input fields valid");
test("All input fields valid", function() {
	setupValidInputs();
	
	ok(validForm(), "All input fields are valid");
});


module("Panel Size Calculation");
test("Panel size is calculated correctly", function() {
	setupValidInputs();
	calculatePanelSize();
	
	equal(panelSize, 13.099656, "Panel fields calculated correctly");
});


module("Valid ajax response");
test("All input data submitted as valid parameters", function() {
	setupValidInputs();
	calculatePanelSize();
	
	var input = "panelSize=" + panelSize + "&panelEfficiency=" + panelEfficiency + "&inverterEfficiency=" + inverterEfficiency +
	   "&orientation=" + orientation + "&angle=" + angle + "&sunlight=" + sunlight + "&consumption=" + consumption + "&address=" + address + "&tariff=" + tariff;
	
	var options = null;
	$.ajax = function(param) {
		options = param;
	};
	calculateInput();
	options.success();
	equal(options.data, input);
});

/*
testAlphaNumericInputFields("New Panel Manufacturer", "manufacturer");
testAlphaNumericInputFields("New Panel Model", "model");
testNumericInputFields("New Panel Power", "power")
testNumericInputFields("New Panel Length", "newPanelLength")
testNumericInputFields("New Panel Width", "newPanelWidth")
*/

/*
test("Valid calculation response received", function() {
	setupValidInputs();
	calculatePanelSize();
	
	var input = "panelSize=" + panelSize + "&panelEfficiency=" + panelEfficiency + "&inverterEfficiency=" + inverterEfficiency +
	   "&orientation=" + orientation + "&angle=" + angle + "&sunlight=" + sunlight + "&consumption=" + consumption + "&address=" + address + "&tariff=" + tariff;
	
	var options = null;
	$.ajax = function(param) {
		options = param;
	};
	calculateInput();
	options.success();
	equal($("#lblSavings").html(), "Based on your input, the annual savings will be <strong>$100.16</strong>");
});
*/


function testNumericInputFields(fieldName, txtFieldName) {
	module(fieldName + " input fields");
	test(fieldName + " is undefined", function() {
		ok(invalidNumberField(txtFieldName), fieldName + " is undefined");
	});

	test(fieldName + " is empty", function() {
		txtFieldName = "";
		ok(invalidNumberField(txtFieldName), fieldName + " is empty");
	});

	test(fieldName + " is NaN", function() {
		txtFieldName = "a";
		ok(invalidNumberField(txtFieldName), fieldName + "  is not a number");
	});

	test(fieldName + " is less than 0", function() {
		txtFieldName = "-1";
		ok(invalidNumberField(txtFieldName), fieldName + "  is less than 0");
	});
	
	test(fieldName + " is valid", function() {
		txtFieldName = "5";
		ok(!invalidNumberField(txtFieldName), fieldName + "  is valid");
	});
}


function testAlphaNumericInputFields(fieldName, txtFieldName) {
	module(fieldName + " input fields");
	test(fieldName + " is undefined", function() {
		ok(!invalidAlphaNumericField(txtFieldName), fieldName + " is undefined");
	});

	test(fieldName + " is empty", function() {
		txtFieldName = "";
		ok(invalidAlphaNumericField(txtFieldName), fieldName + " is empty");
	});

	test(fieldName + " is valid", function() {
		txtFieldName = "ABC12";
		ok(!invalidAlphaNumericField(txtFieldName), fieldName + "  is valid");
	});
}


function setupValidInputs() {
	panelLength = 1649;
	panelWidth = 993;
	panelQty = 8;
	panelEfficiency = 5;
	inverterEfficiency = 5;
	address = "1 Test Street Brisbane Queensland";
	orientation = "N";
	angle = 5;
	sunlight = 5;
	consumption = 5;
	tariff = 0.44;
	manufacturer = "Test manufacturer";
	model = "Test model"; 
	power = 200;
	newPanelLength = 1000;
	newPanelWidth = 400;
}

function clearInputFields() {
	panelLength = "";
	panelWidth = "";
	panelQty = "";
	panelEfficiency = "";
	inverterEfficiency = "";
	address = "";
	orientation = "";
	angle = "";
	sunlight = "";
	consumption = "";
	tariff = "";
	manufacturer = "";
	model = ""; 
	power = "";
	newPanelLength = "";
	newPanelWidth = "";
}
	
