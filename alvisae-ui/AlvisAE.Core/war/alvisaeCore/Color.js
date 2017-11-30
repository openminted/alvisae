/**
 * Author: Lachlan Hunt
 * Date: 2005-11-24
 * Version: 1.0
 *
 * Licence: Public Domain
 * Attribution is considered ethical, but not required.
 *
 * Usage:
 *   Color(255, 255, 255);
 *   Color(255, 255, 255, 1.0);
 *   Color("#FFF");
 *   Color("#FFFFFF");
 *   Color("rgb(255, 255, 255)");
 *   Color("rgba(255, 255, 255, 1.0)");
 *   Color("white"); - CSS 2.1 Color keywords only
 */
var Color = function() {

	var keyword = new Array(); // CSS 2.1 Colour Keywords
	keyword["maroon"]  = "#800000"
	keyword["red"]     = "#ff0000"
	keyword["orange"]  = "#ffA500"
	keyword["yellow"]  = "#ffff00"
	keyword["olive"]   = "#808000"
	keyword["purple"]  = "#800080"
	keyword["fuchsia"] = "#ff00ff"
	keyword["white"]   = "#ffffff"
	keyword["lime"]    = "#00ff00"
	keyword["green"]   = "#008000"
	keyword["navy"]    = "#000080"
	keyword["blue"]    = "#0000ff"
	keyword["aqua"]    = "#00ffff"
	keyword["teal"]    = "#008080"
	keyword["black"]   = "#000000"
	keyword["silver"]  = "#c0c0c0"
	keyword["gray"]    = "#808080"

	var func = new Array(); // CSS Functional Notations and Hex Patterns
	func["rgb"]   = /^rgb\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*\);?$/;
	func["rgb%"]  = /^rgb\(\s*(\d{1,3})%\s*,\s*(\d{1,3})%\s*,\s*(\d{1,3})%\s*\);?$/;
	func["rgba"]  = /^rgba\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*((?:\d+(?:\.\d+)?)|(?:\.\d+))\s*\);?$/;
	func["rgba%"] = /^rgba\(\s*(\d{1,3})%\s*,\s*(\d{1,3})%\s*,\s*(\d{1,3})%\s*,\s*((?:\d+(?:\.\d+)?)|(?:\.\d+))\s*\);?$/;
	func["hex3"]  = /^#([0-9A-Fa-f])([0-9A-Fa-f])([0-9A-Fa-f]);?$/;
	func["hex6"]  = /^#([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2});?$/;

	/*
	 * Clamp the value between the low value and the high value
	 */
	function clamp(value, low, high) {
		if (value < low) {
			value = low;
		}
		else if (value > high) {
			value = high;
		}
		return value;
	}

	function alphaBlend(forground, background, alpha) {
		return Math.round(background * (1.0 - alpha) + forground * (alpha));
	}

	/*
	 * Return the colour in hexadecimal notation: #RRGGBB. e.g. #FF9933
	 * @param bg - Optional parameter used for calculating the colour if an alpha value less than 1.0 has been specified.
	 *             If not specified, the alpha value will be ignored.
	 */
	function hex(bg) {
		if (bg) {
			var r = alphaBlend(this.red, bg.red, this.alpha);
			var g = alphaBlend(this.green, bg.green, this.alpha);
			var b = alphaBlend(this.blue, bg.blue, this.alpha);
		} else {
			r = this.red;
			g = this.green;
			b = this.blue;
		}
		
		var strHexR = r.toString(16).toUpperCase();
		var strHexG = g.toString(16).toUpperCase();
		var strHexB = b.toString(16).toUpperCase();
	
		if (strHexR.length < 2) strHexR = "0" + strHexR;
		if (strHexG.length < 2) strHexG = "0" + strHexG;
		if (strHexB.length < 2) strHexB = "0" + strHexB;
	
		return "#" + strHexR + strHexG + strHexB
	}

	/*
	 * Return the colour in CSS rgb() functional notation, using integers 0-255: rgb(255, 255 255);
	 * @param bg - Optional parameter used for calculating the colour if an alpha value less than 1.0 has been specified.
	 *             If not specified, the alpha value will be ignored.
	 */
	function rgb(bg) {
		if (bg) {
			var r = alphaBlend(this.red, bg.red, this.alpha);
			var g = alphaBlend(this.green, bg.green, this.alpha);
			var b = alphaBlend(this.blue, bg.blue, this.alpha);
		} else {
			r = this.red;
			g = this.green;
			b = this.blue;
		}
	
		return "rgb(" + r + ", " + g + ", " + b + ")";
	}

	/*
	 * Return the colour in CSS rgba() functional notation, using integers 0-255 for color components: rgb(255, 255 255, 1.0);
	 * @param bg - Optional parameter used for calculating the colour if an alpha value less than 1.0 has been specified.
	 *             If not specified, and there is an alpha value, black will be used as the background colour.
	 */
	function rgba() {
		return "rgba(" + this.red + ", " + this.green + ", " + this.blue + ", " + this.alpha + ")";
	}

	/*
	 * Blend this colour with the colour specified and return a pallet with all the steps in between.
	 * @param color - The colour to blend with
	 * @param steps - The number of steps to take to reach the color.
	 */
	function blend(color, steps) {
		var pallet = new Array();
		var r, g, b, i;

		var step = new Object();
		step.red   = (alphaBlend(color.red, this.red, color.alpha) - this.red) / steps;
		step.green = (alphaBlend(color.green, this.green, color.alpha) - this.green) / steps;
		step.blue  = (alphaBlend(color.blue,  this.blue,  color.alpha) - this.blue) / steps;
		for (i = 0; i < steps + 1; i++) {
			r = Math.round(this.red   + (step.red * i));
			g = Math.round(this.green + (step.green * i));
			b = Math.round(this.blue  + (step.blue * i));
			pallet.push(new Color(r, g, b));
		}
		return pallet;
	}

	/*
	 * Constructor function
	 */
	return function() {
		this.toString = this.hex = hex;
		this.rgb = rgb;
		this.rgba = rgba;
		this.blend = blend

		if (arguments.length >= 3) {
			/* r, g, b or r, g, b, a */
			var r = arguments[0];
			var g = arguments[1];
			var b = arguments[2];
			var a = arguments[3];
	
			this.red   = (!isNaN(r)) ? clamp(r, 0, 255) : 0;
			this.green = (!isNaN(g)) ? clamp(g, 0, 255) : 0;
			this.blue  = (!isNaN(b)) ? clamp(b, 0, 255) : 0;
			this.alpha = (!isNaN(a)) ? clamp(a, 0.0, 1.0) : 1.0;
		} else if (arguments.length == 1) {
			/* CSS Colour keyword or value */
			var value = keyword[arguments[0]] ? keyword[arguments[0]] : arguments[0];
			var components, pattern;

			for (var key in func) {
				if (func[key].test(value)) {
					pattern = key;
				}
			}
	
			components = value.match(func[pattern]);
			var base = 10;
			var m = 1; // Multiplier for percentage values
	
			switch (pattern) {
			case "rgb%":
			case "rgba%":
				m = 2.55;
			case "rgb":
			case "rgba":
				base = 10;
				break;
			case "hex3":
				components[1] = components[1] + "" + components[1]
				components[2] = components[2] + "" + components[2]
				components[3] = components[3] + "" + components[3]
			case "hex6":
				base = 16;
				break;
			default:
				components = new Array(0, "255", "255", "255", "1.0");
			}

			this.red   = clamp(Math.round(parseInt(components[1],base) * m), 0, 255);
			this.green = clamp(Math.round(parseInt(components[2],base) * m), 0, 255);
			this.blue  = clamp(Math.round(parseInt(components[3],base) * m), 0, 255);

			if (isNaN(components[4])) {
				this.alpha = 1;
			} else {
				this.alpha = clamp(parseFloat("0" + components[4]), 0.0, 1.0);
			}
		}
	}
}();