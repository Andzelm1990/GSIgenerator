package com.niwelacja;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        float wymaganePrzewyzszenie ;
        float wymaganaDlugoscOdcinka ;
        String firstReper, endReper;

        //Utworzenie instancji klasy Mechanika - umo¿liwiaj¹c¹ wykorzystanie metod mechaniki programu.
        Mechanika mech = new Mechanika();

        System.out.println();
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("***************** GSI GENERATOR  *****************");
        System.out.println("*****************     V 1.0      *****************");
        System.out.println("**************************************************");
        System.out.println();
        System.out.println();

        //Flaga dla pêtli maj¹cej za zadanie wczytanie od u¿ytkownika danych oraz upewnieniu siê ¿e dane s¹ poprawne
        boolean czyWprowadzonoPoprawneDane;
        do {
            Scanner sca = new Scanner(System.in);
            System.out.println("Proszê podaæ nastêpuj¹ce dane:");
            System.out.println("Wymagane przewy¿szenie (format 5,65857 lub -4,65926): ");
            wymaganePrzewyzszenie = mech.wprowadzFloat();
            System.out.println("D³ugoœæ generowanego odcinka (format 2,56): ");
            wymaganaDlugoscOdcinka = mech.wprowadzFloat();
            System.out.println("Numer reperu pocz¹tkowego: ");
            firstReper = sca.nextLine();
            firstReper = mech.firstRapeGSI(firstReper);
            System.out.println("Numer reperu koñcowego: ");
            endReper = sca.nextLine();
            endReper = mech.firstRapeGSI(endReper);

            System.out.println("Czy poni¿sze dane siê zgadzaj¹ (T -tak/N - nie) :");
            System.out.println("Przewy¿szenie: " + wymaganePrzewyzszenie);
            System.out.println("D³ugoœæ odcinka: " + wymaganaDlugoscOdcinka);
            System.out.println("Reper pocz¹tkowy: " + firstReper);
            System.out.println("Reper koñcowy: " + endReper);
            czyWprowadzonoPoprawneDane = mech.akceptacja();
            System.out.println();
            if (!czyWprowadzonoPoprawneDane)
                System.out.println("Podaj dane jeszcze raz.");
        }while (!czyWprowadzonoPoprawneDane);

        //Program wylicza iloœæ stanowisk potrzebnych do wykonaniu pomiaru o podanej d³ugoœci
        int numberOfPosition = mech.howManyPositions(wymaganaDlugoscOdcinka,wymaganePrzewyzszenie);
        //Nastêpnie program generuje tablicê zawieraj¹c¹ d³ugoœci na poszczególnych odcinkach pomiarowych
        ArrayList<Integer> measuringLength = mech.measuringLengthGenerator(numberOfPosition);

        //Fragment kodu który podczas pracy s³u¿y³ do podgl¹du wygenerowanych wartoœci
        //System.out.println(numberOfPosition);
        //System.out.println(mech.howSteep(wymaganaDlugoscOdcinka,wymaganePrzewyzszenie));
        //int suma = 0;
        // int licznik = 3;
        /*
        for (int o: measuringLength ) {
            if ((licznik % 2) != 0) {
                System.out.print("Odczyt w ty³: ");
            }
            else {
                System.out.print("Odczyt w przód: ");
            }
            System.out.println(o);
            suma += o;
            licznik ++;
        }
        System.out.println(suma);
         */
        //------------------------------------------

        //W tym momêcie program wylicza œrednie przewy¿szenie
        float averageElevation = mech.averageElevation(wymaganePrzewyzszenie, numberOfPosition);
        System.out.println("Œrednie przewyzszenie na stanowisko: " + averageElevation);

        //Flaga s³u¿¹ca do zakoñczenia pêtli tworz¹cej listê przewy¿szeñ. Pêtla koñczy siê w przypadku kiedy suma wygenerowanych przewy¿szeñ zbli¿y siê do tej oczekiwanej
        boolean elevationIsClouse = false;
        int[] elevation = new int[2*numberOfPosition] ;
        do {
            float sumaprzewyzszen = 0f;
            for (int i = 0; i < numberOfPosition; i++) {
                ArrayList<Integer> temp = mech.elevationInPosition(wymaganePrzewyzszenie, averageElevation);
                int one = temp.get(0);
                int two = temp.get(1);
                sumaprzewyzszen = sumaprzewyzszen + (one - two);
                elevation[2*i] = one;
                elevation[(2*i)+1] = two;
            }
            if (sumaprzewyzszen >= ((wymaganePrzewyzszenie*100000)-20000) &&  sumaprzewyzszen <= ((wymaganePrzewyzszenie*100000)+20000)){
                elevationIsClouse = true;
            }
        }while (!elevationIsClouse);

        mech.generationGsi(elevation, measuringLength, firstReper, endReper, numberOfPosition, wymaganePrzewyzszenie);

    }
}
