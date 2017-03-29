/**
* 	Program szyfruje plik z rozszerzeniem txt
* 	z interfejsem użytkownika wykorzystującym Swing
*
* 	Wymagana JDK 7 lub nowsza wersja
*
*	@author Bartosz Pajewski
*	
**/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;



class Szyfr implements ActionListener{
	
	// Tutaj trzymam zmienne globalne
	JTextField wejscie, rotCode;  // przechowywuje nazwe pliku, ktory bedziemy szyfrowac lub sprobowac rozkodowac
	
	JButton pSzyfr; // przycisk polecenia szyfruj
	JButton pProba; // przycisk proby odkodowania
	
	JLabel label, label2, label3; // wyswietla komunikaty i zachete
	
	FileReader f;
	String linia;
	
	
	
	Szyfr() {
		
		// Okno, budowa, rozmiar, domyslna reakcja na zamkniecie
		JFrame jfrm = new JFrame("Szyfrowanie podstawieniowe");
		jfrm.setLayout( new FlowLayout());
		jfrm.setSize(480,480);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		wejscie = new JTextField(20);
		wejscie.setActionCommand("stream"); // od strumień
		rotCode = new JTextField(2);
		rotCode.setActionCommand("rotV");  // od ROT Value 
		
		
		pSzyfr = new JButton("Szyfruj");
		pProba = new JButton("Szukaj klucza");
		
		pSzyfr.addActionListener(this);
		pProba.addActionListener(this);
		
		label = new JLabel("Wpisz nazwe pliku: ");
		label2 = new JLabel(" ");
		label3 = new JLabel("(0-32) ROT: ");
		
		jfrm.add(label);
		jfrm.add(wejscie);
		jfrm.add(label3);
		jfrm.add(rotCode);
		jfrm.add(pSzyfr);
		jfrm.add(pProba);
		jfrm.add(label2);
		
		jfrm.setVisible(true);
	} 		//////////////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent ae)
	{
		// Zmienne do wejsciowego pliku
		FileReader fr=null;
		BufferedReader bfr;
		String linia="";
		// ----------- COD ROT szyfrowania ------------ //
		int nosnik=0;
		int wynik=(-1);
		//------------------ // ----------------------- //
		BufferedWriter bfw=null;
		FileWriter fw=null;

	
///////////////// WARUNKI TESTOWE DO PLIKÓW 	
		if(wejscie.getText().equals("")){
			label2.setText("Brak nazwy pliku!");
			return;
		}
		if(rotCode.getText().equals("")){
			label2.setText("Nie podano parametru ROT");
			return;
		}
///////////////// Rozpoczynamy probe wczytania pliku do szyfrowania
		nosnik = Integer.parseInt(rotCode.getText());
				if(nosnik<0 || nosnik>32)
		{
			label2.setText("Podano niepoprawny kod ROT");
			return;
		}
		if(ae.getActionCommand().equals("Szyfruj"))
		{	
			// --------- OTWIERANIE PLIKU ----------- //
			try { fr = new FileReader(wejscie.getText()); }  // <-- "łapie" plik z pola tekstowego, szukamy źródła, próbujemy wczytac
			catch (FileNotFoundException ex) {label2.setText("Wystapil problem z pobraniem pliku"); }
			// --------- TWORZENIE PLIKU Z WIADOMOSCIA ZASZYFROWANA //
			try { fw = new FileWriter("wiadomosc.txt"); } 
			catch (IOException e) { e.printStackTrace(); }
			// Uchwyty strumieni
			PrintWriter bw = new PrintWriter(fw, true); // <-- "łapie" plik w trybie "utwórz nowy i dopisz linie na końcu"
			bfr = new BufferedReader(fr); // <-- "łapie" plik źródłowy w strumień
			// ODCZYT KOLEJNYCH LINII Z PLIKU I SZYFROWANIE:
			try {
			while((linia = bfr.readLine()) != null)
			{
			    char x[] = linia.toCharArray(); 
				for( int i=0 ; i!=x.length ; i++) 
				{
					int n = x[i];
						if(n>134) // Wykrywanie polskich znakow diakrytycznych
						{
							label2.setText("Wykryto w tekscie polskie znaki diakratyczne!");
						}
						if(n>96) // Zamiana malych znakow na duze do szyfrowania
						{
							n-=32;
						}
					n+=nosnik; 
					x[i]=(char)n;
				} 
				linia = new String(x);
				bw.println(linia); // <-- wpisanie "zaszyfrowanej" linii wiadomosci
			}
				// Zwolnienie uchwytow strumieni
				fw.close();
				bw.close();
				bfr.close();
			}
			catch (IOException e) { label2.setText("BLAD ODCZYTU/ZAPISU/SZYFRU Z PLIKU!");	}
		}
		else
		{
			// --------- OTWIERANIE PLIKU ----------- //
			try { fr = new FileReader(wejscie.getText()); }  
			catch (FileNotFoundException ex) { label2.setText("Wystapil problem z pobraniem pliku"); }

			bfr = new BufferedReader(fr); // <-- "łapie" plik źródłowy w strumień

			try{
				while((linia = bfr.readLine()) != null)
				{
					char x[] = linia.toCharArray(); 
					for( int i=0 ; i!=x.length ; i++) 
					{
					int n = x[i];		// Szukamy spacji i ustalamy o ile zostało przesunięte
						if(n<65)
						{
							wynik=((n-32));
						}
					}
					break;
				}
				if(wynik>=0 & wynik<=32)
				{	
					label2.setText("Klucz ROT to: "+wynik);
				}
				else{
					label2.setText("Nie znaleziono klucza");
				}
			fr.close();
			bfr.close();
			}
			catch (IOException e) { label2.setText("BLAD ODCZYTU/ZAPISU/SZYFRU Z PLIKU!");	}
			
		}
	}	
	
	
	
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable(){
		public void run(){
						
			//int nosnik = Integer.parseInt(rotV);
			
			new Szyfr();
		}
		}
			);
	}
}