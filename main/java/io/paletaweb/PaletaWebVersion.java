package io.paletaweb;


public class PaletaWebVersion {

	public static final String VERSION = "0.1 beta";
	
	private static String[] brand_char = null;
	
	public static String[] getAppCharacterName() {
    
		if (brand_char != null)
            return brand_char;
		
        brand_char = new String[9];
        brand_char[0] = paleta[0] + "";
        brand_char[1] = paleta[1] + "";
        brand_char[2] = paleta[2] + "";
        brand_char[3] = paleta[3] + ""; 
        brand_char[4] = paleta[4] + "";
        brand_char[5] = paleta[5];
        brand_char[6] = "";
        brand_char[7] = "version: " + VERSION;
        brand_char[8] = "";
        return brand_char;
    }
    
    static final String paleta[] = {
	" ______    ______    _       ____   _____     ______    ", 
	"|  __  \\  |  __  |  | |     |  __| |_   _|   |  __  |   ",
	"| |  | |  | |  | |  | |     | |_     | |     | |  | |   ",
	"| |___ /  | |__| |  | |     |  _|    | |     | |__| |   ",
	"| |       |  __  |  | |___  | |__    | |     |  __  |   ",
	"|_|       |_|  |_|  |_____| |____|   |_|     |_|  |_|  WEB  " };


    
}
