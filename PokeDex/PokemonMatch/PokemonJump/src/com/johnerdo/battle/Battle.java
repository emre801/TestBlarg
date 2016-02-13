package com.johnerdo.battle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import org.johnerdo.globalInfo.PokemonList;
import org.opencv.core.Core;

import com.johnerdo.imageCompare.MatchingMethod;
import com.johnerdo.imageCompare.RobotBot;
import com.pokejava.Pokemon;

public class Battle {

	//LinkedList<String> pokemonOnScreen;
	LinkedList<Pokemon> pokemon;
	public Battle(){
		this.pokemon = new LinkedList<Pokemon>();
		PokemonList.setMapping();
	}
	public void getPokemonOnScreen(boolean pushButton) throws InterruptedException{
		if(pushButton){
			RobotBot.Screen();
			Thread.sleep(1500);
		}
		//setUp();
		LinkedList<Integer> pokemonNums = new LinkedList<Integer>();
		pokemonNums.add(9);
		pokemonNums.add(3);
		pokemonNums.add(6);
		pokemonNums.add(20);
		pokemonNums.add(21);
		pokemonNums.add(22);
		setUpManually(pokemonNums);
		printPokemon();
	}
	public void getHealth(boolean pushButton) throws InterruptedException{
		if(pushButton){
			RobotBot.Screen();
			Thread.sleep(1500);
		}
		LinkedList<Double> health = MatchingMethod.getHealthBars();
		for(Double d: health){
			System.out.println(d);
		}
	}
	public void setUp(){
		LinkedList<Integer> pokemonNums = MatchingMethod.getPokemonNumbersOnScreen();
		for(Integer dexNum :pokemonNums){
			if(dexNum<720)
				pokemon.add(new Pokemon(dexNum));
			else
				pokemon.add(new Pokemon(PokemonList.pokemonNames[dexNum]));
		}
		MatchingMethod.copyGifsName(pokemonNums);
	}
	
	public void setUpManually(LinkedList<Integer> pokemonNums){
		for(Integer dexNum :pokemonNums){
			if(dexNum<720)
				pokemon.add(new Pokemon(dexNum));
			else
				pokemon.add(new Pokemon(PokemonList.pokemonNames[dexNum]));
		}
		MatchingMethod.copyGifsName(pokemonNums);
		MatchingMethod.getPokemonCurrentlyInBattle(pokemonNums);
	}
	
	public void printPokemon(){
		Iterator<Pokemon> pokIter = pokemon.iterator();
		while(pokIter.hasNext()){
			Pokemon pok1 = pokIter.next();
			Pokemon pok2 = pokIter.next();
			System.out.println(PokemonList.printPokemonInfo(pok1, pok2));
		}
	}
	
	public static void runShellScreenShot() throws IOException{
		Runtime run = Runtime.getRuntime();
		Process pr = run.exec("./3dstest");
		 BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
         String line = "";
         while ((line = input.readLine()) != null) {
             System.out.println(line);
         }

	}
	public static void main(String[] args) throws InterruptedException, IOException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//runShellScreenShot();
		new MatchingMethod().setupScreenInfoScreenShot();
		Battle b = new Battle();
		b.getPokemonOnScreen(false);
		//b.getHealth(false);
	}
}
