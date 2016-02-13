

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class SaveImageFromUrl {

	public static void main(String[] args) throws Exception {
		getImages();
	}
	
	public static void getImages() throws IOException{
		String destinationFile = "/home/emre801/Desktop/PokemonInfo/Sprires/xy/";
		String imageUrl = "http://www.serebii.net/xy/pokemon/";
		for(int i =721; i <= 721; i++){
		String img = "";
		if(i < 100)
			img += "0";
		if(i < 10)
			img += "0";
		img += i + ".png";
		saveImage(imageUrl+ img, destinationFile + img);
		}
		
	}

	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

}