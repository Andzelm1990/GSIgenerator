package com.niwelacja;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        float wymaganePrzewyzszenie ;
        float wymaganaDlugoscOdcinka ;
        String firstReper, endReper;

        //Utworzenie instancji klasy Mechanika - umo�liwiaj�c� wykorzystanie metod mechaniki programu.
        Mechanika mech = new Mechanika();

        System.out.println();
        System.out.println();
        System.out.println("**************************************************");
        System.out.println("***************** GSI GENERATOR  *****************");
        System.out.println("*****************     V 1.0      *****************");
        System.out.println("**************************************************");
        System.out.println();
        System.out.println();

        //Flaga dla p�tli maj�cej za zadanie wczytanie od u�ytkownika danych oraz upewnieniu si� �e dane s� poprawne
        boolean czyWprowadzonoPoprawneDane;
        do {
            Scanner sca = new Scanner(System.in);
            System.out.println("Prosz� poda� nast�puj�ce dane:");
            System.out.println("Wymagane przewy�szenie (format 5,65857 lub -4,65926): ");
            wymaganePrzewyzszenie = mech.wprowadzFloat();
            System.out.println("D�ugo�� generowanego odcinka (format 2,56): ");
            wymaganaDlugoscOdcinka = mech.wprowadzFloat();
            System.out.println("Numer reperu pocz�tkowego: ");
            firstReper = sca.nextLine();
            firstReper = mech.firstRapeGSI(firstReper);
            System.out.println("Numer reperu ko�cowego: ");
            endReper = sca.nextLine();
            endReper = mech.firstRapeGSI(endReper);

            System.out.println("Czy poni�sze dane si� zgadzaj� (T -tak/N - nie) :");
            System.out.println("Przewy�szenie: " + wymaganePrzewyzszenie);
            System.out.println("D�ugo�� odcinka: " + wymaganaDlugoscOdcinka);
            System.out.println("Reper pocz�tkowy: " + firstReper);
            System.out.println("Reper ko�cowy: " + endReper);
            czyWprowadzonoPoprawneDane = mech.akceptacja();
            System.out.println();
            if (!czyWprowadzonoPoprawneDane)
                System.out.println("Podaj dane jeszcze raz.");
        }while (!czyWprowadzonoPoprawneDane);

        //Program wylicza ilo�� stanowisk potrzebnych do wykonaniu pomiaru o podanej d�ugo�ci
        int numberOfPosition = mech.howManyPositions(wymaganaDlugoscOdcinka,wymaganePrzewyzszenie);
        //Nast�pnie program generuje tablic� zawieraj�c� d�ugo�ci na poszczeg�lnych odcinkach pomiarowych
        ArrayList<Integer> measuringLength = mech.measuringLengthGenerator(numberOfPosition);

        //Fragment kodu kt�ry podczas pracy s�u�y� do podgl�du wygenerowanych warto�ci
        //System.out.println(numberOfPosition);
        //System.out.println(mech.howSteep(wymaganaDlugoscOdcinka,wymaganePrzewyzszenie));
        //int suma = 0;
        // int licznik = 3;
        /*
        for (int o: measuringLength ) {
            if ((licznik % 2) != 0) {
                System.out.print("Odczyt w ty�: ");
            }
            else {
                System.out.print("Odczyt w prz�d: ");
            }
            System.out.println(o);
            suma += o;
            licznik ++;
        }
        System.out.println(suma);
         */
        //------------------------------------------

        //W tym mom�cie program wylicza �rednie przewy�szenie
        float averageElevation = mech.averageElevation(wymaganePrzewyzszenie, numberOfPosition);
        System.out.println("�rednie przewyzszenie na stanowisko: " + averageElevation);

        //Flaga s�u��ca do zako�czenia p�tli tworz�cej list� przewy�sze�. P�tla ko�czy si� w przypadku kiedy suma wygenerowanych przewy�sze� zbli�y si� do tej oczekiwanej
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
