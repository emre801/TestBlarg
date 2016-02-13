package com.johnerdo.imageCompare;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import org.johnerdo.globalInfo.PokemonList;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class MatchingMethod {
	
	public static String screenInfoScreenShot = "C:/Users/John/Desktop/ScreenInfo/ScreenShot";
	public static String screenInfoScreen = "C:/Users/John/Desktop/ScreenInfo/";
	public static String resultInfo = "C:/Users/John/Desktop/ScreenInfo/Results/";
	public static String pokemonInfo ="C:/Users/John/Desktop/PokemonInfo/";

	public  void setupScreenInfoScreenShot() throws IOException{
		Properties prop = new Properties();
		String propFileName = "prop.properties";
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		if(inputStream != null){
			prop.load(inputStream);
		}else{
			System.out.println("lol");
		}
		screenInfoScreenShot = prop.getProperty("screenInfoScreenShot");
		screenInfoScreen = prop.getProperty("screenInfoScreen");
		resultInfo = prop.getProperty("resultInfo");
		pokemonInfo = prop.getProperty("pokemonInfo");
	}
	
	public static LinkedList<Double> getHealthBars(){
		LinkedList<Double> result = new LinkedList<Double>();
		System.out.println(screenInfoScreenShot +"/00002.png");
		Mat health1 = screenRegion(screenInfoScreenShot +"/00002.png",screenInfoScreen + "h1.png",new Rect(75,220,50,6));
		Mat health2 = screenRegion(screenInfoScreenShot +"/00002.png",screenInfoScreen + "h2.png",new Rect(330,18,50,6));
		result.add(getHealthPercent(health1) * 100);
		result.add(getHealthPercent(health2) * 100);
		return result;
	}
	
	public static double getHealthPercent(Mat health1){
		int p1Health =0;
		for(int i = 48;i>0;i--){
			double[] pix = health1.get(1, i);
			if(pix[0] == pix[1] && pix[0] == pix[2])
				p1Health++;
		}
		double health = 1d - p1Health/48d;
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(health));
	}
	public static Mat rotateImageFromSource(String img){
		Mat src = Highgui.imread(img);
		Mat dst = new Mat();
		Core.flip(src.t(), dst, 0);
		System.out.println("printing");
		Highgui.imwrite( img,dst);
		return dst;
	}
	
	public static Mat screenRegion(String screenshot, String output, Rect rect) {
		Mat screen = Highgui.imread(screenshot);
		Mat slot1 = screen.submat(rect);
		Highgui.imwrite(output, slot1);
		return slot1;
	}
	public static Mat screenRegion(Mat screen, String output, Rect rect) {
		Mat slot1 = screen.submat(rect);
		Highgui.imwrite(output, slot1);
		return slot1;
	}
	public static void rotateImage(String screenshot){
		Mat screen = Highgui.imread(screenshot);
		Core.flip(screen.t(), screen, 0);
		Highgui.imwrite(screenshot, screen);
	}

	
	public static void removeGreen(Mat mat){
		double[] green = {94, 229, 158};
		for(int i = 0;i <mat.rows();i++){
			for(int j =0;j < mat.cols();j++){
				double[] color = mat.get(j, i);
				if(color[0] == green[0] && color[1] == green[1] && color[2] == green[2]){
					
				}
			}
		}
	}
	public static String getLatestScreenShot(){
		File folder = new File(screenInfoScreenShot);
		long newest = Integer.MIN_VALUE;
		File newestFile = null;
		for(File file:folder.listFiles()){
			if(file.lastModified() > newest){
				newest = file.lastModified();
				newestFile = file;
			}
		}
		return newestFile.getAbsolutePath();
		
	}
	
	public static LinkedList<Mat> getScreenInfo() {
		LinkedList<Mat> results = new LinkedList<Mat>();
		int x = 3;
		int y = 0;
		int box = 30;
		Rect rect1 = new Rect(140 + x, 40 + y, box, box);
		//String screenShot = screenInfoScreenShot +"Screenshot.png";
		//String screenShot = getLatestScreenShot();
		//System.out.println(screenShot);
		Mat screen = rotateImageFromSource(getLatestScreenShot());
		results.add(screenRegion(screen,
				resultInfo +"/slot1.png", rect1));
		Rect rect2 = new Rect(210 + x, 40 + y, box, box);
		results.add(screenRegion(screen,
				resultInfo +"/slot2.png", rect2));
		Rect rect3 = new Rect(140 + x, 100 + y, box, box);
		results.add(screenRegion(screen,
				resultInfo +"/slot3.png", rect3));
		Rect rect4 = new Rect(210 + x, 100 + y, box, box);
		results.add(screenRegion(screen,
				resultInfo +"/slot4.png", rect4));
		Rect rect5 = new Rect(140 + x, 160 + y, box, box);
		results.add(screenRegion(screen,
				resultInfo +"/slot5.png", rect5));
		Rect rect6 = new Rect(210 + x, 160 + y, box, box);
		results.add(screenRegion(screen,
				resultInfo +"/slot6.png", rect6));

		return results;
	}

	public static LinkedList<Mat> getImages() {
		int count = 1;
		LinkedList<Mat> pokemonMats = new LinkedList<Mat>();
		for (int j = 0; j < 29; j++) {
			for (int i = -1; i < 25; i++) {
				int x = 47 + (i * 38);
				int y = 94 + (j * 38);
				Rect rect = new Rect(x, y, 32, 32);
				// if(j ==27 && i<=3 && count < 719)
				// continue;
				System.out.println("--");
				Mat pok = screenRegion(
						pokemonInfo +"/PokemonSprites.png",
						pokemonInfo +"/Pokemon/pok" + count
								+ ".png", rect);
				//pok.dump();
				pokemonMats.add(pok);
				//Highgui.imwrite(pokemonInfo + "/Pokemon/pok"+count+".png", pok);
				count++;
			}
		}
		return pokemonMats;
	}

	public static HashMap<Rect, Integer> getImageRect() {
		int count = 1;
		HashMap<Rect, Integer> pokemonMats = new HashMap<Rect, Integer>();
		for (int j = 0; j < 28; j++) {
			for (int i = -1; i < 25; i++) {
				int x = 47 + (i * 38);
				int y = 94 + (j * 38);
				Rect rect = new Rect(x, y, 32, 32);
				// if(j ==27 && i<=3 && count < 719)
				// continue;
				pokemonMats.put(rect, count);
				count++;
			}
		}
		return pokemonMats;
	}

	public static void match(Mat img, Mat templ, int match_method,
			int pokeNumber) {

		// System.out.println("\nRunning Template Matching");

		FeatureDetector cvFeatureDetector;
		cvFeatureDetector = FeatureDetector.create(FeatureDetector.GFTT);

		MatOfKeyPoint keyPoint1;
		keyPoint1 = new MatOfKeyPoint();
		cvFeatureDetector.detect(img, keyPoint1);

		MatOfKeyPoint keyPoint2;
		keyPoint2 = new MatOfKeyPoint();
		cvFeatureDetector.detect(templ, keyPoint2);

		// FeatureDetector detector =
		// FeatureDetector.create(FeatureDetector.ORB);

		// / Do the Matching and Normalize
		Imgproc.matchTemplate(img, templ, templ, match_method);
		Core.normalize(templ, templ, 0, 1, Core.NORM_MINMAX, -1, new Mat());

		// / Localizing the best match with minMaxLoc
		MinMaxLocResult mmr = Core.minMaxLoc(templ);

		System.out.println(pokeNumber);
		System.out.println(Math.abs(mmr.maxVal));
		System.out.println(Math.abs(mmr.minVal));
		System.out.println();

	}

	public static LinkedList<String> getPokemonNamesOnScreen() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		LinkedList<Mat> pokemons = getScreenInfo();
		HashMap<Rect, Integer> squares = getImageRect();
		LinkedList<String> pokemonName = new LinkedList<String>();
		Mat img = Highgui
				.imread(pokemonInfo +"/PokemonSprites.png");
		// int count = 1;
		for (Mat ok : pokemons) {
			// revenge(img, ok,pokemonInfo +"/Testing" +
			// count + ".png",Imgproc.TM_SQDIFF);
			Point midPoint = findMidPoint(img, ok, Imgproc.TM_SQDIFF);
			for (Rect rect : squares.keySet()) {
				if (rect.contains(midPoint)) {
					// System.out.println("PokemonNumber = " +
					// squares.get(rect));
					String pokemon = PokemonList.pokemonNames[squares.get(rect) - 1];
					pokemonName.add(pokemon);
					// System.out.println("Name  = " +
					// PokemonList.pokemonNames[squares.get(rect) -1]);
				}
			}
			// count++;
		}
		return pokemonName;
	}

	public static LinkedList<Integer> getPokemonNumbersOnScreen() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		LinkedList<Mat> pokemons = getScreenInfo();
		HashMap<Rect, Integer> squares = getImageRect();
		LinkedList<Integer> pokemonName = new LinkedList<Integer>();
		Mat img = Highgui
				.imread(pokemonInfo +"/PokemonSprites.png");
		// int count = 1;
		for (Mat ok : pokemons) {
			// revenge(img, ok,pokemonInfo +"/Testing" +
			// count + ".png",Imgproc.TM_SQDIFF);
			Point midPoint = findMidPoint(img, ok, Imgproc.TM_SQDIFF);
			for (Rect rect : squares.keySet()) {
				if (rect.contains(midPoint)) {
					// System.out.println("PokemonNumber = " +
					// squares.get(rect));
					Integer pokemon = squares.get(rect);
					pokemonName.add(pokemon);
					// System.out.println("Name  = " +
					// PokemonList.pokemonNames[squares.get(rect) -1]);
				}
			}
			// count++;
		}
		return pokemonName;
	}

	public static void copyGifs(LinkedList<String> names) {
		int count = 0;
		for (String name : names) {

			System.out.println(name);
			int i = PokemonList.nameToDex.get(name);
			String numZeros = "";
			if (i < 100)
				numZeros = "0";
			if (i < 10)
				numZeros = "00";
			numZeros = numZeros + i;
			System.out.println("---" + numZeros);
			File source = new File(
					pokemonInfo +"/Sprires/xy-animated/"
							+ numZeros + ".gif");
			File target = new File(
					pokemonInfo +"/Sprires/Current/"
							+ count + ".gif");
			File source2 = new File(pokemonInfo +"/Pokemon/pok" + i
					+ ".png");
			File target2 = new File(
					pokemonInfo +"/Sprires/CurrentGreen/"
							+ count + ".png");
			count++;
			try {
				copyFileUsingFileStreams(source, target);
				System.out.println("---");
				copyFileUsingFileStreams(source2, target2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void copyGifsName(LinkedList<Integer> names) {
		int count = 0;
		for (Integer i : names) {

			String numZeros = "";
			if (i < 100)
				numZeros = "0";
			if (i < 10)
				numZeros = "00";
			numZeros = numZeros + i;
			File source = new File(
					pokemonInfo +"/Sprires/xy-animated/"
							+ numZeros + ".gif");
			File target = new File(
					pokemonInfo +"/Sprires/Current/"
							+ count + ".gif");
			File source2 = new File(pokemonInfo +"/Pokemon/pok" + i
					+ ".png");
			File target2 = new File(
					pokemonInfo +"/Sprires/CurrentGreen/"
							+ count + ".png");
			count++;
			
			try {
				copyFileUsingFileStreams(source, target);
				copyFileUsingFileStreams(source2, target2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void copyFileUsingFileStreams(File source, File dest)
			throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}

	public static void revenge(Mat img, Mat templ, String outFile,
			int match_method) {

		// / Create the result matrix
		int result_cols = img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		// System.out.println(result_cols + " " + result_rows);
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		// / Do the Matching and Normalize
		Imgproc.matchTemplate(img, templ, result, match_method);
		// Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new
		// Mat());

		// / Localizing the best match with minMaxLoc
		MinMaxLocResult mmr = Core.minMaxLoc(result);

		Point matchLoc;
		if (match_method == Imgproc.TM_SQDIFF
				|| match_method == Imgproc.TM_SQDIFF_NORMED) {
			matchLoc = mmr.minLoc;
		} else {
			matchLoc = mmr.maxLoc;
		}

		// Core.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
		// matchLoc.y + templ.rows()), new Scalar(0, 255, 0));
		Rect rect = new Rect(matchLoc, new Point(matchLoc.x + templ.cols(),
				matchLoc.y + templ.rows()));

		// System.out.print(rect);
		int midX = rect.x + rect.width / 2;
		int midY = rect.y + rect.height / 2;
		Point midPoint = new Point(midX, midY);
		System.out.println(midPoint);
		Mat out = img.submat(rect);
		System.out.println("Writing " + outFile);
		// Highgui.imwrite(outFile, out);

	}

	public static Point findMidPoint(Mat img, Mat templ, int match_method) {
		// / Create the result matrix
		int result_cols = img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		// / Do the Matching and Normalize
		Imgproc.matchTemplate(img, templ, result, match_method);

		// / Localizing the best match with minMaxLoc
		MinMaxLocResult mmr = Core.minMaxLoc(result);

		Point matchLoc;
		if (match_method == Imgproc.TM_SQDIFF
				|| match_method == Imgproc.TM_SQDIFF_NORMED) {
			matchLoc = mmr.minLoc;
		} else {
			matchLoc = mmr.maxLoc;
		}
		Rect rect = new Rect(matchLoc, new Point(matchLoc.x + templ.cols(),
				matchLoc.y + templ.rows()));
		int midX = rect.x + rect.width / 2;
		int midY = rect.y + rect.height / 2;
		Point midPoint = new Point(midX, midY);
		return midPoint;

	}
	
	public static int roundUp(int n) {
	    int i = (n + 49) / 50 * 50;
	    if(i > 252)
	    	return 252;
	    return i;
	}
	
	public static void getImagesColors(Mat img){
		HashMap<Color, String> mappingToChar = new HashMap<Color,String>();
		char charCount =' ';
		for(int i = 0;i<img.rows(); i++){
			for(int j = 0; j<img.cols(); j++){
				double[] color= img.get(i, j);
				int r = roundUp((int)color[0]);
				int g = roundUp((int)color[1]);
				int b = roundUp((int)color[2]);
				//System.out.println(r % 10 * 10);
				int c = (r + g + b);
				if(!mappingToChar.containsKey(c)){
					mappingToChar.put( new Color(r,g,b), charCount +"");
					//charCount++;
				}
				
				//System.out.print(mappingToChar.get(new Color(r,g,b)) + " ");
				
			}
			
		}
		System.out.println(mappingToChar.keySet().size());
	}
	
	public static void getPokemonCurrentlyInBattle(LinkedList<Integer> pokes){
		int count =0;
		Mat img = Highgui.imread(getLatestScreenShot());
		for(Integer pokemon:pokes){
			//System.out.println(pokemon);
			String value = pokemonInfo + "Sprires/xy/";
			if(pokemon < 100)
				value += "0";
			if(pokemon < 10)
				value += "0";
			value += pokemon +".png";
			//System.out.println(value);
			Mat templ = Highgui.imread(value);
			getImagesColors(templ);
			/*int result_cols = img.cols() - templ.cols() + 1;
			int result_rows = img.rows() - templ.rows() + 1;
			Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
			result = img.submat(new Rect(240,40,templ.rows(),templ.cols()));
			
			Mat histResultA = compareHist(result);
			Mat histResultB= compareHist(templ);
			
			System.out.println(Imgproc.compareHist(histResultA, histResultB, Imgproc.CV_COMP_CORREL));
			
			
			
			Highgui.imwrite(pokemonInfo +""
					+ count + "1232.png", result);
			*/
			count++;
			
		}
		
	}
	
	public static Mat compareHist(Mat imageIR){
		java.util.List<Mat> matList = new LinkedList<Mat>();
		matList.add(imageIR);
		Mat histogram = new Mat();
		MatOfFloat ranges=new MatOfFloat(0,256);
		MatOfInt histSize = new MatOfInt(255);
		Imgproc.calcHist(
		                matList, 
		                new MatOfInt(0), 
		                new Mat(), 
		                histogram , 
		                histSize , 
		                ranges);

		// Create space for histogram image
		Mat histImage = Mat.zeros( 100, (int)histSize.get(0, 0)[0], CvType.CV_32F);
		// Normalize histogram                          
		Core.normalize(histogram, histogram, 1, histImage.rows() , Core.NORM_MINMAX, -1, new Mat() );   
		return histImage;
	}
	

	public static void getPokemonOnScreen() {
		LinkedList<Mat> pokemonMats = getImages();
		LinkedList<Mat> pokemonOnScreen = getScreenInfo();
		int pokeCount = 1;
		Mat slot1 = pokemonOnScreen.get(0);
		for (Mat pokemonMat : pokemonMats) {
			match(slot1, pokemonMat, Imgproc.TM_SQDIFF, pokeCount++);
		}
	}

	public static void main(String[] args) {
		/*
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		LinkedList<String> pokemonNames = getPokemonNamesOnScreen();
		int count = 0;
		for (String name : pokemonNames) {
			System.out.print(name + "\t\t");
			count++;
			if (count % 2 == 0)
				System.out.println("\n");
		}*/
		//System.out.println(getLatestScreenShot());
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//getHealthBars();
		try {
			new MatchingMethod().setupScreenInfoScreenShot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//getImages();
	}
}
