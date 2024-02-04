import java.awt.*;
import java.util.*;
import hsa.Console;
import java.awt.image.*;

class tile
{ //Object for tiles
    String value;
    Color tileColor;
    Color textColor;
    int textSize;
    int textX;
    int textY;
    tile (String value, Color tileColor, Color textColor)  //constructor
    {
	//assigning values
	this.value = value;
	this.tileColor = tileColor;
	this.textColor = textColor;
	//if statements to check the number of digits for value and adjust the position of the number
	switch (value.length ())
	{
	    case 1:
		textSize = 80;
		textX = 50;
		textY = 100;
		break;
	    case 2:
		textSize = 75;
		textX = 28;
		textY = 100;
		break;
	    case 3:
		textSize = 65;
		textX = 15;
		textY = 95;
		break;
	    case 4:
		textSize = 55;
		textX = 7;
		textY = 95;
		break;
	    case 5:
		textSize = 45;
		textX = 7;
		textY = 93;
	}
    }
}
public class code2048
{
    static Console c = new Console (40, 87, "2048"); //main console called "2048"
    static Console d = new Console ("debug"); //debug console (used for debug purposes)
    static int[] [] grid = new int [4] [4]; //the grid (currently empty)
    static int[] [] pregrid = new int [4] [4]; //previous grid -- used in undo moves
    static int[] xCord = {40, 200, 360, 520}; //storing the x-coordinate for the grids
    static int[] yCord = {120, 280, 440, 600}; //storing the y-coordinate for the grids
    static int xTileCord;
    static int yTileCord;
    static char input; //stores character input
    static int score = 0; //current score
    static int prescore = 0; //previous score -- used in undo moves
    static int highscore = 0; //highscore
    static int prehighscore = 0; //previous highscore -- used in undo moves
    static String plusText = ""; //used for the adding of score
    static boolean check; //
    static boolean gameover = false;
    static boolean undo = false;
    static boolean checkundo = false;
    //secret valuables (Easter Egg!)
    static boolean troll = false;
    static boolean backwards = false;
    /*COLOURS*/
    static Color outline = new Color (187, 173, 160); //Colour for the outline
    static Color empty = new Color (205, 193, 180); // Colour for empty spaces
    static Color newgame = new Color (143, 122, 102); //Colour for the "new game" button
    static Color stc = new Color (119, 110, 101); // Text colour from 2 and 4 (Start text color)
    static Color etc = new Color (248, 232, 218); // Text colour from 8 onwards (End text color)
    static Color normaletc = new Color (248, 232, 218); // Text colour from 8 onwards (Normal mode)
    static Color christmasetc = new Color (42, 100, 42); // Text colour from 128 onwards (Christmas mode)
    static Color darketc = new Color (7, 23, 37); // Text colour from 8 onwards (Dark mode)
    static Color scorec = new Color (238, 228, 218); //Text colour for the scoreboard
    static Color background = new Color (255, 255, 255); //Colour for background
    static boolean check2048 = false;
    static Color homeTextColor = new Color (0, 0, 0);
    //colours for the tiles
    static Color color2 = new Color (238, 228, 218); //Colour for 2
    static Color color4 = new Color (237, 224, 200); //Colour for 4
    static Color color8 = new Color (242, 177, 121); //Colour for 8
    static Color color16 = new Color (245, 149, 99); //Colour for 16
    static Color color32 = new Color (246, 124, 95); //Colour for 32
    static Color color64 = new Color (246, 94, 59); //Colour for 64
    static Color color128 = new Color (237, 207, 114); //Colour for 128
    static Color color256 = new Color (237, 204, 97); //Colour for 256
    static Color color512 = new Color (237, 200, 80); //Colour for 512
    static Color color1024 = new Color (237, 197, 63); //Colour for 1024
    static Color color2048 = new Color (237, 194, 46); //Colour for 2048
    static Color color4096 = new Color (60, 58, 50); //Colour for 4096 and up
    static Color normalcolor2048 = new Color (237, 194, 46); //Colour for 2048 (Normal mode)
    static Color christmascolor2048 = new Color (144, 255, 144); //Colour for 2048 (Christmas mode)
    static Color darkcolor2048 = new Color (18, 61, 209); //Colour for 2048 (Dark mode)

    //tile objects of all tiles in the game
    static tile emptyTile = new tile ("", empty, stc);
    static tile tile2 = new tile ("2", color2, stc);
    static tile tile4 = new tile ("4", color4, stc);
    static tile tile8 = new tile ("8", color8, etc);
    static tile tile16 = new tile ("16", color16, etc);
    static tile tile32 = new tile ("32", color32, etc);
    static tile tile64 = new tile ("64", color64, etc);
    static tile tile128 = new tile ("128", color128, etc);
    static tile tile256 = new tile ("256", color256, etc);
    static tile tile512 = new tile ("512", color512, etc);
    static tile tile1024 = new tile ("1024", color1024, etc);
    static tile tile2048 = new tile ("2048", color2048, etc);
    static tile tile4096 = new tile ("4096", color4096, etc);
    static tile tile8192 = new tile ("8192", color4096, etc);
    static tile tile16384 = new tile ("16384", color4096, etc);
    static tile tile32768 = new tile ("32768", color4096, etc);
    static tile tile65536 = new tile ("65536", color4096, etc);
    //array of all the tiles: used to save code
    static tile[] tiles = {emptyTile, tile2, tile4, tile8, tile16, tile32, tile64, tile128, tile256, tile512, tile1024, tile2048, tile4096, tile8192, tile16384, tile32768, tile65536};



    public static void game () throws Exception //the main game
    {
	int count2048 = 0; //used to prevent spamming "You Win" when there is a 2048 on the grid
	score = 0; //reset score
	//generates two tiles
	grid = new int[] []
	{
	    {
		0, 0, 0, 0
	    }
	    ,
	    {
		0, 0, 0, 0
	    }
	    ,
	    {
		0, 0, 0, 0
	    }
	    ,
	    {
		0, 0, 0, 0
	    }
	}
	; //reset the grid
	pregrid = new int [4] [4];
	gameover = false;
	//generates two tiles at the beginning of the game
	drawGrid ();
	drawGrid ();
	while (true)
	{
	    debug (); //prints the debug stuff in the debug console

	    input (); //check the input
	    if ((input == 'w' || input == 'a' || input == 's' || input == 'd' || input == 'u') && check) //updates the grid and generates a new tile if key pressed is WASD
		drawGrid ();
	    gameover = GameOver (); //check gameover
	    if (gameover == true)
	    {
		gameoverscreen ();
	    }
	    check2048 = Check2048 ();
	    if (check2048 == true)
	    {
		count2048 = count2048 + 1;
	    }
	    if (check2048 == true && count2048 == 1)
	    {
		winscreen (); //win condition: if 2048 is on the grid
	    }
	}
    }


    public static void winscreen () throws Exception //the win screen
    {
	c.setColor (Color.black);
	c.setFont (new Font ("ClearSans", Font.BOLD, 65));
	c.drawString ("YOU GOT 2048!!", 75, 325);
	c.setFont (new Font ("ClearSans", Font.BOLD, 45));
	c.drawString ("Keep Playing To Get 4096!!", 50, 380);


    }


    public static void gameoverscreen () throws Exception //the gameover screen
    {
	c.setColor (Color.black);
	c.fillRect (110, 350, 480, 100);

	c.setColor (Color.red);
	c.setFont (new Font ("ClearSans", Font.BOLD, 75));
	c.drawString ("GAME OVER", 110, 425);
	input = c.getChar ();
	while (true)
	{
	    if (input == 'r')
	    {
		c.clear ();
		mainmenu ();
	    }
	    input = c.getChar ();
	}
    }


    public static void scoreBoard ()  //draws everything in the scoreboard
    {
	highscore = Math.max (highscore, score);
	c.setColor (scorec);
	c.setFont (new Font ("ClearSans", Font.BOLD, 18));
	c.drawString ("SCORE", 400, 55);
	c.setFont (new Font ("ClearSans", Font.BOLD, 18));
	c.drawString ("BEST", 565, 55);
	c.setColor (Color.white);
	c.setFont (new Font ("ClearSans", Font.BOLD, 35));

	String temp = score + "";
	switch (temp.length ())
	{
	    case 1:
		c.drawString ("" + score, 423, 90);
		break;
	    case 2:
		c.drawString ("" + score, 412, 90);
		break;
	    case 3:
		c.setFont (new Font ("ClearSans", Font.BOLD, 33));
		c.drawString ("" + score, 405, 90);
		break;
	    case 4:
		c.setFont (new Font ("ClearSans", Font.BOLD, 33));
		c.drawString ("" + score, 395, 90);
		break;
	    case 5:
		c.setFont (new Font ("ClearSans", Font.BOLD, 30));
		c.drawString ("" + score, 388, 90);
		break;
	    case 6:
		c.setFont (new Font ("ClearSans", Font.BOLD, 30));
		c.drawString ("" + score, 379, 90);
	}
	String temp2 = highscore + "";
	switch (temp2.length ())
	{
	    case 1:
		c.drawString ("" + highscore, 580, 90);
		break;
	    case 2:
		c.drawString ("" + highscore, 569, 90);
		break;
	    case 3:
		c.setFont (new Font ("ClearSans", Font.BOLD, 33));
		c.drawString ("" + highscore, 562, 90);
		break;
	    case 4:
		c.setFont (new Font ("ClearSans", Font.BOLD, 33));
		c.drawString ("" + highscore, 552, 90);
		break;
	    case 5:
		c.setFont (new Font ("ClearSans", Font.BOLD, 30));
		c.drawString ("" + highscore, 545, 90);
		break;
	    case 6:
		c.setFont (new Font ("ClearSans", Font.BOLD, 30));
		c.drawString ("" + highscore, 536, 90);


	}




	c.setFont (new Font ("ClearSans", Font.BOLD, 30));
	c.drawString ("New Game (r)", 36, 75);
	c.setColor (scorec);
	c.setFont (new Font ("ClearSans", Font.BOLD, 30));
	c.drawString (plusText, 380, 90);
	if (undo)
	{
	    c.setColor (homeTextColor);
	    c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	    c.drawString ("Press U to undo moves", 30, 800);
	}
    }



    public static void debug ()  //debug function, prints everything used for debug
    {
	for (int i = 0 ; i < grid.length ; i++)
	{ //prints the grid
	    for (int j = 0 ; j < grid.length ; j++)
	    {
		d.print (grid [i] [j] + " ");
	    }
	    d.println ();
	}


	d.println ("Gameover = " + GameOver ());
	d.println ("2048 = " + Check2048 ());
    }


    public static void input () throws Exception //input function, check the player's input and runs the respective input
    {
	input = c.getChar ();
	checkundo = false;
	switch (input) //rotates the grid with respective inputs and rotate it back
	{
	    case 'w':
		setPreGrid ();
		grid = rotate270 ();
		check = moveleft ();
		grid = rotate90 ();
		break;
	    case 's':
		setPreGrid ();
		grid = rotate90 ();
		check = moveleft ();
		grid = rotate270 ();
		break;
	    case 'a':
		setPreGrid ();
		check = moveleft ();
		break;
	    case 'd':
		setPreGrid ();
		grid = rotate90 ();
		grid = rotate90 ();
		check = moveleft ();
		grid = rotate90 ();
		grid = rotate90 ();
		break;
	    case 'r':
		c.clear ();
		mainmenu ();
		break;
	    case 'u':
		if (undo)
		{
		    score = prescore;
		    highscore = prehighscore;
		    for (int i = 0 ; i < grid.length ; i++)
		    {
			for (int j = 0 ; j < grid.length ; j++)
			{
			    grid [i] [j] = pregrid [i] [j];
			}
		    }
		    checkundo = true;
		}
		else
		    check = false;
	}
    }


    public static void mainmenu () throws Exception //the main menu
    {



	mainmenugraphic ();
	do
	{
	    input = c.getChar ();
	    switch (input)
	    {
		case '1':
		    c.clear ();
		    game ();
		    break;
		case '2':
		    instructions ();
		    break;
		case '3':
		    settings ();
		    break;
		case '4':
		    c.close ();
		    d.close ();
	    }
	}


	while (input != '1' && input != '2' && input != '3' && input != '4' && input != '5');
    }


    public static void mainmenugraphic ()  //draws out the main menu
    {


	c.setColor (background);
	c.fillRect (0, 0, 66669, 66669);
	c.setColor (color2048);
	c.fillRoundRect (250, 75, 200, 200, 30, 30);
	c.setColor (etc);
	c.setFont (new Font ("ClearSans", Font.BOLD, 70));
	c.drawString ("2048", 266, 196);
	c.setColor (homeTextColor);
	c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	c.drawString ("(1) PLAY", 180, 350);
	c.drawString ("(2) INSTRUCTIONS", 180, 400);
	c.drawString ("(3) SETTINGS", 180, 450);
	c.drawString ("(4) EXIT", 180, 500);
    }


    public static void instructions () throws Exception //draws out the instructions screen
    {
	c.clear ();
	c.setColor (background);
	c.fillRect (0, 0, 66669, 66669);
	c.setColor (homeTextColor);
	c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	c.drawString ("HOW TO PLAY", 0, 50);
	c.setFont (new Font ("ClearSans", Font.BOLD, 20));
	c.drawString ("The aim of the game is to combine tiles to get the 2048 tile:", 0, 100);
	c.setColor (color2048);
	c.fillRect (10, 125, 100, 100);
	c.setColor (etc);
	c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	c.drawString ("2048", 12, 190);
	c.setColor (homeTextColor);
	c.setFont (new Font ("ClearSans", Font.BOLD, 20));
	c.drawString ("Use W A S D to combine tiles into their respective direction.", 0, 280);
	c.drawString ("You start out with two tiles which can be combined and everytime", 0, 350);
	c.drawString ("a move is made a new tile appears on the board.", 0, 370);
	c.drawString ("Only tiles with the same value can be combined.", 0, 440);
	c.drawString ("Keep combining tiles until you get the 2048 tile.", 0, 510);
	c.drawString ("If the board is full and there is no possible moves left then GAME OVER", 0, 580);
	c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	c.drawString ("(1) Back", 0, 660);
	while (true)
	{
	    input = c.getChar ();
	    if (input == '1')
	    {
		c.clear ();
		mainmenu ();
	    }
	}
    }



    public static void settings () throws Exception //draws out the settings screen and includes theme selection
    {
	c.clear ();
	c.setColor (background);
	c.fillRect (0, 0, 66669, 66669);
	c.setColor (homeTextColor);


	c.drawString ("(2) Dark Mode", 250, 180);
	c.setColor (darkcolor2048);
	c.fillRoundRect (108, 111, 100, 100, 30, 30);
	c.setColor (darketc);
	c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	c.drawString ("2048", 110, 176);
	c.setColor (homeTextColor);
	c.drawString ("(3) Chirstmas", 250, 280);
	c.setColor (christmascolor2048);
	c.fillRoundRect (108, 215, 100, 100, 30, 30);
	c.setColor (christmasetc);
	c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	c.drawString ("2048", 110, 280);
	c.setColor (homeTextColor);
	c.drawString ("(r) Normal", 250, 380);
	c.setColor (normalcolor2048);
	c.fillRoundRect (108, 319, 100, 100, 30, 30);
	c.setColor (normaletc);
	c.setFont (new Font ("ClearSans", Font.BOLD, 40));
	c.drawString ("2048", 110, 384);
	c.setColor (homeTextColor);

	c.drawString ("(u) Undo moves mode ", 250, 480);
	c.drawString ("(1) Back ", 250, 580);
	boolean c128 = false; //used for text colour problems for christmas theme
	input = c.getChar ();
	if (input == '1') //leave settings screen
	{
	    c.clear ();
	    mainmenu ();
	}


	else if (input == '2') //dark theme
	{
	    outline = new Color (68, 82, 95);
	    empty = new Color (50, 62, 75);
	    newgame = new Color (112, 133, 153);
	    stc = new Color (136, 145, 154);
	    etc = new Color (7, 23, 37);
	    scorec = new Color (17, 27, 37);
	    background = new Color (0, 0, 0);
	    homeTextColor = new Color (255, 255, 255);
	    color2 = new Color (17, 27, 37);
	    color4 = new Color (18, 31, 55);
	    color8 = new Color (13, 78, 134);
	    color16 = new Color (10, 106, 156);
	    color32 = new Color (9, 131, 160);
	    color64 = new Color (9, 161, 196);
	    color128 = new Color (18, 48, 141);
	    color256 = new Color (18, 51, 158);
	    color512 = new Color (18, 55, 175);
	    color1024 = new Color (18, 58, 192);
	    color2048 = new Color (18, 61, 209);
	    color4096 = new Color (195, 197, 205);
	}


	else if (input == '3') //christmas theme
	{
	    outline = new Color (0, 255, 255);
	    empty = new Color (155, 255, 255);
	    newgame = new Color (218, 165, 32);
	    stc = new Color (255, 100, 100);
	    etc = new Color (42, 100, 42);
	    scorec = new Color (17, 27, 37);
	    background = new Color (255, 0, 0);
	    homeTextColor = new Color (0, 255, 0);
	    color2 = new Color (255, 235, 235);
	    color4 = new Color (255, 220, 220);
	    color8 = new Color (255, 200, 200);
	    color16 = new Color (255, 180, 180);
	    color32 = new Color (255, 160, 160);
	    color64 = new Color (255, 120, 120);
	    color128 = new Color (225, 255, 225);
	    color256 = new Color (200, 255, 200);
	    color512 = new Color (180, 255, 180);
	    color1024 = new Color (160, 255, 160);
	    color2048 = new Color (144, 255, 144);
	    color4096 = new Color (66, 255, 66);
	    c128 = true;
	}


	else if (input == 'r') //normal theme
	{
	    color2 = new Color (238, 228, 218);
	    color4 = new Color (237, 224, 200);
	    color8 = new Color (242, 177, 121);
	    color16 = new Color (245, 149, 99);
	    color32 = new Color (246, 124, 95);
	    color64 = new Color (246, 94, 59);
	    color128 = new Color (237, 207, 114);
	    color256 = new Color (237, 204, 97);
	    color512 = new Color (237, 200, 80);
	    color1024 = new Color (237, 197, 63);
	    color2048 = new Color (237, 194, 46);
	    color4096 = new Color (60, 58, 50);
	    outline = new Color (187, 173, 160);
	    empty = new Color (205, 193, 180);
	    newgame = new Color (143, 122, 102);
	    stc = new Color (119, 110, 101);
	    etc = new Color (248, 232, 218);
	    scorec = new Color (238, 228, 218);
	    background = new Color (255, 255, 255);
	    homeTextColor = new Color (0, 0, 0);
	}


	else if (input == 't')
	{
	    if (troll)
		troll = false;
	    else
		troll = true;
	}


	else if (input == 'm')
	{
	    if (backwards)
		backwards = false;
	    else
		backwards = true;
	}
	else if (input == 'u')
	{
	    if (undo)
		undo = false;
	    else
		undo = true;
	}


	//resetting tiles and array
	if (c128)
	{
	    emptyTile = new tile ("", empty, stc);
	    tile2 = new tile ("2", color2, stc);
	    tile4 = new tile ("4", color4, stc);
	    tile8 = new tile ("8", color8, stc);
	    tile16 = new tile ("16", color16, stc);
	    tile32 = new tile ("32", color32, stc);
	    tile64 = new tile ("64", color64, stc);
	}


	else
	{

	    tile2 = new tile ("2", color2, stc);
	    tile4 = new tile ("4", color4, stc);
	    tile8 = new tile ("8", color8, etc);
	    tile16 = new tile ("16", color16, etc);
	    tile32 = new tile ("32", color32, etc);
	    tile64 = new tile ("64", color64, etc);
	    emptyTile = new tile ("", empty, stc);
	}


	tile128 = new tile ("128", color128, etc);
	tile256 = new tile ("256", color256, etc);
	tile512 = new tile ("512", color512, etc);
	tile1024 = new tile ("1024", color1024, etc);
	tile2048 = new tile ("2048", color2048, etc);
	tile4096 = new tile ("4096", color4096, etc);
	tile8192 = new tile ("8192", color4096, etc);
	tile16384 = new tile ("16384", color4096, etc);
	tile32768 = new tile ("32768", color4096, etc);
	tile65536 = new tile ("65536", color4096, etc);
	if (backwards)
	    tiles = new tile[]
	    {
		emptyTile, tile2048, tile1024, tile512, tile256, tile128, tile64, tile32, tile16, tile8, tile4, tile2, tile4096, tile8192, tile16384, tile32768, tile65536
	    }


	;
	else
	    tiles = new tile[]
	    {
		emptyTile, tile2, tile4, tile8, tile16, tile32, tile64, tile128, tile256, tile512, tile1024, tile2048, tile4096, tile8192, tile16384, tile32768, tile65536
	    }


	;
	//refresh the screen
	settings ();

    }


    public static boolean moveleft ()  //moves all tiles left and checks for merges
    {
	prescore = score;
	prehighscore = prehighscore;
	boolean move = false;
	int sum = 0;
	for (int i = 0 ; i < grid.length ; i++)
	{
	    boolean merged = false;
	    for (int j = 1 ; j < grid.length ; j++)
	    {
		int j1 = j;
		if (grid [i] [j] > 0)
		{
		    for (int k = 0 ; k < j ; k++)
		    {
			if (grid [i] [k] == 0)
			{
			    grid [i] [k] = grid [i] [j];
			    grid [i] [j] = 0;
			    j1 = k;
			    move = true;
			    break;
			}
		    }
		    if (merged)
		    {
			merged = false;
		    }
		    else
		    {
			if (j1 > 0 && grid [i] [j1 - 1] == grid [i] [j1])
			{
			    grid [i] [j1 - 1]++;
			    grid [i] [j1] = 0;
			    score += Math.pow (2, grid [i] [j1 - 1]);
			    sum += Math.pow (2, grid [i] [j1 - 1]);
			    move = true;
			    merged = true;
			}
		    }
		}
	    }
	}


	if (sum == 0)
	    plusText = "";
	else
	    plusText = "+" + sum;
	return move;
    }


    public static int[] [] rotate90 ()
    { //rotates the grid 90 degrees clockwise
	int grid1[] [] = new int [grid.length] [grid.length];
	int l = 0;
	for (int i = grid.length - 1 ; i >= 0 ; i--)
	{
	    for (int j = 0 ; j < grid.length ; j++)
	    {
		grid1 [l % grid.length] [l / grid.length] = grid [i] [j];
		l++;
	    }
	}


	return grid1;
    }


    public static int[] [] rotate270 ()
    { //rotates the grid 270 degrees clockwise or 90 degrees counterclockwise
	int grid1[] [] = new int [grid.length] [grid.length];
	int l = 0;
	for (int i = 0 ; i < grid.length ; i++)
	{
	    for (int j = grid.length - 1 ; j >= 0 ; j--)
	    {
		grid1 [l % grid.length] [l / grid.length] = grid [i] [j];
		l++;
	    }
	}


	return grid1;
    }


    public static void setPreGrid ()
    {
	for (int i = 0 ; i < grid.length ; i++)
	{
	    for (int j = 0 ; j < grid.length ; j++)
	    {
		pregrid [i] [j] = grid [i] [j];
	    }
	}
    }


    public static void printTile (tile k, int x, int y)  //prints the tile on coordinate x and y
    {
	c.setColor (k.tileColor);
	c.fillRoundRect (x, y, 150, 150, 30, 30);
	c.setColor (k.textColor);
	c.setFont (new Font ("ClearSans", Font.BOLD, k.textSize));
	c.drawString ("" + k.value, x + k.textX, y + k.textY);
    }


    public static void printNewTile (tile k, int x, int y) throws Exception //prints newly generated tile(the ones with the animation
    {
	c.setColor (k.tileColor);
	for (int i = 50, j = 50 ; i >= 0 ; i--, j += 2)
	{
	    c.fillRoundRect (x + i, y + i, j, j, 30, 30);
	    Thread.sleep (1);
	}


	c.setColor (k.textColor);
	c.setFont (new Font ("ClearSans", Font.BOLD, k.textSize));
	c.drawString ("" + k.value, x + k.textX, y + k.textY);
    }


    public static void drawGrid () throws Exception //draws the grid and generates a new tile in a empty spot
    {
	c.setColor (background);
	c.fillRect (0, 0, 66669, 66669);
	c.setColor (newgame);
	c.fillRoundRect (30, 30, 213, 70, 30, 30);
	c.setColor (outline);
	c.fillRoundRect (30, 110, 650, 650, 30, 30);
	c.fillRoundRect (520, 30, 150, 70, 30, 30);
	c.fillRoundRect (360, 30, 150, 70, 30, 30);
	c.setColor (empty);
	scoreBoard ();

	int r = randInt (0, 4);
	int e = randInt (0, 4);
	if (!checkundo)
	{
	    while (grid [r] [e] != 0) //finds an empty slot
	    {
		r = randInt (0, 4);
		e = randInt (0, 4);
	    }


	    d.println ("New Tile generated: " + r + " " + e); //debug
	    int chance = randInt (1, 11); //choosing of wether generating a 2 or 4
	    if (chance == 10)
	    {
		if (troll)
		    grid [r] [e] = 12;
		else
		    grid [r] [e] = 2;
	    }


	    else
	    {
		grid [r] [e] = 1;
	    }
	}

	for (int row = 0 ; row < grid.length ; row++) //prints the grid
	{
	    for (int coloum = 0 ; coloum < grid.length ; coloum++)
	    {
		if (row == r && coloum == e)
		{
		    continue;
		}
		else
		{
		    printTile (tiles [grid [row] [coloum]], xCord [coloum], yCord [row]);
		}
	    }
	}


	for (int row = 0 ; row < grid.length ; row++) //prints the newly generated tile
	{
	    for (int coloum = 0 ; coloum < grid.length ; coloum++)
	    {
		if (row == r && coloum == e)
		{
		    printNewTile (tiles [grid [row] [coloum]], xCord [coloum], yCord [row]);
		}
	    }
	}
    }


    public static boolean Check2048 ()  //returns true if there is a 2048 on the grid, false otherwise
    {
	for (int row = 0 ; row < grid.length ; row++)
	{
	    for (int coloum = 0 ; coloum < grid.length ; coloum++)
	    {
		if (grid [row] [coloum] == 11)
		{
		    return true;
		}
	    }
	}


	return false;
    }


    public static boolean GameOver ()  //returns true if there is no moves availible, false otherwise
    {
	for (int row1 = 0 ; row1 < grid.length ; row1++)
	{
	    for (int coloum1 = 0 ; coloum1 < grid.length ; coloum1++)
	    {
		if (grid [row1] [coloum1] == 0)
		{
		    return false;
		}
	    }
	}


	for (int row = 0 ; row < grid.length - 1 ; row++)
	{
	    for (int coloum = 0 ; coloum < grid.length ; coloum++)
	    {
		if (grid [row] [coloum] == grid [row + 1] [coloum])
		{
		    return false;
		}
	    }
	}


	for (int coloum = 0 ; coloum < grid.length - 1 ; coloum++)
	{
	    for (int row = 0 ; row < grid.length ; row++)
	    {
		if (grid [row] [coloum] == grid [row] [coloum + 1])
		{
		    return false;
		}
	    }
	}


	return true;
    }


    public static int randInt (int hi, int lo)  //generates a random number from the interval [lo,hi]
    {
	return (int) ((hi - lo) * Math.random () + lo);
    }


    public static void main (String[] args) throws Exception //main
    {
	mainmenu ();
    }
}
















