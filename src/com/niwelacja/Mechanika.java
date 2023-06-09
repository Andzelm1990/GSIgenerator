package com.niwelacja;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Mechanika {

    /**
     * Funkcja sprawdzaj¹ca czy wprowadzony ci¹g znaków to liczba typu  float
     * @return wynikowa - bêd¹ca  poprawn¹ liczb¹
     */
    public float wprowadzFloat () {
        Scanner sc = new Scanner(System.in);
        float wynikowa = 0f;
        do {
            if (sc.hasNextFloat()) {
                wynikowa = sc.nextFloat();
            } else {
                System.out.println("Nie poda³eœ liczby zmiennoprzecinkowej typu Float (przyk³ad: 54,6587)");
                System.out.println("Podaj liczbê jeszcze raz.");
            }
        }while (wynikowa == 0f);
        return wynikowa;
    }

    /**
     * Metoda sprawdzaj¹ca czy wprowadzone znaki odpowiadaj¹ akceptacji "T,t" lub brak akceptacji "n,N"
     * @return boolean - akceptacja/brak akceptacji
     */
    public boolean akceptacja (){
    Scanner sc = new Scanner(System.in);
    boolean potwierdzenie = false;
    boolean flag = false;
    char tn = '0';
    do {
        if (sc.hasNextLine()){
            tn = sc.nextLine().charAt(0);
        }
        if (tn == 't' || tn == 'T'){
            potwierdzenie = true;
            flag = true;
        }
        else if (tn == 'n' || tn == 'N'){
            flag = true;
        }
        else {
            System.out.println("nie poda³eœ symbolu T lub N. WprowadŸ jeszcze raz.");
        }
    }while (!flag);
    return potwierdzenie;
    }

    /**
     * The function give information how is Steep:
     * 1-low steep
     * 2-medium steep
     * 3-very steep
     * @param lenghtLeveling - estimated length of the measuring section
     * @param elevation - predicted elevation gain on the measuring section
     * @return howSteep
     */
    public int howSteep (float lenghtLeveling, float elevation){
        int howSteep = 0;
        if ((elevation/lenghtLeveling) > 10){
            howSteep = 3;
        }else if ((elevation/lenghtLeveling) > 5){
            howSteep = 2;
        }else if ((elevation/lenghtLeveling) <= 5){
            howSteep =1;
        }else {
            System.out.println("Error in function howSteep");
        }
        return howSteep;
    }

    /**
     * Metoda s³u¿y do obliczenia iloœci pozycji w zale¿noœci od stopnia nachylenia terenu
     * @param lenghtLeveling - wymagana d³ugoœæ odcinka
     * @param elevation - wymagane przewy¿szenie
     * @return - zwraca liczbê pozycji wymagan¹ do wygenerowania pomiaru danego odcinka
     */
    public int howManyPositions (float lenghtLeveling, float elevation){
        int howManyPositions = 0;
        if (howSteep(lenghtLeveling, elevation) == 1)
            howManyPositions = Math.round(lenghtLeveling/0.085F);
        else if (howSteep(lenghtLeveling, elevation) == 2)
            howManyPositions = Math.round(lenghtLeveling/0.055F);
        else if (howSteep(lenghtLeveling, elevation) == 3)
            howManyPositions = Math.round(lenghtLeveling/0.025F);
        else System.out.println("Error in function howManyPositions");

        if ((howManyPositions%2) != 0)
            howManyPositions ++;

        return howManyPositions;
    }

    public ArrayList<Integer> measuringLengthGenerator(int numberOfPositions){
        ArrayList<Integer> measuringLength = new ArrayList<>();
        Random generation = new Random();

        measuringLength.add(1000000 + generation.nextInt(999999));
        measuringLength.add(1000000 + generation.nextInt(999999));

        for (int i = 2; i < (2*numberOfPositions)-2; i++) {
            measuringLength.add(4000000 + generation.nextInt(799999));
        }

        measuringLength.add(1000000 + generation.nextInt(999999));
        measuringLength.add(1000000 + generation.nextInt(999999));

        return measuringLength;
    }

    public  float averageElevation(float elevation, int numberOfPosition){
        return elevation/((float)numberOfPosition);
    }

    public ArrayList<Integer> elevationInPosition(float elevation, float averageElevation){
        Random generation = new Random();
        int averageElevation100000 = (int)(averageElevation * 100000);
        ArrayList<Integer> elevationInOnePosition = new ArrayList<>();
        if (elevation >=0){
            elevationInOnePosition.add(120000 + generation.nextInt(69999));
            elevationInOnePosition.add(elevationInOnePosition.get(0) - ((generation.nextInt(averageElevation100000+averageElevation100000+30000+30000)-30000)));
        }else {
            elevationInOnePosition.add(80000 + generation.nextInt(59999));
            elevationInOnePosition.add(elevationInOnePosition.get(0) + ((generation.nextInt( Math.abs(averageElevation100000)+Math.abs(averageElevation100000)+30000+30000)-30000)));
        }
        return  elevationInOnePosition;

    }

    public String firstRapeGSI (String firstRape){
        if (firstRape.length()>8)
        {
            System.out.println("numer reperu nie mo¿e byæ d³u¿szy ni¿ 8 znaków");
        }
        else if (firstRape.length() < 8){
            do {
                firstRape = "0" + firstRape;
            }while (firstRape.length() < 8);
        }

        return firstRape;
    }

    public String accuracyGenerator(){
        Random gen = new Random();
        int temp = gen.nextInt(29);
        String accurancy;
        if (temp < 10){
            accurancy = "0000000" + temp;
        }else {
            accurancy = "000000" + temp;
        }
        return accurancy;
    }

    public String elevationValueToGsiString (int elevation){
        String temp = "" + elevation;

        if (temp.length()>8){
            temp = temp.substring(0 , 8);
        }

        if (temp.length()<8){
            do {
                temp = "0" + temp;
            }while (temp.length()<8);
        }
        return temp;
    }

    public int FloatOnIntigerMilimetr (float changFlote){
        return (int)(changFlote*100000);
    }

    public  void generationGsi (int[] elevation, ArrayList<Integer> measuringLength, String firstRape, String endRape, int numberOfPosition, float wymaganeprzewyzszenie){
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwê pliku (Bez rozszerzenia)");
        String nazwaPliku = scan.nextLine();
        String path = nazwaPliku + ".txt";
        File f = new File(path);

        if (!f.exists()) {
            try {
                f.createNewFile();
                System.out.println("Plik zostya³ utworzony");
            } catch (Exception var12) {
                System.out.println("Nie uda³o siê utworzyæ plików.");
            }
        }


        try {
            if (f.canWrite()) {
                FileWriter fw = new FileWriter(f, true);
                Formatter fm = new Formatter(fw);
                String text = "";
                new Scanner(System.in);

            //pierwsze 5 wierszy które s¹ unikatowe
                String temp = "410001+?......1";
                fm.format("%s \n", temp);

                temp = "110002+" + firstRape + " 83..18+00000000";
                fm.format("%s \n", temp);

                temp = "110003+" + firstRape + " 32...8+0" + measuringLength.get(0) + " 331.08+"
                        + elevationValueToGsiString(elevation[0]) + " 390...+00000002 391.08+" + accuracyGenerator();
                fm.format("%s \n", temp);

                temp = "110004+00000001 32...8+0" + measuringLength.get(1) + " 332.08+" + elevationValueToGsiString(elevation[1]) + " 390...+00000002 391.08+"
                        + accuracyGenerator();
                fm.format("%s \n", temp);

                temp = "110005+00000001 573..8" +(((measuringLength.get(0) - measuringLength.get(1)) >= 0)? "+":"-") + elevationValueToGsiString(Math.abs(measuringLength.get(0) - measuringLength.get(1)))
                            +  " 574..8+" + elevationValueToGsiString(measuringLength.get(0)+measuringLength.get(1)) +  " 83..08" +
                            (((elevation[0]-elevation[1]) >= 0)? "+": "-")
                            + (elevationValueToGsiString(Math.abs(elevation[0]-elevation[1])));
                fm.format("%s \n", temp);
                
            //œrodkowe wersy zapisywane w pêtli
                int nrLine = 110006;
                int nrFroog = 1;
                int addMeasuringLength = measuringLength.get(0)+measuringLength.get(1);
                int addElevation = elevation[0]-elevation[1];

                for (int i=1 ; i<(numberOfPosition - 1) ; i++){

                    temp = nrLine + "+" + elevationValueToGsiString(nrFroog) + " 32...8+0" + measuringLength.get(i*2) + " 331.08+"
                            + elevationValueToGsiString(elevation[i*2]) + " 390...+00000002 391.08+" + accuracyGenerator();
                    fm.format("%s \n", temp);
                    nrLine ++;
                    nrFroog ++;

                    temp = nrLine + "+" + elevationValueToGsiString(nrFroog) +" 32...8+0" + measuringLength.get((i*2)+1) + " 332.08+" + elevationValueToGsiString(elevation[(i*2)+1]) + " 390...+00000002 391.08+"
                            + accuracyGenerator();
                    fm.format("%s \n", temp);

                    nrLine ++;

                    addMeasuringLength += measuringLength.get(i*2)+measuringLength.get((i*2)+1);
                    addElevation += elevation[i*2]-elevation[(i*2)+1];


                    temp = nrLine + "+" + elevationValueToGsiString(nrFroog) + " 573..8" +(((measuringLength.get(i*2) - measuringLength.get((i*2)+1)) >= 0)? "+":"-") + elevationValueToGsiString(Math.abs(measuringLength.get(i*2) - measuringLength.get((i*2)+1)))
                            +  " 574.." + ((addMeasuringLength > 99999999)? "6": "8") + "+" + elevationValueToGsiString(addMeasuringLength) +  " 83..08" +
                            ((addElevation >= 0)? "+": "-")
                            + (elevationValueToGsiString(Math.abs(addElevation)));
                    fm.format("%s \n", temp);
                    nrLine++;
                }

                //koñcowe 3 wiersze

                Random r = new Random();
                addMeasuringLength += measuringLength.get((numberOfPosition*2)-2)+measuringLength.get((numberOfPosition*2)-1);
                int addElevationTemp = addElevation + (elevation[(numberOfPosition*2)-2]-elevation[(numberOfPosition*2)-1]);
                //System.out.println("Przewy¿szenie z programu: " + addElevationTemp);
                int differenceElevationExpected = FloatOnIntigerMilimetr(wymaganeprzewyzszenie)  - addElevationTemp;
                //System.out.println("Ró¿nica w pliku wynikowym :" + differenceElevationExpected);
               // System.out.println("Przewy¿szenie przed popraw¹: " + (elevation[(numberOfPosition*2)-1]));
                int correctedScore = (elevation[(numberOfPosition*2)-1]) - (differenceElevationExpected + r.nextInt(150)) ;
               // System.out.println("Poprawny ostatni odczyt :" + correctedScore);
                elevation[(numberOfPosition*2)-1] = correctedScore;
                //System.out.println("Zapisany odczyt na ostatniej pozycji :" + (elevation[(numberOfPosition*2)-1]));
                addElevation += (elevation[(numberOfPosition*2)-2]-elevation[(numberOfPosition*2)-1]);
                //System.out.println("Wynikowe przewy¿szenie po poprawce: " + addElevation);


                temp = nrLine + "+" + elevationValueToGsiString(nrFroog) + " 32...8+0" + measuringLength.get((numberOfPosition*2)-2) + " 331.08+"
                        + elevationValueToGsiString((numberOfPosition*2)-2) + " 390...+00000002 391.08+" + accuracyGenerator();
                fm.format("%s \n", temp);
                nrLine ++;
                nrFroog ++;

                temp = nrLine + "+" + endRape +" 32...8+0" + measuringLength.get((numberOfPosition*2)-1) + " 332.08+" + elevationValueToGsiString(elevation[(numberOfPosition*2)-1]) + " 390...+00000002 391.08+"
                        + accuracyGenerator();
                fm.format("%s \n", temp);

                nrLine ++;


                temp = nrLine + "+" + endRape + " 573..8" +(((measuringLength.get((numberOfPosition*2)-2) - measuringLength.get((numberOfPosition*2)-1)) >= 0)? "+":"-") + elevationValueToGsiString(Math.abs(measuringLength.get((numberOfPosition*2)-2) - measuringLength.get((numberOfPosition*2)-1)))
                        +  " 574.." + ((addMeasuringLength > 99999999)? "6": "8") + "+" + elevationValueToGsiString(addMeasuringLength) +  " 83..08" +
                        (((addElevation) >= 0)? "+": "-")
                        + (elevationValueToGsiString(Math.abs(addElevation)));
                fm.format("%s \n", temp);



                fm.close();
                fw.close();
                System.out.println("zakoñczono");
            }
        } catch (Exception var13) {
            System.out.println("Nie uda³o siê zapisaæ do pliku");
        }




    }









}
